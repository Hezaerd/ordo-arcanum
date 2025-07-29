package com.hezaerd.protego.commands;

import com.hezaerd.lumos.text.RichText;
import com.hezaerd.protego.ModLib;
import com.hezaerd.protego.managers.WhitelistManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class WhitelistCommand {

    private WhitelistCommand() {}

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        ModLib.LOGGER.info("Registering whitelist commands");
        dispatcher.register(literal("whitelist")
            .requires(source -> source.hasPermissionLevel(2))
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



    // Command implementations
    private static int createWhitelist(CommandContext<ServerCommandSource> context, String name, String displayName, String description) {
        ServerCommandSource source = context.getSource();
        UUID createdBy = source.getPlayer() != null ? source.getPlayer().getUuid() : UUID.randomUUID();

        ModLib.LOGGER.info("Creating whitelist: {} with display name: {}", name, displayName);

        WhitelistManager.getInstance().createWhitelist(name, displayName, description, createdBy)
            .thenAccept(success -> {
                if (success) {
                    ModLib.LOGGER.info("Whitelist '{}' created successfully", name);
                    ProtegoCommandManager.sendSuccess(source, "Whitelist '" + name + "' created successfully");
                } else {
                    ModLib.LOGGER.warn("Failed to create whitelist '{}'", name);
                    ProtegoCommandManager.sendError(source, "Failed to create whitelist '" + name + "'");
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
                    ProtegoCommandManager.sendSuccess(source, "Whitelist '" + name + "' deleted successfully");
                } else {
                    ModLib.LOGGER.warn("Whitelist '{}' not found for deletion", name);
                    ProtegoCommandManager.sendError(source, "Whitelist '" + name + "' not found");
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
                    ProtegoCommandManager.sendInfo(source, "No whitelists found");
                    return;
                }

                ModLib.LOGGER.info("Found {} whitelists", whitelists.size());
                ProtegoCommandManager.sendInfo(source, "Available whitelists:");
                for (WhitelistManager.WhitelistInfo whitelist : whitelists) {
                    String status = whitelist.isActive() ? "&aACTIVE" : "&cINACTIVE";
                    source.sendMessage(RichText.fromColorCodes("&7- &f" + whitelist.getDisplayName() + " &7(" + whitelist.getName() + ") " + status));
                }
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
                    ProtegoCommandManager.sendSuccess(source, "Whitelist '" + name + "' " + action + " successfully");
                } else {
                    ModLib.LOGGER.warn("Whitelist '{}' not found for activation/deactivation", name);
                    ProtegoCommandManager.sendError(source, "Whitelist '" + name + "' not found");
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
                        ProtegoCommandManager.sendSuccess(source, "Added '" + playerName + "' to whitelist '" + whitelistName + "'");
                    } else {
                        ModLib.LOGGER.warn("Failed to add player '{}' to whitelist '{}'", playerName, whitelistName);
                        ProtegoCommandManager.sendError(source, "Failed to add player '" + playerName + "' to whitelist");
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
                        ProtegoCommandManager.sendSuccess(source, "Removed '" + playerName + "' from whitelist '" + whitelistName + "'");
                    } else {
                        ModLib.LOGGER.warn("Player '{}' not found in whitelist '{}'", playerName, whitelistName);
                        ProtegoCommandManager.sendError(source, "Player '" + playerName + "' not found in whitelist");
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
                        ProtegoCommandManager.sendSuccess(source, "Added '" + playerName + "' to " + whitelists.size() + " whitelists");
                    } else {
                        ModLib.LOGGER.warn("Failed to add player '{}' to multiple whitelists", playerName);
                        ProtegoCommandManager.sendError(source, "Failed to add player '" + playerName + "' to whitelists");
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
                        ProtegoCommandManager.sendSuccess(source, "Removed '" + playerName + "' from " + whitelists.size() + " whitelists");
                    } else {
                        ModLib.LOGGER.warn("Failed to remove player '{}' from multiple whitelists", playerName);
                        ProtegoCommandManager.sendError(source, "Failed to remove player '" + playerName + "' from whitelists");
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
                    ProtegoCommandManager.sendError(source, "Whitelist '" + whitelistName + "' does not exist");
                    return;
                }

                if (players.isEmpty()) {
                    ModLib.LOGGER.info("No players found in whitelist '{}'", whitelistName);
                    ProtegoCommandManager.sendInfo(source, "No players in whitelist '" + whitelistName + "'");
                    return;
                }

                ModLib.LOGGER.info("Found {} players in whitelist '{}'", players.size(), whitelistName);
                ProtegoCommandManager.sendInfo(source, "Players in whitelist '" + whitelistName + "':");
                for (String player : players) {
                    source.sendMessage(RichText.fromColorCodes("&7- &f" + player));
                }
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
                        ProtegoCommandManager.sendInfo(source, "Player '" + playerName + "' is not in any whitelists");
                        return;
                    }

                    ModLib.LOGGER.info("Player '{}' is in {} whitelists", playerName, playerWhitelists.size());
                    ProtegoCommandManager.sendInfo(source, "Whitelists for '" + playerName + "':");
                    for (WhitelistManager.PlayerWhitelistInfo info : playerWhitelists) {
                        String status = info.isExpired() ? "&cEXPIRED" : "&aACTIVE";
                        source.sendMessage(RichText.fromColorCodes("&7- &f" + info.getDisplayName() + " &7(" + info.getWhitelistName() + ") " + status));
                    }
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