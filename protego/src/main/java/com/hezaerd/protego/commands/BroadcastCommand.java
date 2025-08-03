package com.hezaerd.protego.commands;

import com.hezaerd.lumos.text.TextHelper;
import com.hezaerd.protego.managers.BroadcastManager;
import com.hezaerd.protego.permissions.PermissionManager;
import com.hezaerd.protego.text.TranslationKeys;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;

public final class BroadcastCommand {

    private BroadcastCommand() {}

    /**
     * Register the broadcast command
     * @param dispatcher The command dispatcher
     */
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("broadcast")
                .requires(source -> source.hasPermissionLevel(2) ||
                        (source.getPlayer() != null &&
                                PermissionManager.hasPermission(source.getPlayer(), "protego.broadcast")))
                .then(CommandManager.literal("announcement")
                        .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                .executes(BroadcastCommand::executeAnnouncement)))
                .then(CommandManager.literal("alert")
                        .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                .executes(BroadcastCommand::executeAlert)))
                .then(CommandManager.literal("info")
                        .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                .executes(BroadcastCommand::executeInfo)))
                .then(CommandManager.literal("raw")
                        .requires(source -> source.hasPermissionLevel(3) ||
                                (source.getPlayer() != null &&
                                        PermissionManager.hasPermission(source.getPlayer(), "protego.broadcast.raw")))
                        .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                .executes(BroadcastCommand::executeRaw)))
                .then(CommandManager.literal("to")
                        .requires(source -> source.hasPermissionLevel(3) ||
                                (source.getPlayer() != null &&
                                        PermissionManager.hasPermission(source.getPlayer(), "protego.broadcast.target")))
                        .then(CommandManager.argument("permission", StringArgumentType.word())
                                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                                        .executes(BroadcastCommand::executeToPermission))))
        );

        // Alias commands for convenience
        dispatcher.register(CommandManager.literal("announce")
                .requires(source -> source.hasPermissionLevel(2) ||
                        (source.getPlayer() != null &&
                                PermissionManager.hasPermission(source.getPlayer(), "protego.broadcast")))
                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                        .executes(BroadcastCommand::executeAnnouncement)));

        dispatcher.register(CommandManager.literal("alert")
                .requires(source -> source.hasPermissionLevel(2) ||
                        (source.getPlayer() != null &&
                                PermissionManager.hasPermission(source.getPlayer(), "protego.broadcast")))
                .then(CommandManager.argument("message", StringArgumentType.greedyString())
                        .executes(BroadcastCommand::executeAlert)));
    }

    private static int executeAnnouncement(CommandContext<ServerCommandSource> context) {
        String message = StringArgumentType.getString(context, "message");
        ServerCommandSource source = context.getSource();

        if (checkPermission(source, "protego.broadcast")) {
            ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Broadcast.ERROR_NO_PERMISSION));
            return 0;
        }

        BroadcastManager.broadcast(source.getServer(), BroadcastManager.BroadcastType.ANNOUNCEMENT, message);
        ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Broadcast.SUCCESS_ANNOUNCEMENT));

        return 1;
    }

    private static int executeAlert(CommandContext<ServerCommandSource> context) {
        String message = StringArgumentType.getString(context, "message");
        ServerCommandSource source = context.getSource();

        if (checkPermission(source, "protego.broadcast")) {
            ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Broadcast.ERROR_NO_PERMISSION));
            return 0;
        }

        BroadcastManager.broadcast(source.getServer(), BroadcastManager.BroadcastType.ALERT, message);
        ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Broadcast.SUCCESS_ALERT));

        return 1;
    }

    private static int executeInfo(CommandContext<ServerCommandSource> context) {
        String message = StringArgumentType.getString(context, "message");
        ServerCommandSource source = context.getSource();

        if (checkPermission(source, "protego.broadcast")) {
            ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Broadcast.ERROR_NO_PERMISSION));
            return 0;
        }

        BroadcastManager.broadcast(source.getServer(), BroadcastManager.BroadcastType.INFO, message);
        ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Broadcast.SUCCESS_INFO));

        return 1;
    }

    private static int executeRaw(CommandContext<ServerCommandSource> context) {
        String message = StringArgumentType.getString(context, "message");
        ServerCommandSource source = context.getSource();

        if (checkPermission(source, "protego.broadcast.raw")) {
            ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Broadcast.ERROR_NO_PERMISSION));
            return 0;
        }

        BroadcastManager.sendRawMessage(source.getServer(), message);
        ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Broadcast.SUCCESS_RAW));

        return 1;
    }

    private static int executeToPermission(CommandContext<ServerCommandSource> context) {
        String permission = StringArgumentType.getString(context, "permission");
        String message = StringArgumentType.getString(context, "message");
        ServerCommandSource source = context.getSource();

        if (checkPermission(source, "protego.broadcast.target")) {
            ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Broadcast.ERROR_NO_PERMISSION));
            return 0;
        }

        BroadcastManager.broadcastToPermission(source.getServer(), BroadcastManager.BroadcastType.INFO, message, permission);
        ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Broadcast.SUCCESS_TO_PERMISSION));

        return 1;
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
