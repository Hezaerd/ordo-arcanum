package com.hezaerd.protego.commands;

import com.hezaerd.lumos.text.TextHelper;
import com.hezaerd.protego.permissions.PermissionManager;
import com.hezaerd.protego.text.TranslationKeys;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class ClsCommand {

    private ClsCommand() {}

    /**
     * Register the cls command
     * @param dispatcher The command dispatcher
     */
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("cls")
                .requires(source -> source.hasPermissionLevel(2) ||
                        (source.getPlayer() != null &&
                                PermissionManager.hasPermission(source.getPlayer(), "protego.cls")))
                .executes(ClsCommand::execute));
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        if (checkPermission(source, "protego.cls")) {
            ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Cls.ERROR_NO_PERMISSION));
            return 0;
        }

        // Clear the chat by sending empty lines
        clearChat(source);

        ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Cls.SUCCESS_CLEARED));

        return 1;
    }

    /**
     * Clear the chat by sending empty lines to all players
     * @param source The command source
     */
    private static void clearChat(ServerCommandSource source) {
        // Send 100 empty lines to clear the chat
        for (int i = 0; i < 100; i++) {
            source.getServer().getPlayerManager().broadcast(Text.empty(), false);
        }
    }

    /**
     * Check if the command source has the required permission
     * @param source The command source
     * @param permission The permission to check
     * @return true if the source has permission
     */
    private static boolean checkPermission(ServerCommandSource source, String permission) {
        // Check operator level first
        if (source.hasPermissionLevel(4)) {
            return false;
        }

        // Check if it's a player and use LuckPerms
        ServerPlayerEntity player = source.getPlayer();
        if (player != null) {
            return !PermissionManager.hasPermission(player, permission);
        }

        // Console always has permission
        return false;
    }
}