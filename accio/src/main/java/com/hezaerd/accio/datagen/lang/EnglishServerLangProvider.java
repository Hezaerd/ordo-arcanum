package com.hezaerd.accio.datagen.lang;

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
        registerDiceCommands(translationBuilder);
    }

    private void registerStats(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        translationBuilder.add("stat.accio.failed_feur", "Amount of failed /feur");
        translationBuilder.add("stat.accio.success_feur", "Amount of successful /feur");

        translationBuilder.add("stat.accio.dice_duels_won", "Amount of dice duels won");
        translationBuilder.add("stat.accio.dice_duels_lost", "Amount of dice duels lost");
        translationBuilder.add("stat.accio.dice_duels_tied", "Amount of dice duels tied");
    }

        private void registerDiceCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        // Dice command help
        translationBuilder.add("command.accio.dice.help.title", "&6&lDice Duel Commands:");
        translationBuilder.add("command.accio.dice.help.challenge", "&e/dice duel <player> &7- Challenge a player to a dice duel");
        translationBuilder.add("command.accio.dice.help.challenge_dice", "&e/dice duel <player> <dice> &7- Challenge with specific dice (d4, d6, d8, d12, d20, d100)");
        translationBuilder.add("command.accio.dice.help.accept", "&e/dice accept &7- Accept a pending duel challenge");
        translationBuilder.add("command.accio.dice.help.stats", "&e/dice stats [player] &7- Show duel statistics");
        translationBuilder.add("command.accio.dice.help.timeout", "&7Challenges expire after 30 seconds. Players have a 60-second cooldown between duels.");

        // Dice command error messages
        translationBuilder.add("command.accio.dice.error.players_only", "&cThis command can only be used by players.");
        translationBuilder.add("command.accio.dice.error.player_offline", "&cPlayer '&f%s&c' is not online.");
        translationBuilder.add("command.accio.dice.error.self_challenge", "&cYou cannot challenge yourself to a dice duel!");
        translationBuilder.add("command.accio.dice.error.on_cooldown", "&cYou must wait &f%d&c more seconds before challenging another duel!");
        translationBuilder.add("command.accio.dice.error.target_on_cooldown", "&c%s is on cooldown and cannot be challenged for &f%d&c more seconds!");
        translationBuilder.add("command.accio.dice.error.pending_duel", "&cYou already have a pending duel challenge!");
        translationBuilder.add("command.accio.dice.error.no_challenge", "&cYou don't have any pending duel challenges.");
        translationBuilder.add("command.accio.dice.error.challenge_expired", "&cThe duel challenge has expired!");
        translationBuilder.add("command.accio.dice.error.challenger_offline", "&cThe challenger is no longer online.");
        translationBuilder.add("command.accio.dice.error.invalid_dice", "&cInvalid dice type: &f%s&c. Available: d4, d6, d8, d12, d20, d100");

        // Dice duel messages
        translationBuilder.add("command.accio.dice.challenge.sent", "&aDuel challenge sent to &f%s&a! &7(30s timeout)");
        translationBuilder.add("command.accio.dice.challenge.sent_with_dice", "&aDuel challenge sent to &f%s&a with &e%s&a! &7(30s timeout)");
        translationBuilder.add("command.accio.dice.challenge.received", "&f%s &ewants to confront you to a dice duel, type &a/dice accept &eto accept the duel. &7(30s timeout)");
        translationBuilder.add("command.accio.dice.challenge.received_with_dice", "&f%s &ewants to confront you to a &e%s&e duel, type &a/dice accept &eto accept the duel. &7(30s timeout)");
        translationBuilder.add("command.accio.dice.duel.win", "&f%s &6wins with a &e%d &7vs &e%d&6!");
        translationBuilder.add("command.accio.dice.duel.tie", "&6It's a tie! Both rolled &e%d&6!");
        translationBuilder.add("command.accio.dice.duel.roll", "&bYou rolled: &e%d");
        translationBuilder.add("command.accio.dice.duel.epic_victory", "&d&lEPIC! &f%s &dgot a perfect 6 while &f%s &dgot a 1! What a crushing victory!");
        translationBuilder.add("command.accio.dice.duel.dramatic_tie", "&6&lBoth players rolled the same! It's a dramatic tie!");

        // Stats display messages
        translationBuilder.add("command.accio.dice.stats.header", "&6&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        translationBuilder.add("command.accio.dice.stats.title", "&6&l🎲 &f%s's &6Duel Statistics &l🎲");
        translationBuilder.add("command.accio.dice.stats.separator", "&7&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        translationBuilder.add("command.accio.dice.stats.total", "&e📊 Total Duels: &f%d");
        translationBuilder.add("command.accio.dice.stats.wins", "&a✅ Wins: &f%d");
        translationBuilder.add("command.accio.dice.stats.losses", "&c❌ Losses: &f%d");
        translationBuilder.add("command.accio.dice.stats.ties", "&6🤝 Ties: &f%d");
        translationBuilder.add("command.accio.dice.stats.winrate", "&b📈 Win Rate: &f%.1f%%");
        translationBuilder.add("command.accio.dice.stats.footer", "&7&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
}
