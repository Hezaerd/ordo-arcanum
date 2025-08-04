package com.hezaerd.protego.commands;

import java.util.Arrays;
import java.util.List;

import com.hezaerd.lumos.text.RichText;
import com.hezaerd.protego.ModLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;

public final class ProtegoCommandManager {
	private static final Logger LOGGER = LoggerFactory.getLogger("protego-commands");

	private ProtegoCommandManager() {}

	/**
	 * Register all Protego commands
	 * @param dispatcher The command dispatcher
	 */
	public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
		BroadcastCommand.register(dispatcher);
		WhitelistCommand.register(dispatcher);
		GamemodeCommand.register(dispatcher);
		ClsCommand.register(dispatcher);
		LOGGER.info("Protego commands registered successfully");
	}

	/**
	 * Get broadcast type suggestions
	 */
	public static final SuggestionProvider<ServerCommandSource> BROADCAST_TYPE_SUGGESTIONS = SuggestionProviders.register(
			ModLib.id("broadcast_types"),
			(context, builder) -> {
				List<String> types = Arrays.asList("announcement", "alert", "info", "admin", "emergency");
				for (String type : types) {
					builder.suggest(type);
				}
				return builder.buildFuture();
			}
	);

	/**
	 * Send a formatted error message to the command source
	 * @param source The command source
	 * @param message The error message
	 */
	public static void sendError(ServerCommandSource source, String message) {
		Text errorText = RichText.fromColorCodesWithPrefix("[Protego] ", Formatting.RED, message);
		source.sendMessage(errorText);
	}

	/**
	 * Send a formatted success message to the command source
	 * @param source The command source
	 * @param message The success message
	 */
	public static void sendSuccess(ServerCommandSource source, String message) {
		Text successText = RichText.fromColorCodesWithPrefix("[Protego] ", Formatting.GREEN, message);
		source.sendMessage(successText);
	}

	/**
	 * Send a formatted info message to the command source
	 * @param source The command source
	 * @param message The info message
	 */
	public static void sendInfo(ServerCommandSource source, String message) {
		Text infoText = RichText.fromColorCodesWithPrefix("[Protego] ", Formatting.AQUA, message);
		source.sendMessage(infoText);
	}
}
