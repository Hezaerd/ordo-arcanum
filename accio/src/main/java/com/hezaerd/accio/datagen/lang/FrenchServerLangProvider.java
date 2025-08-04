package com.hezaerd.accio.datagen.lang;

import com.hezaerd.accio.text.TranslationKeys;
import com.hezaerd.lumos.datagen.provider.ServerLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class FrenchServerLangProvider extends ServerLanguageProvider {
	public FrenchServerLangProvider(FabricDataOutput dataOutput) {
		super(dataOutput, "fr_fr");
	}

	@Override
	public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		registerStats(translationBuilder);
		registerFeurCommands(translationBuilder);
		registerDiceCommands(translationBuilder);
	}

	private void registerStats(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		// Feur stats
		translationBuilder.add(TranslationKeys.Stats.FAILED_FEUR, "Nombre de /feur ratés");
		translationBuilder.add(TranslationKeys.Stats.SUCCESS_FEUR, "Nombre de /feur réussis");

		// Dice stats
		translationBuilder.add(TranslationKeys.Stats.DICE_DUELS_WON, "Nombre de duels de dés gagnés");
		translationBuilder.add(TranslationKeys.Stats.DICE_DUELS_LOST, "Nombre de duels de dés perdus");
		translationBuilder.add(TranslationKeys.Stats.DICE_DUELS_TIED, "Nombre de duels de dés nuls");
	}

	private void registerFeurCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		translationBuilder.add(TranslationKeys.Commands.Feur.ERROR_PLAYERS_ONLY, "&cCette commande ne peut être utilisée que par les joueurs.");
		translationBuilder.add(TranslationKeys.Commands.Feur.ERROR_NO_TRIGGER, "&cPersonne n'a dit de mot déclencheur récemment. Vous avez été expulsé pour Feur!");
		translationBuilder.add(TranslationKeys.Commands.Feur.SUCCESS_KICKED_TRIGGER, "&aExpulsion réussie de &f%s &apour Feur! (a dit '%s')");
		translationBuilder.add(TranslationKeys.Commands.Feur.KICK_MESSAGE, "&cVous avez été expulsé pour: %s");
	}

	private void registerDiceCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		// Dice command help
		translationBuilder.add(TranslationKeys.Commands.Dice.HELP_TITLE, "&6&lCommandes de Duel de Dés:");
		translationBuilder.add(TranslationKeys.Commands.Dice.HELP_CHALLENGE, "&e/dice duel <joueur> &7- Défier un joueur à un duel de dés");
		translationBuilder.add(TranslationKeys.Commands.Dice.HELP_CHALLENGE_DICE, "&e/dice duel <joueur> <dés> &7- Défier avec des dés spécifiques (d4, d6, d8, d12, d20, d100)");
		translationBuilder.add(TranslationKeys.Commands.Dice.HELP_ACCEPT, "&e/dice accept &7- Accepter un défi de duel en attente");
		translationBuilder.add(TranslationKeys.Commands.Dice.HELP_STATS, "&e/dice stats [joueur] &7- Afficher les statistiques de duel");
		translationBuilder.add(TranslationKeys.Commands.Dice.HELP_TIMEOUT, "&7Les défis expirent après 30 secondes. Les joueurs ont un délai de 60 secondes entre les duels.");

		// Dice command error messages
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_PLAYERS_ONLY, "&cCette commande ne peut être utilisée que par les joueurs.");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_PLAYER_OFFLINE, "&cLe joueur '&f%s&c' n'est pas en ligne.");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_SELF_CHALLENGE, "&cVous ne pouvez pas vous défier vous-même à un duel de dés!");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_ON_COOLDOWN, "&cVous devez attendre &f%d&c secondes de plus avant de lancer un autre défi!");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_TARGET_ON_COOLDOWN, "&c%s est en délai d'attente et ne peut pas être défié pendant &f%d&c secondes de plus!");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_PENDING_DUEL, "&cVous avez déjà un défi de duel en attente!");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_NO_CHALLENGE, "&cVous n'avez pas de défi de duel en attente.");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_CHALLENGE_EXPIRED, "&cLe défi de duel a expiré!");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_CHALLENGER_OFFLINE, "&cLe challenger n'est plus en ligne.");
		translationBuilder.add(TranslationKeys.Commands.Dice.ERROR_INVALID_DICE, "&cType de dés invalide: &f%s&c. Disponible: d4, d6, d8, d12, d20, d100");

		// Dice duel messages
		translationBuilder.add(TranslationKeys.Commands.Dice.CHALLENGE_SENT, "&aDéfi de duel envoyé à &f%s&a! &7(30s de délai)");
		translationBuilder.add(TranslationKeys.Commands.Dice.CHALLENGE_SENT_WITH_DICE, "&aDéfi de duel envoyé à &f%s&a avec &e%s&a! &7(30s de délai)");
		translationBuilder.add(TranslationKeys.Commands.Dice.CHALLENGE_RECEIVED, "&f%s &eveut vous confronter à un duel de dés, tapez &a/dice accept &epour accepter le duel. &7(30s de délai)");
		translationBuilder.add(TranslationKeys.Commands.Dice.CHALLENGE_RECEIVED_WITH_DICE, "&f%s &eveut vous confronter à un duel &e%s&e, tapez &a/dice accept &epour accepter le duel. &7(30s de délai)");
		translationBuilder.add(TranslationKeys.Commands.Dice.DUEL_WIN, "&f%s &6gagne avec un &e%d &7contre &e%d&6!");
		translationBuilder.add(TranslationKeys.Commands.Dice.DUEL_TIE, "&6C'est un match nul! Les deux ont fait &e%d&6!");
		translationBuilder.add(TranslationKeys.Commands.Dice.DUEL_ROLL, "&bVous avez fait: &e%d");
		translationBuilder.add(TranslationKeys.Commands.Dice.DUEL_EPIC_VICTORY, "&d&lÉPIQUE! &f%s &da obtenu un 6 parfait tandis que &f%s &da obtenu un 1! Quelle victoire écrasante!");
		translationBuilder.add(TranslationKeys.Commands.Dice.DUEL_DRAMATIC_TIE, "&6&lLes deux joueurs ont fait le même score! C'est un match nul dramatique!");

		// Stats display messages
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_HEADER, "&6&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_TITLE, "&6&l🎲 &fStatistiques de Duel de %s &l🎲");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_SEPARATOR, "&7&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_TOTAL, "&e📊 Total des Duels: &f%d");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_WINS, "&a✅ Victoires: &f%d");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_LOSSES, "&c❌ Défaites: &f%d");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_TIES, "&6🤝 Nuls: &f%d");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_WINRATE, "&b📈 Taux de Victoire: &f%.1f%%");
		translationBuilder.add(TranslationKeys.Commands.Dice.STATS_FOOTER, "&7&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
	}
}
