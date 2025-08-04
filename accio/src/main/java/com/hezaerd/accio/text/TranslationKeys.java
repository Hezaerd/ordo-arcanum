package com.hezaerd.accio.text;

/**
 * Centralized translation keys for the Accio mod.
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
		 * Dice command translation keys
		 */
		public static final class Dice {
			private Dice() {}

			// Help messages
			public static final String HELP_TITLE = "command.accio.dice.help.title";
			public static final String HELP_CHALLENGE = "command.accio.dice.help.challenge";
			public static final String HELP_CHALLENGE_DICE = "command.accio.dice.help.challenge_dice";
			public static final String HELP_ACCEPT = "command.accio.dice.help.accept";
			public static final String HELP_STATS = "command.accio.dice.help.stats";
			public static final String HELP_TIMEOUT = "command.accio.dice.help.timeout";

			// Error messages
			public static final String ERROR_PLAYERS_ONLY = "command.accio.dice.error.players_only";
			public static final String ERROR_PLAYER_OFFLINE = "command.accio.dice.error.player_offline";
			public static final String ERROR_SELF_CHALLENGE = "command.accio.dice.error.self_challenge";
			public static final String ERROR_ON_COOLDOWN = "command.accio.dice.error.on_cooldown";
			public static final String ERROR_TARGET_ON_COOLDOWN = "command.accio.dice.error.target_on_cooldown";
			public static final String ERROR_PENDING_DUEL = "command.accio.dice.error.pending_duel";
			public static final String ERROR_NO_CHALLENGE = "command.accio.dice.error.no_challenge";
			public static final String ERROR_CHALLENGE_EXPIRED = "command.accio.dice.error.challenge_expired";
			public static final String ERROR_CHALLENGER_OFFLINE = "command.accio.dice.error.challenger_offline";
			public static final String ERROR_INVALID_DICE = "command.accio.dice.error.invalid_dice";

			// Challenge messages
			public static final String CHALLENGE_SENT = "command.accio.dice.challenge.sent";
			public static final String CHALLENGE_SENT_WITH_DICE = "command.accio.dice.challenge.sent_with_dice";
			public static final String CHALLENGE_RECEIVED = "command.accio.dice.challenge.received";
			public static final String CHALLENGE_RECEIVED_WITH_DICE = "command.accio.dice.challenge.received_with_dice";

			// Duel messages
			public static final String DUEL_WIN = "command.accio.dice.duel.win";
			public static final String DUEL_TIE = "command.accio.dice.duel.tie";
			public static final String DUEL_ROLL = "command.accio.dice.duel.roll";
			public static final String DUEL_EPIC_VICTORY = "command.accio.dice.duel.epic_victory";
			public static final String DUEL_DRAMATIC_TIE = "command.accio.dice.duel.dramatic_tie";

			// Stats messages
			public static final String STATS_HEADER = "command.accio.dice.stats.header";
			public static final String STATS_TITLE = "command.accio.dice.stats.title";
			public static final String STATS_SEPARATOR = "command.accio.dice.stats.separator";
			public static final String STATS_TOTAL = "command.accio.dice.stats.total";
			public static final String STATS_WINS = "command.accio.dice.stats.wins";
			public static final String STATS_LOSSES = "command.accio.dice.stats.losses";
			public static final String STATS_TIES = "command.accio.dice.stats.ties";
			public static final String STATS_WINRATE = "command.accio.dice.stats.winrate";
			public static final String STATS_FOOTER = "command.accio.dice.stats.footer";
		}

		/**
		 * Feur command translation keys
		 */
		public static final class Feur {
			private Feur() {}

			// Error messages
			public static final String ERROR_PLAYERS_ONLY = "command.accio.feur.error.players_only";
			public static final String ERROR_NO_TRIGGER = "command.accio.feur.error.no_trigger";

			// Success messages
			public static final String SUCCESS_KICKED_TRIGGER = "command.accio.feur.success.kicked_trigger";
			public static final String SUCCESS_KICKED_SELF = "command.accio.feur.success.kicked_self";

			// Kick messages
			public static final String KICK_MESSAGE = "command.accio.feur.kick.message";
		}
	}

	/**
	 * Stats-related translation keys
	 */
	public static final class Stats {
		private Stats() {}

		public static final String FAILED_FEUR = "stat.accio.failed_feur";
		public static final String SUCCESS_FEUR = "stat.accio.success_feur";
		public static final String DICE_DUELS_WON = "stat.accio.dice_duels_won";
		public static final String DICE_DUELS_LOST = "stat.accio.dice_duels_lost";
		public static final String DICE_DUELS_TIED = "stat.accio.dice_duels_tied";
	}
}