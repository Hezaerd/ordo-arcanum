package com.hezaerd.protego.commands;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.hezaerd.lumos.permissions.Permissions;
import com.hezaerd.lumos.text.TextHelper;
import com.hezaerd.protego.ModLib;
import com.hezaerd.protego.text.TranslationKeys;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public final class GamemodeCommand {

	private GamemodeCommand() {}

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		ModLib.LOGGER.info("Registering gamemode command");
		dispatcher.register(literal("gamemode")
				.requires(source -> Permissions.check(source, "minecraft.command.gamemode", 2))
				.then(argument("mode", StringArgumentType.word())
						.executes(GamemodeCommand::executeGamemodeSelf)
						.then(argument("target", EntityArgumentType.player())
								.executes(GamemodeCommand::executeGamemodeTarget))));

		// Add aliases
		ModLib.LOGGER.info("Registering gamemode command 'gm' aliases");
		dispatcher.register(literal("gm")
				.requires(source -> Permissions.check(source, "minecraft.command.gamemode", 2))
				.then(argument("mode", StringArgumentType.word())
						.executes(GamemodeCommand::executeGamemodeSelf)
						.then(argument("target", EntityArgumentType.player())
								.executes(GamemodeCommand::executeGamemodeTarget))));
	}

	/**
	 * Execute the gamemode command for self
	 * @param context The command context
	 * @return 1 if successful, 0 if failed
	 */
	private static int executeGamemodeSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		String modeArg = StringArgumentType.getString(context, "mode").toLowerCase();
		ServerCommandSource source = context.getSource();
		ServerPlayerEntity player = source.getPlayerOrThrow();

		GameMode gameMode = parseGamemode(modeArg);
		if (gameMode == null) {
			ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Gamemode.ERROR_INVALID_GAMEMODE));
			return 0;
		}

		// Check if player has permission to change gamemode
		if (!Permissions.check(source, "minecraft.command.gamemode", 2)) {
			ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Gamemode.ERROR_NO_PERMISSION));
			return 0;
		}

		// Set the gamemode for the player
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
		if (!Permissions.check(source, "minecraft.command.gamemode", 2)) {
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
	 * Parse the gamemode argument
	 * @param modeArg The mode argument string
	 * @return The GameMode or null if invalid
	 */
	private static GameMode parseGamemode(String modeArg) {
		return switch (modeArg) {
			case "survival", "s", "0" -> GameMode.SURVIVAL;
			case "creative", "c", "1" -> GameMode.CREATIVE;
			case "adventure", "a", "2" -> GameMode.ADVENTURE;
			case "spectator", "sp", "3" -> GameMode.SPECTATOR;
			default -> null;
		};
	}

	/**
	 * Get the user-friendly name of a gamemode
	 * @param gameMode The GameMode
	 * @return The user-friendly name
	 */
	private static String getGamemodeName(GameMode gameMode) {
		return switch (gameMode) {
			case SURVIVAL -> "Survival";
			case CREATIVE -> "Creative";
			case ADVENTURE -> "Adventure";
			case SPECTATOR -> "Spectator";
		};
	}
}
