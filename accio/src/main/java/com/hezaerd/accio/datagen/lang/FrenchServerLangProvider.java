package com.hezaerd.accio.datagen.lang;

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
        registerDiceCommands(translationBuilder);
    }

    private void registerStats(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        translationBuilder.add("stat.accio.failed_feur", "Nombre de /feur ratés");
        translationBuilder.add("stat.accio.success_feur", "Nombre de /feur réussis");

        translationBuilder.add("stat.accio.dice_duels_won", "Nombre de duels de dés gagnés");
        translationBuilder.add("stat.accio.dice_duels_lost", "Nombre de duels de dés perdus");
        translationBuilder.add("stat.accio.dice_duels_tied", "Nombre de duels de dés nuls");
    }

        private void registerDiceCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        // Dice command help
        translationBuilder.add("command.accio.dice.help.title", "&6&lCommandes de Duel de Dés:");
        translationBuilder.add("command.accio.dice.help.challenge", "&e/dice duel <joueur> &7- Défier un joueur à un duel de dés");
        translationBuilder.add("command.accio.dice.help.challenge_dice", "&e/dice duel <joueur> <dés> &7- Défier avec des dés spécifiques (d4, d6, d8, d12, d20, d100)");
        translationBuilder.add("command.accio.dice.help.accept", "&e/dice accept &7- Accepter un défi de duel en attente");
        translationBuilder.add("command.accio.dice.help.stats", "&e/dice stats [joueur] &7- Afficher les statistiques de duel");
        translationBuilder.add("command.accio.dice.help.timeout", "&7Les défis expirent après 30 secondes. Les joueurs ont un délai de 60 secondes entre les duels.");

        // Dice command error messages
        translationBuilder.add("command.accio.dice.error.players_only", "&cCette commande ne peut être utilisée que par les joueurs.");
        translationBuilder.add("command.accio.dice.error.player_offline", "&cLe joueur '&f%s&c' n'est pas en ligne.");
        translationBuilder.add("command.accio.dice.error.self_challenge", "&cVous ne pouvez pas vous défier vous-même à un duel de dés!");
        translationBuilder.add("command.accio.dice.error.on_cooldown", "&cVous devez attendre &f%d&c secondes de plus avant de lancer un autre défi!");
        translationBuilder.add("command.accio.dice.error.target_on_cooldown", "&c%s est en délai d'attente et ne peut pas être défié pendant &f%d&c secondes de plus!");
        translationBuilder.add("command.accio.dice.error.pending_duel", "&cVous avez déjà un défi de duel en attente!");
        translationBuilder.add("command.accio.dice.error.no_challenge", "&cVous n'avez pas de défi de duel en attente.");
        translationBuilder.add("command.accio.dice.error.challenge_expired", "&cLe défi de duel a expiré!");
                translationBuilder.add("command.accio.dice.error.challenger_offline", "&cLe challenger n'est plus en ligne.");
        translationBuilder.add("command.accio.dice.error.invalid_dice", "&cType de dés invalide: &f%s&c. Disponible: d4, d6, d8, d12, d20, d100");

        // Dice duel messages
        translationBuilder.add("command.accio.dice.challenge.sent", "&aDéfi de duel envoyé à &f%s&a! &7(30s de délai)");
        translationBuilder.add("command.accio.dice.challenge.sent_with_dice", "&aDéfi de duel envoyé à &f%s&a avec &e%s&a! &7(30s de délai)");
        translationBuilder.add("command.accio.dice.challenge.received", "&f%s &eveut vous confronter à un duel de dés, tapez &a/dice accept &epour accepter le duel. &7(30s de délai)");
        translationBuilder.add("command.accio.dice.challenge.received_with_dice", "&f%s &eveut vous confronter à un duel &e%s&e, tapez &a/dice accept &epour accepter le duel. &7(30s de délai)");
        translationBuilder.add("command.accio.dice.duel.win", "&f%s &6gagne avec un &e%d &7contre &e%d&6!");
        translationBuilder.add("command.accio.dice.duel.tie", "&6C'est un match nul! Les deux ont fait &e%d&6!");
        translationBuilder.add("command.accio.dice.duel.roll", "&bVous avez fait: &e%d");
        translationBuilder.add("command.accio.dice.duel.epic_victory", "&d&lÉPIQUE! &f%s &da obtenu un 6 parfait tandis que &f%s &da obtenu un 1! Quelle victoire écrasante!");
        translationBuilder.add("command.accio.dice.duel.dramatic_tie", "&6&lLes deux joueurs ont fait le même score! C'est un match nul dramatique!");

        // Stats display messages
        translationBuilder.add("command.accio.dice.stats.header", "&6&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        translationBuilder.add("command.accio.dice.stats.title", "&6&l🎲 &fStatistiques de Duel de %s &l🎲");
        translationBuilder.add("command.accio.dice.stats.separator", "&7&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        translationBuilder.add("command.accio.dice.stats.total", "&e📊 Total des Duels: &f%d");
        translationBuilder.add("command.accio.dice.stats.wins", "&a✅ Victoires: &f%d");
        translationBuilder.add("command.accio.dice.stats.losses", "&c❌ Défaites: &f%d");
        translationBuilder.add("command.accio.dice.stats.ties", "&6🤝 Nuls: &f%d");
        translationBuilder.add("command.accio.dice.stats.winrate", "&b📈 Taux de Victoire: &f%.1f%%");
        translationBuilder.add("command.accio.dice.stats.footer", "&7&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
}
