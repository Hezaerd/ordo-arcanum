package com.hezaerd.accio.datagen.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class AccioEnglishLangProvider extends FabricLanguageProvider {
    public AccioEnglishLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        registerStats(translationBuilder);
    }
    
    private void registerStats(TranslationBuilder translationBuilder) {
        translationBuilder.add("stat.accio.failed_feur", "Amount of failed /feur");
        translationBuilder.add("stat.accio.success_feur", "Amount of successful /feur");
    }
}
