package com.hezaerd.protego.text;

/**
 * Centralized translation keys for the Protego mod.
 * This class contains all translation key constants to ensure type safety
 * and prevent typos when using translation keys throughout the mod.
 */
public final class TranslationKeys {
    private TranslationKeys() {}

    /**
     * Command-related translation keys
     */
    public static final class Commands {
        private Commands() {}

        /**
         * Broadcast command translation keys
         */
        public static final class Broadcast {
            private Broadcast() {}

            // Error messages
            public static final String ERROR_NO_PERMISSION = "command.protego.broadcast.error.no_permission";
            public static final String ERROR_PLAYERS_ONLY = "command.protego.broadcast.error.players_only";

            // Success messages
            public static final String SUCCESS_ANNOUNCEMENT = "command.protego.broadcast.success.announcement";
            public static final String SUCCESS_ALERT = "command.protego.broadcast.success.alert";
            public static final String SUCCESS_INFO = "command.protego.broadcast.success.info";
            public static final String SUCCESS_RAW = "command.protego.broadcast.success.raw";
            public static final String SUCCESS_TO_PERMISSION = "command.protego.broadcast.success.to_permission";
        }

        /**
         * Gamemode command translation keys
         */
        public static final class Gamemode {
            private Gamemode() {}

            // Error messages
            public static final String ERROR_PLAYERS_ONLY = "command.protego.gamemode.error.players_only";
            public static final String ERROR_NO_PERMISSION = "command.protego.gamemode.error.no_permission";
            public static final String ERROR_INVALID_GAMEMODE = "command.protego.gamemode.error.invalid_gamemode";
            public static final String ERROR_TARGET_OFFLINE = "command.protego.gamemode.error.target_offline";

            // Success messages
            public static final String SUCCESS_CHANGED_SELF = "command.protego.gamemode.success.changed_self";
            public static final String SUCCESS_CHANGED_TARGET = "command.protego.gamemode.success.changed_target";
            public static final String SUCCESS_TARGET_NOTIFIED = "command.protego.gamemode.success.target_notified";

            // Gamemode names
            public static final String GAMEMODE_SURVIVAL = "command.protego.gamemode.survival";
            public static final String GAMEMODE_CREATIVE = "command.protego.gamemode.creative";
            public static final String GAMEMODE_ADVENTURE = "command.protego.gamemode.adventure";
            public static final String GAMEMODE_SPECTATOR = "command.protego.gamemode.spectator";
        }

        /**
         * Whitelist command translation keys
         */
        public static final class Whitelist {
            private Whitelist() {}

            // Error messages
            public static final String ERROR_PLAYERS_ONLY = "command.protego.whitelist.error.players_only";
            public static final String ERROR_NO_PERMISSION = "command.protego.whitelist.error.no_permission";
            public static final String ERROR_WHITELIST_EXISTS = "command.protego.whitelist.error.exists";
            public static final String ERROR_WHITELIST_NOT_FOUND = "command.protego.whitelist.error.not_found";
            public static final String ERROR_WHITELIST_ALREADY_ACTIVE = "command.protego.whitelist.error.already_active";
            public static final String ERROR_WHITELIST_ALREADY_INACTIVE = "command.protego.whitelist.error.already_inactive";
            public static final String ERROR_PLAYER_ALREADY_IN_WHITELIST = "command.protego.whitelist.error.player_already_in";
            public static final String ERROR_PLAYER_NOT_IN_WHITELIST = "command.protego.whitelist.error.player_not_in";
            public static final String ERROR_INVALID_WHITELIST_NAMES = "command.protego.whitelist.error.invalid_names";

            // Success messages
            public static final String SUCCESS_CREATED = "command.protego.whitelist.success.created";
            public static final String SUCCESS_DELETED = "command.protego.whitelist.success.deleted";
            public static final String SUCCESS_ACTIVATED = "command.protego.whitelist.success.activated";
            public static final String SUCCESS_DEACTIVATED = "command.protego.whitelist.success.deactivated";
            public static final String SUCCESS_ADDED_PLAYER = "command.protego.whitelist.success.added_player";
            public static final String SUCCESS_REMOVED_PLAYER = "command.protego.whitelist.success.removed_player";
            public static final String SUCCESS_ADDED_MULTIPLE = "command.protego.whitelist.success.added_multiple";
            public static final String SUCCESS_REMOVED_MULTIPLE = "command.protego.whitelist.success.removed_multiple";

            // List messages
            public static final String LIST_HEADER = "command.protego.whitelist.list.header";
            public static final String LIST_ENTRY = "command.protego.whitelist.list.entry";
            public static final String LIST_FOOTER = "command.protego.whitelist.list.footer";
            public static final String LIST_EMPTY = "command.protego.whitelist.list.empty";

            // Player list messages
            public static final String PLAYERS_HEADER = "command.protego.whitelist.players.header";
            public static final String PLAYERS_ENTRY = "command.protego.whitelist.players.entry";
            public static final String PLAYERS_FOOTER = "command.protego.whitelist.players.footer";
            public static final String PLAYERS_EMPTY = "command.protego.whitelist.players.empty";

            // Player whitelists messages
            public static final String PLAYER_WHITELISTS_HEADER = "command.protego.whitelist.player_whitelists.header";
            public static final String PLAYER_WHITELISTS_ENTRY = "command.protego.whitelist.player_whitelists.entry";
            public static final String PLAYER_WHITELISTS_FOOTER = "command.protego.whitelist.player_whitelists.footer";
            public static final String PLAYER_WHITELISTS_EMPTY = "command.protego.whitelist.player_whitelists.empty";

            // Help messages
            public static final String HELP_TITLE = "command.protego.whitelist.help.title";
            public static final String HELP_CREATE = "command.protego.whitelist.help.create";
            public static final String HELP_DELETE = "command.protego.whitelist.help.delete";
            public static final String HELP_LIST = "command.protego.whitelist.help.list";
            public static final String HELP_ACTIVATE = "command.protego.whitelist.help.activate";
            public static final String HELP_DEACTIVATE = "command.protego.whitelist.help.deactivate";
            public static final String HELP_ADD = "command.protego.whitelist.help.add";
            public static final String HELP_REMOVE = "command.protego.whitelist.help.remove";
            public static final String HELP_ADD_MULTIPLE = "command.protego.whitelist.help.add_multiple";
            public static final String HELP_REMOVE_MULTIPLE = "command.protego.whitelist.help.remove_multiple";
            public static final String HELP_PLAYERS = "command.protego.whitelist.help.players";
            public static final String HELP_PLAYER = "command.protego.whitelist.help.player";
        }
    }

    /**
     * Broadcast-related translation keys
     */
    public static final class Broadcast {
        private Broadcast() {}

        public static final String ANNOUNCEMENT_PREFIX = "broadcast.protego.announcement.prefix";
        public static final String ALERT_PREFIX = "broadcast.protego.alert.prefix";
        public static final String INFO_PREFIX = "broadcast.protego.info.prefix";
        public static final String RAW_PREFIX = "broadcast.protego.raw.prefix";
        public static final String TO_PERMISSION_PREFIX = "broadcast.protego.to_permission.prefix";
    }

    /**
     * Stats-related translation keys
     */
    public static final class Stats {
        private Stats() {}

        public static final String BROADCASTS_SENT = "stat.protego.broadcasts_sent";
        public static final String GAMEMODE_CHANGES = "stat.protego.gamemode_changes";
        public static final String WHITELISTS_CREATED = "stat.protego.whitelists_created";
        public static final String WHITELISTS_DELETED = "stat.protego.whitelists_deleted";
    }
}