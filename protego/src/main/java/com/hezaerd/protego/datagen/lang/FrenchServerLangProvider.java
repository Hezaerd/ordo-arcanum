package com.hezaerd.protego.datagen.lang;

import com.hezaerd.lumos.datagen.provider.ServerLanguageProvider;
import com.hezaerd.protego.text.TranslationKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class FrenchServerLangProvider extends ServerLanguageProvider {
	public FrenchServerLangProvider(FabricDataOutput dataOutput) {
		super(dataOutput, "fr_fr");
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
		translationBuilder.add(TranslationKeys.Stats.BROADCASTS_SENT, "Nombre de diffusions envoyées");
		translationBuilder.add(TranslationKeys.Stats.GAMEMODE_CHANGES, "Nombre de changements de mode de jeu");
		translationBuilder.add(TranslationKeys.Stats.WHITELISTS_CREATED, "Nombre de listes blanches créées");
		translationBuilder.add(TranslationKeys.Stats.WHITELISTS_DELETED, "Nombre de listes blanches supprimées");
	}

	private void registerBroadcastCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		translationBuilder.add(TranslationKeys.Commands.Broadcast.ERROR_NO_PERMISSION, "&cVous n'avez pas la permission d'utiliser cette commande.");
		translationBuilder.add(TranslationKeys.Commands.Broadcast.ERROR_PLAYERS_ONLY, "&cCette commande ne peut être utilisée que par les joueurs.");
		translationBuilder.add(TranslationKeys.Commands.Broadcast.SUCCESS_ANNOUNCEMENT, "&aAnnonce diffusée avec succès.");
		translationBuilder.add(TranslationKeys.Commands.Broadcast.SUCCESS_ALERT, "&aAlerte diffusée avec succès.");
		translationBuilder.add(TranslationKeys.Commands.Broadcast.SUCCESS_INFO, "&aInformation diffusée avec succès.");
		translationBuilder.add(TranslationKeys.Commands.Broadcast.SUCCESS_RAW, "&aDiffusion brute envoyée avec succès.");
		translationBuilder.add(TranslationKeys.Commands.Broadcast.SUCCESS_TO_PERMISSION, "&aDiffusion basée sur les permissions envoyée avec succès.");
	}

	private void registerGamemodeCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		translationBuilder.add(TranslationKeys.Commands.Gamemode.ERROR_PLAYERS_ONLY, "&cCette commande ne peut être utilisée que par les joueurs.");
		translationBuilder.add(TranslationKeys.Commands.Gamemode.ERROR_NO_PERMISSION, "&cVous n'avez pas la permission de changer le mode de jeu.");
		translationBuilder.add(TranslationKeys.Commands.Gamemode.ERROR_INVALID_GAMEMODE, "&cMode de jeu invalide. Utilisez: 0 (survie), 1 (créatif), 2 (aventure), 3 (spectateur)");
		translationBuilder.add(TranslationKeys.Commands.Gamemode.ERROR_TARGET_OFFLINE, "&cLe joueur cible n'est pas en ligne.");
		translationBuilder.add(TranslationKeys.Commands.Gamemode.SUCCESS_CHANGED_SELF, "&aMode de jeu changé vers %s");
		translationBuilder.add(TranslationKeys.Commands.Gamemode.SUCCESS_CHANGED_TARGET, "&aMode de jeu de %s changé vers %s");
		translationBuilder.add(TranslationKeys.Commands.Gamemode.SUCCESS_TARGET_NOTIFIED, "&aVotre mode de jeu a été changé vers %s par %s");
		translationBuilder.add(TranslationKeys.Commands.Gamemode.GAMEMODE_SURVIVAL, "Survie");
		translationBuilder.add(TranslationKeys.Commands.Gamemode.GAMEMODE_CREATIVE, "Créatif");
		translationBuilder.add(TranslationKeys.Commands.Gamemode.GAMEMODE_ADVENTURE, "Aventure");
		translationBuilder.add(TranslationKeys.Commands.Gamemode.GAMEMODE_SPECTATOR, "Spectateur");
	}

	private void registerWhitelistCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		// Error messages
		translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_PLAYERS_ONLY, "&cCette commande ne peut être utilisée que par les joueurs.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_NO_PERMISSION, "&cVous n'avez pas la permission d'utiliser cette commande.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_WHITELIST_EXISTS, "&cUne liste blanche avec ce nom existe déjà.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_WHITELIST_NOT_FOUND, "&cListe blanche '%s' introuvable.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_WHITELIST_ALREADY_ACTIVE, "&cLa liste blanche '%s' est déjà active.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_WHITELIST_ALREADY_INACTIVE, "&cLa liste blanche '%s' est déjà inactive.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_PLAYER_ALREADY_IN_WHITELIST, "&cLe joueur %s est déjà dans la liste blanche '%s'.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_PLAYER_NOT_IN_WHITELIST, "&cLe joueur %s n'est pas dans la liste blanche '%s'.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.ERROR_INVALID_WHITELIST_NAMES, "&cNoms de listes blanches invalides: %s");

		// Success messages
		translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_CREATED, "&aListe blanche '%s' créée avec succès.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_DELETED, "&aListe blanche '%s' supprimée avec succès.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_ACTIVATED, "&aListe blanche '%s' activée avec succès.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_DEACTIVATED, "&aListe blanche '%s' désactivée avec succès.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_ADDED_PLAYER, "&aAjouté %s à la liste blanche '%s'.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_REMOVED_PLAYER, "&aRetiré %s de la liste blanche '%s'.");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_ADDED_MULTIPLE, "&aAjouté %s aux listes blanches: %s");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.SUCCESS_REMOVED_MULTIPLE, "&aRetiré %s des listes blanches: %s");

		// List messages
		translationBuilder.add(TranslationKeys.Commands.Whitelist.LIST_HEADER, "&6&l📋 &fListes Blanches Disponibles:");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.LIST_ENTRY, "&e• %s &7(%s) &f- %s");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.LIST_FOOTER, "&7Total: %d listes blanches");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.LIST_EMPTY, "&7Aucune liste blanche trouvée.");

		// Player list messages
		translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYERS_HEADER, "&6&l👥 &fJoueurs dans la liste blanche '%s':");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYERS_ENTRY, "&e• %s");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYERS_FOOTER, "&7Total: %d joueurs");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYERS_EMPTY, "&7Aucun joueur dans cette liste blanche.");

		// Player whitelists messages
		translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYER_WHITELISTS_HEADER, "&6&l📋 &fListes blanches pour le joueur %s:");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYER_WHITELISTS_ENTRY, "&e• %s &7(%s)");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYER_WHITELISTS_FOOTER, "&7Total: %d listes blanches");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.PLAYER_WHITELISTS_EMPTY, "&7Le joueur n'est dans aucune liste blanche.");

		// Help messages
		translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_TITLE, "&6&l📋 &fCommandes de Liste Blanche:");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_CREATE, "&e/whitelist create <nom> <nom_affiché> [description] &7- Créer une nouvelle liste blanche");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_DELETE, "&e/whitelist delete <nom> &7- Supprimer une liste blanche");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_LIST, "&e/whitelist list &7- Lister toutes les listes blanches");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_ACTIVATE, "&e/whitelist activate <nom> &7- Activer une liste blanche");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_DEACTIVATE, "&e/whitelist deactivate <nom> &7- Désactiver une liste blanche");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_ADD, "&e/whitelist add <liste> <joueur> &7- Ajouter un joueur à une liste blanche");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_REMOVE, "&e/whitelist remove <liste> <joueur> &7- Retirer un joueur d'une liste blanche");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_ADD_MULTIPLE, "&e/whitelist addmultiple <listes> <joueur> &7- Ajouter un joueur à plusieurs listes blanches");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_REMOVE_MULTIPLE, "&e/whitelist removemultiple <listes> <joueur> &7- Retirer un joueur de plusieurs listes blanches");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_PLAYERS, "&e/whitelist players <liste> &7- Lister les joueurs dans une liste blanche");
		translationBuilder.add(TranslationKeys.Commands.Whitelist.HELP_PLAYER, "&e/whitelist player <joueur> &7- Afficher les listes blanches pour un joueur");
	}

	private void registerClsCommands(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		translationBuilder.add(TranslationKeys.Commands.Cls.ERROR_NO_PERMISSION, "&cVous n'avez pas la permission d'effacer le chat.");
		translationBuilder.add(TranslationKeys.Commands.Cls.ERROR_PLAYERS_ONLY, "&cCette commande ne peut être utilisée que par les joueurs.");
		translationBuilder.add(TranslationKeys.Commands.Cls.SUCCESS_CLEARED, "&aChat effacé avec succès.");
	}

	private void registerBroadcastMessages(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		translationBuilder.add(TranslationKeys.Broadcast.ANNOUNCEMENT_PREFIX, "&6&l📢 &f");
		translationBuilder.add(TranslationKeys.Broadcast.ALERT_PREFIX, "&c&l🚨 &f");
		translationBuilder.add(TranslationKeys.Broadcast.INFO_PREFIX, "&b&lℹ &f");
		translationBuilder.add(TranslationKeys.Broadcast.RAW_PREFIX, "");
		translationBuilder.add(TranslationKeys.Broadcast.TO_PERMISSION_PREFIX, "&d&l📢 &f");
	}
}