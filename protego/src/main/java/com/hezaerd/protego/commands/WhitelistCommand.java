package com.hezaerd.protego.commands;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.hezaerd.lumos.permissions.Permissions;
import com.hezaerd.lumos.text.RichText;
import com.hezaerd.lumos.text.TextHelper;
import com.hezaerd.protego.ModLib;
import com.hezaerd.protego.managers.WhitelistManager;
import com.hezaerd.protego.text.TranslationKeys;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;

public final class WhitelistCommand {

	private WhitelistCommand() {}

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		ModLib.LOGGER.info("Registering whitelist commands");
		dispatcher.register(literal("whitelist")
			.requires(source -> Permissions.check(source, "protego.whitelist", 2))
			.executes(WhitelistCommand::executeHelp)
			.then(literal("help")
				.executes(WhitelistCommand::executeHelp))
			.then(literal("create")
				.then(argument("name", StringArgumentType.word())
				.then(argument("displayName", StringArgumentType.greedyString())
				.executes(context -> createWhitelist(context, StringArgumentType.getString(context, "name"),
					StringArgumentType.getString(context, "displayName"), ""))
				.then(argument("description", StringArgumentType.greedyString())
				.executes(context -> createWhitelist(context, StringArgumentType.getString(context, "name"),
					StringArgumentType.getString(context, "displayName"),
					StringArgumentType.getString(context, "description"))))))
			)
			.then(literal("delete")
				.then(argument("name", StringArgumentType.word())
				.suggests(WHITELIST_NAME_SUGGESTIONS)
				.executes(context -> deleteWhitelist(context, StringArgumentType.getString(context, "name"))))
			)
			.then(literal("list")
				.executes(WhitelistCommand::listWhitelists)
			)
			.then(literal("activate")
				.then(argument("name", StringArgumentType.word())
				.suggests(WHITELIST_NAME_SUGGESTIONS)
				.executes(context -> setWhitelistActive(context, StringArgumentType.getString(context, "name"), true)))
			)
			.then(literal("deactivate")
				.then(argument("name", StringArgumentType.word())
				.suggests(WHITELIST_NAME_SUGGESTIONS)
				.executes(context -> setWhitelistActive(context, StringArgumentType.getString(context, "name"), false)))
			)
			.then(literal("add")
				.then(argument("whitelist", StringArgumentType.word())
				.suggests(WHITELIST_NAME_SUGGESTIONS)
				.then(argument("player", EntityArgumentType.players())
				.executes(context -> addPlayerToWhitelist(context, StringArgumentType.getString(context, "whitelist"),
					EntityArgumentType.getPlayers(context, "player")))))
			)
			.then(literal("remove")
				.then(argument("whitelist", StringArgumentType.word())
				.suggests(WHITELIST_NAME_SUGGESTIONS)
				.then(argument("player", EntityArgumentType.players())
				.executes(context -> removePlayerFromWhitelist(context, StringArgumentType.getString(context, "whitelist"),
					EntityArgumentType.getPlayers(context, "player")))))
			)
			.then(literal("addmultiple")
				.then(argument("whitelists", StringArgumentType.greedyString())
				.then(argument("player", EntityArgumentType.players())
				.executes(context -> addPlayerToMultipleWhitelists(context,
					StringArgumentType.getString(context, "whitelists"),
					EntityArgumentType.getPlayers(context, "player")))))
			)
			.then(literal("removemultiple")
				.then(argument("whitelists", StringArgumentType.greedyString())
				.then(argument("player", EntityArgumentType.players())
				.executes(context -> removePlayerFromMultipleWhitelists(context,
					StringArgumentType.getString(context, "whitelists"),
					EntityArgumentType.getPlayers(context, "player")))))
			)
			.then(literal("players")
				.then(argument("whitelist", StringArgumentType.word())
				.suggests(WHITELIST_NAME_SUGGESTIONS)
				.executes(context -> listWhitelistPlayers(context, StringArgumentType.getString(context, "whitelist"))))
			)
			.then(literal("player")
				.then(argument("player", EntityArgumentType.players())
				.executes(context -> showPlayerWhitelists(context, EntityArgumentType.getPlayers(context, "player"))))
			)
		);
		ModLib.LOGGER.info("Whitelist commands registered successfully");
	}

	/**
	 * Execute the /whitelist command with no arguments or with "help" (show help)
	 * @param context The command context
	 * @return 1 if successful, 0 if failed
	 */
	private static int executeHelp(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();
		ServerPlayerEntity player = source.getPlayer();

		if (player == null) {
			source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Whitelist.ERROR_PLAYERS_ONLY));
			return 0;
		}

		source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Whitelist.HELP_TITLE));
		source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Whitelist.HELP_CREATE));
		source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Whitelist.HELP_DELETE));
		source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Whitelist.HELP_LIST));
		source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Whitelist.HELP_ACTIVATE));
		source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Whitelist.HELP_DEACTIVATE));
		source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Whitelist.HELP_ADD));
		source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Whitelist.HELP_REMOVE));
		source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Whitelist.HELP_ADD_MULTIPLE));
		source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Whitelist.HELP_REMOVE_MULTIPLE));
		source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Whitelist.HELP_PLAYERS));
		source.sendMessage(TextHelper.getTranslatedRichText(TranslationKeys.Commands.Whitelist.HELP_PLAYER));

		return 1;
	}



	// Command implementations
	private static int createWhitelist(CommandContext<ServerCommandSource> context, String name, String displayName, String description) {
		ServerCommandSource source = context.getSource();
		UUID createdBy = source.getPlayer() != null ? source.getPlayer().getUuid() : UUID.randomUUID();

		ModLib.LOGGER.info("Creating whitelist: {} with display name: {}", name, displayName);

		WhitelistManager.getInstance().createWhitelist(name, displayName, description, createdBy)
			.thenAccept(success -> {
				if (success) {
					ModLib.LOGGER.info("Whitelist '{}' created successfully", name);
					ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.SUCCESS_CREATED, name));
				} else {
					ModLib.LOGGER.warn("Failed to create whitelist '{}'", name);
					ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.ERROR_WHITELIST_EXISTS));
				}
			});

		return 1;
	}

	private static int deleteWhitelist(CommandContext<ServerCommandSource> context, String name) {
		ServerCommandSource source = context.getSource();

		ModLib.LOGGER.info("Deleting whitelist: {}", name);

		WhitelistManager.getInstance().deleteWhitelist(name)
			.thenAccept(success -> {
				if (success) {
					ModLib.LOGGER.info("Whitelist '{}' deleted successfully", name);
					ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.SUCCESS_DELETED, name));
				} else {
					ModLib.LOGGER.warn("Whitelist '{}' not found for deletion", name);
					ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.ERROR_WHITELIST_NOT_FOUND, name));
				}
			});

		return 1;
	}

	private static int listWhitelists(CommandContext<ServerCommandSource> context) {
		ServerCommandSource source = context.getSource();

		ModLib.LOGGER.info("Listing all whitelists");

		WhitelistManager.getInstance().getAllWhitelists()
			.thenAccept(whitelists -> {
				if (whitelists.isEmpty()) {
					ModLib.LOGGER.info("No whitelists found");
					ProtegoCommandManager.sendInfo(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.LIST_EMPTY));
					return;
				}

				ModLib.LOGGER.info("Found {} whitelists", whitelists.size());
				ProtegoCommandManager.sendInfo(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.LIST_HEADER));
				for (WhitelistManager.WhitelistInfo whitelist : whitelists) {
					String status = whitelist.isActive() ? "&aACTIVE" : "&cINACTIVE";
					source.sendMessage(RichText.fromColorCodes(TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.LIST_ENTRY, whitelist.getDisplayName(), whitelist.getName(), status)));
				}
				ProtegoCommandManager.sendInfo(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.LIST_FOOTER, whitelists.size()));
			});

		return 1;
	}

	private static int setWhitelistActive(CommandContext<ServerCommandSource> context, String name, boolean active) {
		ServerCommandSource source = context.getSource();

		ModLib.LOGGER.info("Setting whitelist '{}' active: {}", name, active);

		WhitelistManager.getInstance().setWhitelistActive(name, active)
			.thenAccept(success -> {
				if (success) {
					String action = active ? "activated" : "deactivated";
					ModLib.LOGGER.info("Whitelist '{}' {} successfully", name, action);
					if (active) {
						ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.SUCCESS_ACTIVATED, name));
					} else {
						ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.SUCCESS_DEACTIVATED, name));
					}
				} else {
					ModLib.LOGGER.warn("Whitelist '{}' not found for activation/deactivation", name);
					ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.ERROR_WHITELIST_NOT_FOUND, name));
				}
			});

		return 1;
	}

	private static int addPlayerToWhitelist(CommandContext<ServerCommandSource> context, String whitelistName, Collection<ServerPlayerEntity> players) {
		ServerCommandSource source = context.getSource();
		UUID addedBy = source.getPlayer() != null ? source.getPlayer().getUuid() : UUID.randomUUID();

		ModLib.LOGGER.info("Adding {} players to whitelist '{}'", players.size(), whitelistName);

		// Process each player
		for (ServerPlayerEntity player : players) {
			String playerName = player.getName().getString();
			ModLib.LOGGER.info("Adding player '{}' to whitelist '{}'", playerName, whitelistName);

			WhitelistManager.getInstance().addPlayerToWhitelist(whitelistName, player.getUuid(), playerName, addedBy)
				.thenAccept(success -> {
					if (success) {
						ModLib.LOGGER.info("Added player '{}' to whitelist '{}'", playerName, whitelistName);
						ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.SUCCESS_ADDED_PLAYER, playerName, whitelistName));
					} else {
						ModLib.LOGGER.warn("Failed to add player '{}' to whitelist '{}'", playerName, whitelistName);
						ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.ERROR_PLAYER_ALREADY_IN_WHITELIST, playerName, whitelistName));
					}
				});
		}

		return players.size();
	}

	private static int removePlayerFromWhitelist(CommandContext<ServerCommandSource> context, String whitelistName, Collection<ServerPlayerEntity> players) {
		ServerCommandSource source = context.getSource();

		ModLib.LOGGER.info("Removing {} players from whitelist '{}'", players.size(), whitelistName);

		// Process each player
		for (ServerPlayerEntity player : players) {
			String playerName = player.getName().getString();
			ModLib.LOGGER.info("Removing player '{}' from whitelist '{}'", playerName, whitelistName);

			WhitelistManager.getInstance().removePlayerFromWhitelist(whitelistName, player.getUuid())
				.thenAccept(success -> {
					if (success) {
						ModLib.LOGGER.info("Removed player '{}' from whitelist '{}'", playerName, whitelistName);
						ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.SUCCESS_REMOVED_PLAYER, playerName, whitelistName));
					} else {
						ModLib.LOGGER.warn("Player '{}' not found in whitelist '{}'", playerName, whitelistName);
						ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.ERROR_PLAYER_NOT_IN_WHITELIST, playerName, whitelistName));
					}
				});
		}

		return players.size();
	}

	private static int addPlayerToMultipleWhitelists(CommandContext<ServerCommandSource> context, String whitelistNames, Collection<ServerPlayerEntity> players) {
		ServerCommandSource source = context.getSource();
		UUID addedBy = source.getPlayer() != null ? source.getPlayer().getUuid() : UUID.randomUUID();

		// Parse whitelist names
		List<String> whitelists = List.of(whitelistNames.split(","));

		ModLib.LOGGER.info("Adding {} players to {} whitelists: {}", players.size(), whitelists.size(), whitelists);

		// Process each player
		for (ServerPlayerEntity player : players) {
			String playerName = player.getName().getString();
			ModLib.LOGGER.info("Adding player '{}' to {} whitelists", playerName, whitelists.size());

			WhitelistManager.getInstance().addPlayerToMultipleWhitelists(whitelists, player.getUuid(), playerName, addedBy)
				.thenAccept(success -> {
					if (success) {
						ModLib.LOGGER.info("Added player '{}' to {} whitelists", playerName, whitelists.size());
						ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.SUCCESS_ADDED_MULTIPLE, playerName, String.join(", ", whitelists)));
					} else {
						ModLib.LOGGER.warn("Failed to add player '{}' to multiple whitelists", playerName);
						ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.ERROR_INVALID_WHITELIST_NAMES, String.join(", ", whitelists)));
					}
				});
		}

		return players.size();
	}

	private static int removePlayerFromMultipleWhitelists(CommandContext<ServerCommandSource> context, String whitelistNames, Collection<ServerPlayerEntity> players) {
		ServerCommandSource source = context.getSource();

		// Parse whitelist names
		List<String> whitelists = List.of(whitelistNames.split(","));

		ModLib.LOGGER.info("Removing {} players from {} whitelists: {}", players.size(), whitelists.size(), whitelists);

		// Process each player
		for (ServerPlayerEntity player : players) {
			String playerName = player.getName().getString();
			ModLib.LOGGER.info("Removing player '{}' from {} whitelists", playerName, whitelists.size());

			WhitelistManager.getInstance().removePlayerFromMultipleWhitelists(whitelists, player.getUuid())
				.thenAccept(success -> {
					if (success) {
						ModLib.LOGGER.info("Removed player '{}' from {} whitelists", playerName, whitelists.size());
						ProtegoCommandManager.sendSuccess(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.SUCCESS_REMOVED_MULTIPLE, playerName, String.join(", ", whitelists)));
					} else {
						ModLib.LOGGER.warn("Failed to remove player '{}' from multiple whitelists", playerName);
						ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.ERROR_INVALID_WHITELIST_NAMES, String.join(", ", whitelists)));
					}
				});
		}

		return players.size();
	}

	private static int listWhitelistPlayers(CommandContext<ServerCommandSource> context, String whitelistName) {
		ServerCommandSource source = context.getSource();

		ModLib.LOGGER.info("Listing players in whitelist: {}", whitelistName);

		WhitelistManager.getInstance().getWhitelistedPlayers(whitelistName)
			.thenAccept(players -> {
				if (players == null) {
					ModLib.LOGGER.warn("Whitelist '{}' does not exist", whitelistName);
					ProtegoCommandManager.sendError(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.ERROR_WHITELIST_NOT_FOUND, whitelistName));
					return;
				}

				if (players.isEmpty()) {
					ModLib.LOGGER.info("No players found in whitelist '{}'", whitelistName);
					ProtegoCommandManager.sendInfo(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.PLAYERS_EMPTY));
					return;
				}

				ModLib.LOGGER.info("Found {} players in whitelist '{}'", players.size(), whitelistName);
				ProtegoCommandManager.sendInfo(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.PLAYERS_HEADER, whitelistName));
				for (String player : players) {
					source.sendMessage(RichText.fromColorCodes(TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.PLAYERS_ENTRY, player)));
				}
				ProtegoCommandManager.sendInfo(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.PLAYERS_FOOTER, players.size()));
			});

		return 1;
	}

	private static int showPlayerWhitelists(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		ServerCommandSource source = context.getSource();

		ModLib.LOGGER.info("Showing whitelists for {} players", players.size());

		// Process each player
		for (ServerPlayerEntity player : players) {
			String playerName = player.getName().getString();
			ModLib.LOGGER.info("Showing whitelists for player: {}", playerName);

			WhitelistManager.getInstance().getPlayerWhitelists(player.getUuid())
				.thenAccept(playerWhitelists -> {
					if (playerWhitelists.isEmpty()) {
						ModLib.LOGGER.info("Player '{}' is not in any whitelists", playerName);
						ProtegoCommandManager.sendInfo(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.PLAYER_WHITELISTS_EMPTY));
						return;
					}

					ModLib.LOGGER.info("Player '{}' is in {} whitelists", playerName, playerWhitelists.size());
					ProtegoCommandManager.sendInfo(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.PLAYER_WHITELISTS_HEADER, playerName));
					for (WhitelistManager.PlayerWhitelistInfo info : playerWhitelists) {
						String status = info.isExpired() ? "&cEXPIRED" : "&aACTIVE";
						source.sendMessage(RichText.fromColorCodes(TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.PLAYER_WHITELISTS_ENTRY, info.getDisplayName(), info.getWhitelistName())));
					}
					ProtegoCommandManager.sendInfo(source, TextHelper.getTranslatedString(TranslationKeys.Commands.Whitelist.PLAYER_WHITELISTS_FOOTER, playerWhitelists.size()));
				});
		}

		return players.size();
	}

	// Suggestion providers
	public static final SuggestionProvider<ServerCommandSource> WHITELIST_NAME_SUGGESTIONS = SuggestionProviders.register(
		ModLib.id("whitelist_names"),
		(context, builder) -> {
			// Get whitelists from cache synchronously
			WhitelistManager manager = WhitelistManager.getInstance();
			if (manager.isInitialized()) {
				List<String> whitelistNames = manager.getCachedWhitelistNames();
				for (String name : whitelistNames) {
					builder.suggest(name);
				}
			}
			return builder.buildFuture();
		}
	);
}