package com.hezaerd.protego.datagen;

import com.hezaerd.protego.ModLib;
import com.hezaerd.protego.datagen.lang.EnglishServerLangProvider;
import com.hezaerd.protego.datagen.lang.FrenchServerLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ProtegoDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        registerLangs(pack);

        ModLib.LOGGER.info("Protego data generator initialized");
    }

    private void registerLangs(FabricDataGenerator.Pack pack) {
        pack.addProvider(EnglishServerLangProvider::new);
        pack.addProvider(FrenchServerLangProvider::new);
    }
}