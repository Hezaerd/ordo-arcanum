package com.hezaerd.accio.commands;

import com.hezaerd.accio.chat.ChatTracker;
import com.hezaerd.accio.registry.ModStats;
import com.hezaerd.accio.text.TranslationKeys;
import com.hezaerd.lumos.text.TextHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FeurCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger("accio-feur-command");

    private FeurCommand() {}

    /**
     * Register the feur command
     * @param dispatcher The command dispatcher
     */
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("feur")
                .executes(FeurCommand::execute));
    }

        /**
     * Execute the /feur command
     * @param context The command context
     * @return 1 if successful, 0 if failed
     */
    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null) {
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Feur.ERROR_PLAYERS_ONLY));
            return 0;
        }

        // Check if there's a valid player who said a trigger word
        ServerPlayerEntity triggerPlayer = ChatTracker.getLastTriggerPlayer();
        String triggerWord = ChatTracker.getLastTriggerWord();

        if (triggerPlayer == null) {
            // No one said a trigger word recently, kick the command user
            player.increaseStat(ModStats.FAILED_FEUR, 1);
            source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Feur.ERROR_NO_TRIGGER));
            kickPlayer(player, "Feur");
            return 1;
        }

        // Someone said a trigger word, kick them instead
        player.increaseStat(ModStats.SUCCESS_FEUR, 1);
        kickPlayer(triggerPlayer, "Feur");
        source.sendMessage(TextHelper.getTranslatedRichTextWithPrefix("[Accio] ", Formatting.GREEN,
            TranslationKeys.Commands.Feur.SUCCESS_KICKED_TRIGGER, triggerPlayer.getName().getString(), triggerWord));

        // Clear the tracked player
        ChatTracker.clearLastTriggerPlayer();

        return 1;
    }

    /**
     * Kick a player with a custom reason
     * @param player The player to kick
     * @param reason The kick reason
     */
    private static void kickPlayer(ServerPlayerEntity player, String reason) {
        try {
            Text kickMessage = TextHelper.getTranslatedRichText(TranslationKeys.Commands.Feur.KICK_MESSAGE, reason);
            player.networkHandler.disconnect(kickMessage);
            LOGGER.info("Player {} was kicked for: {}", player.getName().getString(), reason);
        } catch (Exception e) {
            LOGGER.error("Failed to kick player {}: {}", player.getName().getString(), e.getMessage(), e);
        }
    }
}