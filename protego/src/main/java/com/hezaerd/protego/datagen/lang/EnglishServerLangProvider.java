package com.hezaerd.protego.datagen.lang;

import com.hezaerd.protego.text.TranslationKeys;
import com.hezaerd.lumos.datagen.provider.ServerLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class EnglishServerLangProvider extends ServerLanguageProvider {
    public EnglishServerLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        registerStats(translationBuilder);
        registerBroadcastCommands(translationBuilder);
        registerGamemodeCommands(translationBuilder);
        registerWhitelistCommands(translationBuilder);
        registerClsCommands(translationBuilder);
        registerBroadcastMessages(translationBuilder);
    }

    private void registerStats(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        translationBuilder.add(TranslationKeys.Stats.BROADCASTS_SENT, "Amount of broadcasts sent");
        translationBuilder.add(TranslationKeys.Stats.GAMEMODE_CHANGES, "Amount of gamemode changes");
        translationBuilder.add(TranslationKeys.Stats.WHITELISTS_CREATED, "Amount of whitelists created");
        translationBuilder.add(TranslationKeys.Stats.WHITELISTS_DELETED, "Amount of whitelists deleted");
    }

    private void registerBroadcastCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        translationBuilder.add(TranslationKeys.Commands.Broadcast.ERROR_NO_PERMISSION, "&cYou don't have permission to use this command.");
        translationBuilder.add(TranslationKeys.Commands.Broadcast.ERROR_PLAYERS_ONLY, "&cThis command can only be used by players.");
        translationBuilder.add(TranslationKeys.Commands.Broadcast.SUCCESS_ANNOUNCEMENT, "&aAnnouncement broadcast sent successfully.");
        translationBuilder.add(TranslationKeys.Commands.Broadcast.SUCCESS_ALERT, "&aAlert broadcast sent successfully.");
        translationBuilder.add(TranslationKeys.Commands.Broadcast.SUCCESS_INFO, "&aInfo broadcast sent successfully.");
        translationBuilder.add(TranslationKeys.Commands.Broadcast.SUCCESS_RAW, "&aRaw broadcast sent successfully.");
        translationBuilder.add(TranslationKeys.Commands.Broadcast.SUCCESS_TO_PERMISSION, "&aPermission-based broadcast sent successfully.");
    }

    private void registerGamemodeCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        translationBuilder.add(TranslationKeys.Commands.Gamemode.ERROR_PLAYERS_ONLY, "&cThis command can only be used by players.");
        translationBuilder.add(TranslationKeys.Commands.Gamemode.ERROR_NO_PERMISSION, "&cYou don't have permission to change gamemode.");
        translationBuilder.add(TranslationKeys.Commands.Gamemode.ERROR_INVALID_GAMEMODE, "&cInvalid gamemode. Use: 0 (survival), 1 (creative), 2 (adventure), 3 (spectator)");
        translationBuilder.add(TranslationKeys.Commands.Gamemode.ERROR_TARGET_OFFLINE, "&cTarget player is not online.");
        translationBuilder.add(TranslationKeys.Commands.Gamemode.SUCCESS_CHANGED_SELF, "&aGamemode changed to %s");
        translationBuilder.add(TranslationKeys.Commands.Gamemode.SUCCESS_CHANGED_TARGET, "&aChanged %s's gamemode to %s");
        translationBuilder.add(TranslationKeys.Commands.Gamemode.SUCCESS_TARGET_NOTIFIED, "&aYour gamemode has been changed to %s by %s");
        translationBuilder.add(TranslationKeys.Commands.Gamemode.GAMEMODE_SURVIVAL, "Survival");
        translationBuilder.add(TranslationKeys.Commands.Gamemode.GAMEMODE_CREATIVE, "Creative");
        translationBuilder.add(TranslationKeys.Commands.Gamemode.GAMEMODE_ADVENTURE, "Adventure");
        translationBuilder.add(TranslationKeys.Commands.Gamemode.GAMEMODE_SPECTATOR, "Spectator");
    }

    private void registerWhitelistCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        // Error messages
        translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_PLAYERS_ONLY, "&cThis command can only be used by players.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_NO_PERMISSION, "&cYou don't have permission to use this command.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_WHITELIST_EXISTS, "&cA whitelist with that name already exists.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_WHITELIST_NOT_FOUND, "&cWhitelist '%s' not found.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_WHITELIST_ALREADY_ACTIVE, "&cWhitelist '%s' is already active.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_WHITELIST_ALREADY_INACTIVE, "&cWhitelist '%s' is already inactive.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_PLAYER_ALREADY_IN_WHITELIST, "&cPlayer %s is already in whitelist '%s'.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_PLAYER_NOT_IN_WHITELIST, "&cPlayer %s is not in whitelist '%s'.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_INVALID_WHITELIST_NAMES, "&cInvalid whitelist names: %s");

        // Success messages
        translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_CREATED, "&aWhitelist '%s' created successfully.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_DELETED, "&aWhitelist '%s' deleted successfully.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_ACTIVATED, "&aWhitelist '%s' activated successfully.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_DEACTIVATED, "&aWhitelist '%s' deactivated successfully.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_ADDED_PLAYER, "&aAdded %s to whitelist '%s'.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_REMOVED_PLAYER, "&aRemoved %s from whitelist '%s'.");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_ADDED_MULTIPLE, "&aAdded %s to whitelists: %s");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_REMOVED_MULTIPLE, "&aRemoved %s from whitelists: %s");

        // List messages
        translationBuilder.add(TranslationKeys.Commands.Whitelist.LIST_HEADER, "&6&l📋 &fAvailable Whitelists:");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.LIST_ENTRY, "&e• %s &7(%s) &f- %s");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.LIST_FOOTER, "&7Total: %d whitelists");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.LIST_EMPTY, "&7No whitelists found.");

        // Player list messages
        translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYERS_HEADER, "&6&l👥 &fPlayers in whitelist '%s':");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYERS_ENTRY, "&e• %s");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYERS_FOOTER, "&7Total: %d players");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYERS_EMPTY, "&7No players in this whitelist.");

        // Player whitelists messages
        translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYER_WHITELISTS_HEADER, "&6&l📋 &fWhitelists for player %s:");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYER_WHITELISTS_ENTRY, "&e• %s &7(%s)");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYER_WHITELISTS_FOOTER, "&7Total: %d whitelists");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYER_WHITELISTS_EMPTY, "&7Player is not in any whitelists.");

        // Help messages
        translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_TITLE, "&6&l📋 &fWhitelist Commands:");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_CREATE, "&e/whitelist create <name> <display_name> [description] &7- Create a new whitelist");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_DELETE, "&e/whitelist delete <name> &7- Delete a whitelist");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_LIST, "&e/whitelist list &7- List all whitelists");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_ACTIVATE, "&e/whitelist activate <name> &7- Activate a whitelist");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_DEACTIVATE, "&e/whitelist deactivate <name> &7- Deactivate a whitelist");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_ADD, "&e/whitelist add <whitelist> <player> &7- Add player to whitelist");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_REMOVE, "&e/whitelist remove <whitelist> <player> &7- Remove player from whitelist");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_ADD_MULTIPLE, "&e/whitelist addmultiple <whitelists> <player> &7- Add player to multiple whitelists");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_REMOVE_MULTIPLE, "&e/whitelist removemultiple <whitelists> <player> &7- Remove player from multiple whitelists");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_PLAYERS, "&e/whitelist players <whitelist> &7- List players in a whitelist");
        translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_PLAYER, "&e/whitelist player <player> &7- Show whitelists for a player");
    }

    private void registerClsCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        translationBuilder.add(TranslationKeys.Commands.Cls.ERROR_NO_PERMISSION, "&cYou don't have permission to clear the chat.");
        translationBuilder.add(TranslationKeys.Commands.Cls.ERROR_PLAYERS_ONLY, "&cThis command can only be used by players.");
        translationBuilder.add(TranslationKeys.Commands.Cls.SUCCESS_CLEARED, "&aChat cleared successfully.");
    }

    private void registerBroadcastMessages(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        translationBuilder.add(TranslationKeys.Broadcast.ANNOUNCEMENT_PREFIX, "&6&l📢 &f");
        translationBuilder.add(TranslationKeys.Broadcast.ALERT_PREFIX, "&c&l🚨 &f");
        translationBuilder.add(TranslationKeys.Broadcast.INFO_PREFIX, "&b&lℹ &f");
        translationBuilder.add(TranslationKeys.Broadcast.RAW_PREFIX, "");
        translationBuilder.add(TranslationKeys.Broadcast.TO_PERMISSION_PREFIX, "&d&l📢 &f");
    }
}