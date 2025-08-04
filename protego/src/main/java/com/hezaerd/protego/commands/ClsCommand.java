package com.hezaerd.protego.commands;

import static net.minecraft.server.command.CommandManager.literal;

import com.hezaerd.lumos.permissions.Permissions;
import com.hezaerd.lumos.text.TextHelper;
import com.hezaerd.protego.ModLib;
import com.hezaerd.protego.text.TranslationKeys;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

public final class ClsCommand {

	private ClsCommand() {}

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		ModLib.LOGGER.info("Registering cls command");
		dispatcher.register(literal("cls")
				.requires(source -> Permissions.check(source, "protego.cls", 2))
				.executes(ClsCommand::executeCls));
	}

	/**
	 * Execute the cls command
	 * @param context The command context
	 * @return 1 if successful, 0 if failed
	 */
	private static int executeCls(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();

		// Check if player has permission to clear chat
		if (!Permissions.check(source, "protego.cls", 2)) {
			ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Cls.ERROR_NO_PERMISSION));
			return 0;
		}

		// Send 100 empty lines to clear the chat
		for (int i = 0; i < 100; i++) {
			source.getServer().getPlayerManager().broadcast(Text.empty(), false);
		}

		ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Cls.SUCCESS_CLEARED));

		return 1;
	}
}
