package com.hezaerd.accio.datagen.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class AccioFrenchLangProvider extends FabricLanguageProvider {
    public AccioFrenchLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "fr_fr");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        registerStats(translationBuilder);
    }

    private void registerStats(TranslationBuilder translationBuilder) {
        translationBuilder.add("stat.accio.failed_feur", "Nombre de /feur ratés");
        translationBuilder.add("stat.accio.sucess_feur", "Nombre de /feur réussis");
    }
}
