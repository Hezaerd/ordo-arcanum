package com.hezaerd.protego.commands;

import com.hezaerd.lumos.permissions.Permissions;
import com.hezaerd.lumos.text.TextHelper;
import com.hezaerd.protego.ModLib;
import com.hezaerd.protego.managers.BroadcastManager;
import com.hezaerd.protego.text.TranslationKeys;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

public final class BroadcastCommand {

	private BroadcastCommand() {}

	/**
	 * Register the broadcast command
	 * @param dispatcher The command dispatcher
	 */
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		ModLib.LOGGER.info("Registering broadcast command");
		dispatcher.register(CommandManager.literal("broadcast")
				.requires(source -> Permissions.check(source, "protego.broadcast", 2))
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
						.requires(source -> Permissions.check(source, "protego.broadcast.raw", 3))
						.then(CommandManager.argument("message", StringArgumentType.greedyString())
								.executes(BroadcastCommand::executeRaw)))
				.then(CommandManager.literal("to")
						.requires(source -> Permissions.check(source, "protego.broadcast.target", 3))
						.then(CommandManager.argument("permission", StringArgumentType.word())
								.then(CommandManager.argument("message", StringArgumentType.greedyString())
										.executes(BroadcastCommand::executeToPermission))))
		);

		// Alias commands for convenience
		dispatcher.register(CommandManager.literal("announce")
				.requires(source -> Permissions.check(source, "protego.broadcast", 2))
				.then(CommandManager.argument("message", StringArgumentType.greedyString())
						.executes(BroadcastCommand::executeAnnouncement)));

		dispatcher.register(CommandManager.literal("alert")
				.requires(source -> Permissions.check(source, "protego.broadcast", 2))
				.then(CommandManager.argument("message", StringArgumentType.greedyString())
						.executes(BroadcastCommand::executeAlert)));
	}

	private static int executeAnnouncement(CommandContext<ServerCommandSource> context) {
		String message = StringArgumentType.getString(context, "message");
		ServerCommandSource source = context.getSource();

		if (!Permissions.check(source, "protego.broadcast", 2)) {
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

		if (!Permissions.check(source, "protego.broadcast", 2)) {
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

		if (!Permissions.check(source, "protego.broadcast", 2)) {
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

		if (!Permissions.check(source, "protego.broadcast.raw", 3)) {
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

		if (!Permissions.check(source, "protego.broadcast.target", 3)) {
			ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Broadcast.ERROR_NO_PERMISSION));
			return 0;
		}

		BroadcastManager.broadcastToPermission(source.getServer(), BroadcastManager.BroadcastType.INFO, message, permission);
		ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Broadcast.SUCCESS_TO_PERMISSION));

		return 1;
	}
}
