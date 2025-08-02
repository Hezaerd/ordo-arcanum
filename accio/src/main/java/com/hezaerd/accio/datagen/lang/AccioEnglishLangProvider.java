package com.hezaerd.accio.datagen.lang;

import com.hezaerd.lumos.datagen.provider.ServerLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class AccioEnglishLangProvider extends ServerLanguageProvider {
    public AccioEnglishLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        registerStats(translationBuilder);
    }
    
    private void registerStats(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        translationBuilder.add("stat.minecraft.failed_feur", "Amount of failed /feur");
        translationBuilder.add("stat.minecraft.success_feur", "Amount of successful /feur");
        
        translationBuilder.add("stat.minecraft.dice_duels_won", "Amount of dice duels won");
        translationBuilder.add("stat.minecraft.dice_duels_lost", "Amount of dice duels lost");
        translationBuilder.add("stat.minecraft.dice_duels_tied", "Amount of dice duels tied");
    }
}
