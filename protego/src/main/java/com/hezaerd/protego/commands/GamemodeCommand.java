package com.hezaerd.protego.commands;

import com.hezaerd.lumos.text.TextHelper;
import com.hezaerd.protego.text.TranslationKeys;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import net.minecraft.text.Text;
import com.hezaerd.protego.ModLib;
import com.hezaerd.protego.permissions.PermissionManager;
import net.minecraft.command.argument.EntityArgumentType;

import java.util.Arrays;
import java.util.List;

public final class GamemodeCommand {

    private GamemodeCommand() {}

    /**
     * Gamemode suggestions provider
     */
    public static final SuggestionProvider<ServerCommandSource> GAMEMODE_SUGGESTIONS = SuggestionProviders.register(
            ModLib.id("gamemode_modes"),
            (context, builder) -> {
                List<String> modes = Arrays.asList("0", "1", "2", "3");
                for (String mode : modes) {
                    builder.suggest(mode);
                }
                return builder.buildFuture();
            }
    );

    /**
     * Register the gamemode command
     * @param dispatcher The command dispatcher
     */
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("gm")
                .requires(source -> source.hasPermissionLevel(2) ||
                        (source.getPlayer() != null &&
                                source.getPlayer().hasPermissionLevel(2)))
                .then(CommandManager.argument("mode", StringArgumentType.word())
                        .suggests(GAMEMODE_SUGGESTIONS)
                        .executes(GamemodeCommand::executeGamemode))
                .then(CommandManager.argument("mode", StringArgumentType.word())
                        .suggests(GAMEMODE_SUGGESTIONS)
                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .executes(GamemodeCommand::executeGamemodeTarget))));
    }

        /**
     * Execute the gamemode command
     * @param context The command context
     * @return 1 if successful, 0 if failed
     */
    private static int executeGamemode(CommandContext<ServerCommandSource> context) {
        String modeArg = StringArgumentType.getString(context, "mode").toLowerCase();
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null) {
            ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Gamemode.ERROR_PLAYERS_ONLY));
            return 0;
        }

        GameMode gameMode = parseGamemode(modeArg);
        if (gameMode == null) {
            ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Gamemode.ERROR_INVALID_GAMEMODE));
            return 0;
        }

        // Check if player has permission to change gamemode
        if (checkGamemodePermission(source)) {
            ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Gamemode.ERROR_NO_PERMISSION));
            return 0;
        }

        // Set the gamemode
        player.changeGameMode(gameMode);

        String modeName = getGamemodeName(gameMode);
        ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Gamemode.SUCCESS_CHANGED_SELF, modeName));

        return 1;
    }

        /**
     * Execute the gamemode command with a target player
     * @param context The command context
     * @return 1 if successful, 0 if failed
     */
    private static int executeGamemodeTarget(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String modeArg = StringArgumentType.getString(context, "mode").toLowerCase();
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");

        GameMode gameMode = parseGamemode(modeArg);
        if (gameMode == null) {
            ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Gamemode.ERROR_INVALID_GAMEMODE));
            return 0;
        }

        // Check if player has permission to change gamemode
        if (checkGamemodePermission(source)) {
            ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Gamemode.ERROR_NO_PERMISSION));
            return 0;
        }

        // Set the gamemode for the target player
        target.changeGameMode(gameMode);

        String modeName = getGamemodeName(gameMode);
        ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Gamemode.SUCCESS_CHANGED_TARGET, target.getName().getString(), modeName));

        return 1;
    }

    /**
     * Check if the command source has permission to change gamemode
     * @param source The command source
     * @return true if the source does NOT have permission
     */
    private static boolean checkGamemodePermission(ServerCommandSource source) {
        // Check operator level first (level 2+ can use gamemode)
        if (source.hasPermissionLevel(2)) {
            return false;
        }

        // Check if it's a player and use LuckPerms
        ServerPlayerEntity player = source.getPlayer();
        if (player != null) {
            return !PermissionManager.hasPermission(player, "minecraft.command.gamemode");
        }

        // Console always has permission
        return false;
    }

    /**
     * Parse the gamemode argument
     * @param modeArg The mode argument string
     * @return The GameMode or null if invalid
     */
    private static GameMode parseGamemode(String modeArg) {
        return switch (modeArg) {
            case "0" -> GameMode.SURVIVAL;
            case "1" -> GameMode.CREATIVE;
            case "2" -> GameMode.ADVENTURE;
            case "3" -> GameMode.SPECTATOR;
            default -> null;
        };
    }

    /**
     * Get the display name for a gamemode
     * @param gameMode The gamemode
     * @return The display name
     */
    private static String getGamemodeName(GameMode gameMode) {
        return switch (gameMode) {
            case SURVIVAL -> TextHelper.getTranslatedString(TranslationKeys.Commands.Gamemode.GAMEMODE_SURVIVAL);
            case CREATIVE -> TextHelper.getTranslatedString(TranslationKeys.Commands.Gamemode.GAMEMODE_CREATIVE);
            case ADVENTURE -> TextHelper.getTranslatedString(TranslationKeys.Commands.Gamemode.GAMEMODE_ADVENTURE);
            case SPECTATOR -> TextHelper.getTranslatedString(TranslationKeys.Commands.Gamemode.GAMEMODE_SPECTATOR);
        };
    }
}