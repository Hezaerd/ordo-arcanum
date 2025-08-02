package com.hezaerd.accio.datagen.lang;

import com.hezaerd.lumos.datagen.provider.ServerLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class AccioFrenchLangProvider extends ServerLanguageProvider {
    public AccioFrenchLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "fr_fr");
    }

    @Override
    public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        registerStats(translationBuilder);
    }

    private void registerStats(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        translationBuilder.add("stat.minecraft.failed_feur", "Nombre de /feur ratés");
        translationBuilder.add("stat.minecraft.success_feur", "Nombre de /feur réussis");
        
        translationBuilder.add("stat.minecraft.dice_duels_won", "Nombre de duels de dés gagnés");
        translationBuilder.add("stat.minecraft.dice_duels_lost", "Nombre de duels de dés perdus");
        translationBuilder.add("stat.minecraft.dice_duels_tied", "Nombre de duels de dés nuls");
    }
}
