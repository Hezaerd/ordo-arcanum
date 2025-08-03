package com.hezaerd.accio.commands;

import com.hezaerd.lumos.text.RichText;
import com.hezaerd.lumos.text.TextHelper;
import com.hezaerd.accio.registry.ModStats;
import com.hezaerd.accio.text.TranslationKeys;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.sun.jna.platform.win32.Winsock2;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public final class DiceCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger("accio-dice-command");
    private static final Random RANDOM = new Random();

    // Timeout and cooldown constants (in milliseconds)
    private static final long DUEL_TIMEOUT = 30000; // 30 seconds
    private static final long PLAYER_COOLDOWN = 60000; // 60 seconds (1 minute)

        // Store pending duel requests: challenger -> target
    private static final Map<String, String> pendingDuels = new HashMap<>();

    // Store challenge timestamps: challenger -> timestamp
    private static final Map<String, Long> challengeTimestamps = new HashMap<>();

    // Store dice types for pending duels: challenger -> dice_type
    private static final Map<String, String> pendingDiceTypes = new HashMap<>();

        // Store player cooldowns: player -> last challenge timestamp
    private static final Map<String, Long> playerCooldowns = new ConcurrentHashMap<>();

    // Suggestion provider for online players
    private static final SuggestionProvider<ServerCommandSource> ONLINE_PLAYERS = (context, builder) -> {
        ServerCommandSource source = context.getSource();
        if (source.getServer() != null) {
            CommandSource.suggestMatching(source.getServer().getPlayerNames(), builder);
        }
        return builder.buildFuture();
    };

    // Suggestion provider for dice types
    private static final SuggestionProvider<ServerCommandSource> DICE_TYPE_SUGGESTIONS = (context, builder) -> {
        builder.suggest("d4");
        builder.suggest("d6");
        builder.suggest("d8");
        builder.suggest("d12");
        builder.suggest("d20");
        builder.suggest("d100");
        return builder.buildFuture();
    };

    // Available dice types and their sides
    private static final Map<String, Integer> DICE_TYPES = Map.of(
        "d4", 4,
        "d6", 6,
        "d8", 8,
        "d12", 12,
        "d20", 20,
        "d100", 100
    );

    private DiceCommand() {}

    /**
     * Check if a player is on cooldown
     * @param playerName The player's name
     * @return true if the player is on cooldown
     */
    private static boolean isPlayerOnCooldown(String playerName) {
        Long lastChallenge = playerCooldowns.get(playerName);
        if (lastChallenge == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        return (currentTime - lastChallenge) < PLAYER_COOLDOWN;
    }

    /**
     * Get remaining cooldown time for a player
     * @param playerName The player's name
     * @return remaining cooldown time in seconds, or 0 if not on cooldown
     */
    private static long getRemainingCooldown(String playerName) {
        Long lastChallenge = playerCooldowns.get(playerName);
        if (lastChallenge == null) {
            return 0;
        }

        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastChallenge;
        if (elapsed >= PLAYER_COOLDOWN) {
            return 0;
        }

        return (PLAYER_COOLDOWN - elapsed) / 1000;
    }

    /**
     * Check if a challenge has timed out
     * @param challengerName The challenger's name
     * @return true if the challenge has timed out
     */
    private static boolean isChallengeTimedOut(String challengerName) {
        Long timestamp = challengeTimestamps.get(challengerName);
        if (timestamp == null) {
            return true;
        }

        long currentTime = System.currentTimeMillis();
        return (currentTime - timestamp) > DUEL_TIMEOUT;
    }

        /**
     * Clear expired challenges and cooldowns
     */
    private static void clearExpiredData() {
        long currentTime = System.currentTimeMillis();

        // Clear expired challenges
        challengeTimestamps.entrySet().removeIf(entry ->
            (currentTime - entry.getValue()) > DUEL_TIMEOUT);

        // Clear expired cooldowns
        playerCooldowns.entrySet().removeIf(entry ->
            (currentTime - entry.getValue()) > PLAYER_COOLDOWN);

        // Clear expired dice types (same as challenges)
        pendingDiceTypes.entrySet().removeIf(entry ->
            (currentTime - challengeTimestamps.getOrDefault(entry.getKey(), 0L)) > DUEL_TIMEOUT);
    }

    /**
     * Register the dice command
     * @param dispatcher The command dispatcher
     */
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("dice")
                .executes(DiceCommand::executeHelp)
                .then(CommandManager.literal("duel")
                        .then(CommandManager.argument("target", StringArgumentType.word())
                                .suggests(ONLINE_PLAYERS)
                                .executes(DiceCommand::executeChallenge)
                                .then(CommandManager.argument("dice_type", StringArgumentType.word())
                                        .suggests(DICE_TYPE_SUGGESTIONS)
                                        .executes(DiceCommand::executeChallengeWithDice))))
                .then(CommandManager.literal("accept")
                        .executes(DiceCommand::executeAccept))
                .then(CommandManager.literal("stats")
                        .executes(DiceCommand::executeStats)
                        .then(CommandManager.argument("player", StringArgumentType.word())
                                .suggests(ONLINE_PLAYERS)
                                .executes(DiceCommand::executeStatsForPlayer))));
    }

    /**
     * Execute the /dice command with no arguments (show help)
     * @param context The command context
     * @return 1 if successful, 0 if failed
     */
    private static int executeHelp(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_PLAYERS_ONLY));
            return 0;
        }

        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.HELP_TITLE));
        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.HELP_CHALLENGE));
        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.HELP_CHALLENGE_DICE));
        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.HELP_ACCEPT));
        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.HELP_STATS));
        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.HELP_TIMEOUT));

        return 1;
    }

    /**
     * Execute the /dice command with a target player
     * @param context The command context
     * @return 1 if successful, 0 if failed
     */
    private static int executeChallenge(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity challenger = source.getPlayer();

        if (challenger == null) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_PLAYERS_ONLY));
            return 0;
        }

        // Clear expired data first
        clearExpiredData();

        String challengerName = challenger.getName().getString();

        // Check cooldown
        if (isPlayerOnCooldown(challengerName)) {
            long remainingCooldown = getRemainingCooldown(challengerName);
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_ON_COOLDOWN, remainingCooldown));
            return 0;
        }

        String targetName = StringArgumentType.getString(context, "target");
        MinecraftServer server = source.getServer();
        ServerPlayerEntity target = server.getPlayerManager().getPlayer(targetName);

        if (target == null) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_PLAYER_OFFLINE, targetName));
            return 0;
        }

        if (target.equals(challenger)) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_SELF_CHALLENGE));
            return 0;
        }

        String targetNameStr = target.getName().getString();

        // Check if target is on cooldown
        if (isPlayerOnCooldown(targetNameStr)) {
            long remainingCooldown = getRemainingCooldown(targetNameStr);
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_TARGET_ON_COOLDOWN, targetNameStr, remainingCooldown));
            return 0;
        }

        // Check if there's already a pending duel
        if (pendingDuels.containsKey(challengerName)) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_PENDING_DUEL));
            return 0;
        }

        // Store the duel request with timestamp
        pendingDuels.put(challengerName, targetNameStr);
        challengeTimestamps.put(challengerName, System.currentTimeMillis());

                        // Send challenge message to target
        Text challengeMessage = TextHelper.getTranslatedRichTextWithPrefix(
            "[Dice Duel] ",
            Formatting.GOLD,
            TranslationKeys.Commands.Dice.CHALLENGE_RECEIVED,
            challengerName
        );
        target.sendMessage(challengeMessage);

        // Send confirmation to challenger
        source.sendMessage(TextHelper.getTranslatedRichTextWithPrefix("[Accio] ", Formatting.GREEN,
            TranslationKeys.Commands.Dice.CHALLENGE_SENT, targetNameStr));

        LOGGER.info("Dice duel challenge: {} -> {}", challengerName, targetNameStr);
        return 1;
    }

    /**
     * Execute the /dice duel command with a target player and dice type
     * @param context The command context
     * @return 1 if successful, 0 if failed
     */
    private static int executeChallengeWithDice(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity challenger = source.getPlayer();

        if (challenger == null) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_PLAYERS_ONLY));
            return 0;
        }

        // Clear expired data first
        clearExpiredData();

        String challengerName = challenger.getName().getString();

        // Check cooldown
        if (isPlayerOnCooldown(challengerName)) {
            long remainingCooldown = getRemainingCooldown(challengerName);
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_ON_COOLDOWN, remainingCooldown));
            return 0;
        }

        String targetName = StringArgumentType.getString(context, "target");
        String diceType = StringArgumentType.getString(context, "dice_type");
        MinecraftServer server = source.getServer();
        ServerPlayerEntity target = server.getPlayerManager().getPlayer(targetName);

        if (target == null) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_PLAYER_OFFLINE, targetName));
            return 0;
        }

        if (target.equals(challenger)) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_SELF_CHALLENGE));
            return 0;
        }

        String targetNameStr = target.getName().getString();

        // Check if target is on cooldown
        if (isPlayerOnCooldown(targetNameStr)) {
            long remainingCooldown = getRemainingCooldown(targetNameStr);
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_TARGET_ON_COOLDOWN, targetNameStr, remainingCooldown));
            return 0;
        }

        // Check if there's already a pending duel
        if (pendingDuels.containsKey(challengerName)) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_PENDING_DUEL));
            return 0;
        }

        // Validate dice type
        if (!DICE_TYPES.containsKey(diceType)) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_INVALID_DICE, diceType));
            return 0;
        }

        // Store the duel request with timestamp and dice type
        pendingDuels.put(challengerName, targetNameStr);
        challengeTimestamps.put(challengerName, System.currentTimeMillis());
        pendingDiceTypes.put(challengerName, diceType);

        // Send challenge message to target
        Text challengeMessage = TextHelper.getTranslatedRichTextWithPrefix(
            "[Dice Duel] ",
            Formatting.GOLD,
            TranslationKeys.Commands.Dice.CHALLENGE_RECEIVED_WITH_DICE,
            challengerName, diceType
        );
        target.sendMessage(challengeMessage);

        // Send confirmation to challenger
        source.sendMessage(TextHelper.getTranslatedRichTextWithPrefix("[Accio] ", Formatting.GREEN,
            TranslationKeys.Commands.Dice.CHALLENGE_SENT_WITH_DICE, targetNameStr, diceType));

        LOGGER.info("Dice duel challenge: {} -> {} with {}", challengerName, targetNameStr, diceType);
        return 1;
    }

        /**
     * Execute the /dice accept command
     * @param context The command context
     * @return 1 if successful, 0 if failed
     */
    private static int executeAccept(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity accepter = source.getPlayer();

        if (accepter == null) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_PLAYERS_ONLY));
            return 0;
        }

        // Clear expired data first
        clearExpiredData();

        String accepterName = accepter.getName().getString();

        // Find who challenged this player
        String challengerName = null;
        for (Map.Entry<String, String> entry : pendingDuels.entrySet()) {
            if (entry.getValue().equals(accepterName)) {
                challengerName = entry.getKey();
                break;
            }
        }

        if (challengerName == null) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_NO_CHALLENGE));
            return 0;
        }

        // Check if the challenge has timed out
        if (isChallengeTimedOut(challengerName)) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_CHALLENGE_EXPIRED));
            pendingDuels.remove(challengerName);
            challengeTimestamps.remove(challengerName);
            return 0;
        }

        // Get the challenger player
        MinecraftServer server = source.getServer();
        ServerPlayerEntity challenger = server.getPlayerManager().getPlayer(challengerName);

        if (challenger == null) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_CHALLENGER_OFFLINE));
            pendingDuels.remove(challengerName);
            challengeTimestamps.remove(challengerName);
            return 0;
        }

        // Get dice type for this duel
        String diceType = pendingDiceTypes.getOrDefault(challengerName, "d6");

        // Remove the pending duel, timestamp, and dice type
        pendingDuels.remove(challengerName);
        challengeTimestamps.remove(challengerName);
        pendingDiceTypes.remove(challengerName);

        // Set cooldowns for both players
        long currentTime = System.currentTimeMillis();
        playerCooldowns.put(challengerName, currentTime);
        playerCooldowns.put(accepterName, currentTime);

        // Execute the duel with the specified dice type
        executeDuel(challenger, accepter, diceType);

        return 1;
    }

    /**
     * Execute the actual dice duel between two players
     * @param challenger The player who initiated the duel
     * @param target The player who accepted the duel
     */
    private static void executeDuel(ServerPlayerEntity challenger, ServerPlayerEntity target) {
        executeDuel(challenger, target, "d6"); // Default to d6
    }

    /**
     * Execute the actual dice duel between two players with specified dice type
     * @param challenger The player who initiated the duel
     * @param target The player who accepted the duel
     * @param diceType The type of dice to use
     */
    private static void executeDuel(ServerPlayerEntity challenger, ServerPlayerEntity target, String diceType) {
        // Get dice sides
        int diceSides = DICE_TYPES.getOrDefault(diceType, 6);

        // Roll dice for both players
        int challengerRoll = RANDOM.nextInt(diceSides) + 1;
        int targetRoll = RANDOM.nextInt(diceSides) + 1;

        // Determine winner and track statistics
        if (challengerRoll > targetRoll) {
            // Track statistics
            challenger.increaseStat(ModStats.DICE_DUELS_WON, 1);
            target.increaseStat(ModStats.DICE_DUELS_LOST, 1);
        } else if (targetRoll > challengerRoll) {
            // Track statistics
            target.increaseStat(ModStats.DICE_DUELS_WON, 1);
            challenger.increaseStat(ModStats.DICE_DUELS_LOST, 1);
        } else {
            // Tie - Track statistics
            challenger.increaseStat(ModStats.DICE_DUELS_TIED, 1);
            target.increaseStat(ModStats.DICE_DUELS_TIED, 1);
        }

        // Send results to both players
        Text duelResult;
        if (challengerRoll > targetRoll) {
            duelResult = TextHelper.getTranslatedRichTextWithPrefix("[Dice Duel] ", Formatting.YELLOW,
                TranslationKeys.Commands.Dice.DUEL_WIN, challenger.getName().getString(), challengerRoll, targetRoll);
        } else if (targetRoll > challengerRoll) {
            duelResult = TextHelper.getTranslatedRichTextWithPrefix("[Dice Duel] ", Formatting.YELLOW,
                TranslationKeys.Commands.Dice.DUEL_WIN, target.getName().getString(), targetRoll, challengerRoll);
        } else {
            duelResult = TextHelper.getTranslatedRichTextWithPrefix("[Dice Duel] ", Formatting.YELLOW,
                TranslationKeys.Commands.Dice.DUEL_TIE, challengerRoll);
        }
        challenger.sendMessage(duelResult);
        target.sendMessage(duelResult);

        // Send individual rolls to each player
        Text challengerRollMsg = TextHelper.getTranslatedRichTextWithPrefix("[Dice Duel] ", Formatting.AQUA,
            TranslationKeys.Commands.Dice.DUEL_ROLL, challengerRoll);
        Text targetRollMsg = TextHelper.getTranslatedRichTextWithPrefix("[Dice Duel] ", Formatting.AQUA,
            TranslationKeys.Commands.Dice.DUEL_ROLL, targetRoll);

        challenger.sendMessage(challengerRollMsg);
        target.sendMessage(targetRollMsg);

        // Send a fun message based on the result
        if (challengerRoll == 6 && targetRoll == 1) {
            Text epicMsg = TextHelper.getTranslatedRichTextWithPrefix("[Dice Duel] ", Formatting.LIGHT_PURPLE,
                TranslationKeys.Commands.Dice.DUEL_EPIC_VICTORY, challenger.getName().getString(), target.getName().getString());
            challenger.sendMessage(epicMsg);
            target.sendMessage(epicMsg);
        } else if (targetRoll == 6 && challengerRoll == 1) {
            Text epicMsg = TextHelper.getTranslatedRichTextWithPrefix("[Dice Duel] ", Formatting.LIGHT_PURPLE,
                TranslationKeys.Commands.Dice.DUEL_EPIC_VICTORY, target.getName().getString(), challenger.getName().getString());
            challenger.sendMessage(epicMsg);
            target.sendMessage(epicMsg);
        } else if (challengerRoll == targetRoll) {
            Text tieMsg = TextHelper.getTranslatedRichTextWithPrefix("[Dice Duel] ", Formatting.GOLD,
                TranslationKeys.Commands.Dice.DUEL_DRAMATIC_TIE);
            challenger.sendMessage(tieMsg);
            target.sendMessage(tieMsg);
        }

        // Log the duel result
        LOGGER.info("Dice duel result: {} ({}) vs {} ({})",
            challenger.getName().getString(), challengerRoll,
            target.getName().getString(), targetRoll);
    }

    /**
     * Clear expired duel requests and cooldowns
     */
    public static void clearExpiredDuels() {
        clearExpiredData();
    }

    /**
     * Execute the /dice stats command (show own stats)
     * @param context The command context
     * @return 1 if successful, 0 if failed
     */
    private static int executeStats(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_PLAYERS_ONLY));
            return 0;
        }

        displayStats(source, player);
        return 1;
    }

    /**
     * Execute the /dice stats <player> command (show other player's stats)
     * @param context The command context
     * @return 1 if successful, 0 if failed
     */
    private static int executeStatsForPlayer(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity requester = source.getPlayer();

        if (requester == null) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_PLAYERS_ONLY));
            return 0;
        }

        String targetName = StringArgumentType.getString(context, "player");
        MinecraftServer server = source.getServer();
        ServerPlayerEntity targetPlayer = server.getPlayerManager().getPlayer(targetName);

        if (targetPlayer == null) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.ERROR_PLAYER_OFFLINE, targetName));
            return 0;
        }

        displayStats(source, targetPlayer);
        return 1;
    }

    /**
     * Display beautiful stats for a player
     * @param source The command source
     * @param player The player to show stats for
     */
    private static void displayStats(ServerCommandSource source, ServerPlayerEntity player) {
        ServerStatHandler statHandler = player.getStatHandler();

        final int wins = statHandler.getStat(Stats.CUSTOM, ModStats.DICE_DUELS_WON);
        final int losses = statHandler.getStat(Stats.CUSTOM, ModStats.DICE_DUELS_LOST);
        final int ties = statHandler.getStat(Stats.CUSTOM, ModStats.DICE_DUELS_TIED);
        final int total = wins + losses + ties;

        final double winRate = total > 0 ? (double) wins / (wins + losses) * 100 : 0;

        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.STATS_HEADER));
        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.STATS_TITLE, player.getName().getString()));
        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.STATS_SEPARATOR));

        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.STATS_TOTAL, total));

        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.STATS_WINS, wins));
        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.STATS_LOSSES, losses));
        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.STATS_TIES, ties));

        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.STATS_WINRATE, String.format("%.1f", winRate)));

        source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Dice.STATS_FOOTER));
    }
}