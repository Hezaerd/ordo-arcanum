package com.hezaerd.accio.datagen.lang;

import com.hezaerd.accio.text.TranslationKeys;
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
		registerFeurCommands(translationBuilder);
		registerDiceCommands(translationBuilder);
	}

	private void registerStats(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		// Feur stats
		translationBuilder.add(TranslationKeys.Stats.FAILED_FEUR, "Amount of failed /feur");
		translationBuilder.add(TranslationKeys.Stats.SUCCESS_FEUR, "Amount of successful /feur");

		// Dice stats
		translationBuilder.add(TranslationKeys.Stats.DICE_DUELS_WON, "Amount of dice duels won");
		translationBuilder.add(TranslationKeys.Stats.DICE_DUELS_LOST, "Amount of dice duels lost");
		translationBuilder.add(TranslationKeys.Stats.DICE_DUELS_TIED, "Amount of dice duels tied");
	}

	private void registerFeurCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		translationBuilder.add(TranslationKeys.Commands.Feur.ERROR_PLAYERS_ONLY, "&cThis command can only be used by players.");
		translationBuilder.add(TranslationKeys.Commands.Feur.ERROR_NO_TRIGGER, "&cNo one said a trigger word recently. You have been kicked for Feur!");
		translationBuilder.add(TranslationKeys.Commands.Feur.SUCCESS_KICKED_TRIGGER, "&aSuccessfully kicked &f%s &afor Feur! (said '%s')");
		translationBuilder.add(TranslationKeys.Commands.Feur.KICK_MESSAGE, "&cYou have been kicked for: %s");
	}

	private void registerDiceCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		// Dice command help
		translationBuilder.add(TranslationKeys.Commands.Dice.HELP_TITLE, "&6&lDice Duel Commands:");
		translationBuilder.add(TranslationKeys.Commands.Dice.HELP_CHALLENGE, "&e/dice duel <player> &7- Challenge a player to a dice duel");
		translationBuilder.add(TranslationKeys.Commands.Dice.HELP_CHALLENGE_DICE, "&e/dice duel <player> <dice> &7- Challenge with specific dice (d4, d6, d8, d12, d20, d100)");
		translationBuilder.add(TranslationKeys.Commands.Dice.HELP_ACCEPT, "&e/dice accept &7- Accept a pending duel challenge");
		translationBuilder.add(TranslationKeys.Commands.Dice.HELP_STATS, "&e/dice stats [player] &7- Show duel statistics");
		translationBuilder.add(TranslationKeys.Commands.Dice.HELP_TIMEOUT, "&7Challenges expire after 30 seconds. Players have a 60-second cooldown between duels.");

		// Dice command error messages
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_PLAYERS_ONLY, "&cThis command can only be used by players.");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_PLAYER_OFFLINE, "&cPlayer '&f%s&c' is not online.");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_SELF_CHALLENGE, "&cYou cannot challenge yourself to a dice duel!");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_ON_COOLDOWN, "&cYou must wait &f%d&c more seconds before challenging another duel!");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_TARGET_ON_COOLDOWN, "&c%s is on cooldown and cannot be challenged for &f%d&c more seconds!");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_PENDING_DUEL, "&cYou already have a pending duel challenge!");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_NO_CHALLENGE, "&cYou don't have any pending duel challenges.");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_CHALLENGE_EXPIRED, "&cThe duel challenge has expired!");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_CHALLENGER_OFFLINE, "&cThe challenger is no longer online.");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_INVALID_DICE, "&cInvalid dice type: &f%s&c. Available: d4, d6, d8, d12, d20, d100");

		// Dice duel messages
		translationBuilder.add(TranslationKeys.Commands.Dice.CHALLENGE_SENT, "&aDuel challenge sent to &f%s&a! &7(30s timeout)");
		translationBuilder.add(TranslationKeys.Commands.Dice.CHALLENGE_SENT_WITH_DICE, "&aDuel challenge sent to &f%s&a with &e%s&a! &7(30s timeout)");
		translationBuilder.add(TranslationKeys.Commands.Dice.CHALLENGE_RECEIVED, "&f%s &ewants to confront you to a dice duel, type &a/dice accept &eto accept the duel. &7(30s timeout)");
		translationBuilder.add(TranslationKeys.Commands.Dice.CHALLENGE_RECEIVED_WITH_DICE, "&f%s &ewants to confront you to a &e%s&e duel, type &a/dice accept &eto accept the duel. &7(30s timeout)");
		translationBuilder.add(TranslationKeys.Commands.Dice.DUEL_WIN, "&f%s &6wins with a &e%d &7vs &e%d&6!");
		translationBuilder.add(TranslationKeys.Commands.Dice.DUEL_TIE, "&6It's a tie! Both rolled &e%d&6!");
		translationBuilder.add(TranslationKeys.Commands.Dice.DUEL_ROLL, "&bYou rolled: &e%d");
		translationBuilder.add(TranslationKeys.Commands.Dice.DUEL_EPIC_VICTORY, "&d&lEPIC! &f%s &dgot a perfect 6 while &f%s &dgot a 1! What a crushing victory!");
		translationBuilder.add(TranslationKeys.Commands.Dice.DUEL_DRAMATIC_TIE, "&6&lBoth players rolled the same! It's a dramatic tie!");

		// Stats display messages
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_HEADER, "&6&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_TITLE, "&6&l🎲 &f%s's &6Duel Statistics &l🎲");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_SEPARATOR, "&7&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_TOTAL, "&e📊 Total Duels: &f%d");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_WINS, "&a✅ Wins: &f%d");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_LOSSES, "&c❌ Losses: &f%d");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_TIES, "&6🤝 Ties: &f%d");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_WINRATE, "&b📈 Win Rate: &f%.1f%%");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_FOOTER, "&7&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
	}
}
