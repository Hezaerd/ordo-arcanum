package com.hezaerd.accio.datagen;

import com.hezaerd.accio.ModLib;
import com.hezaerd.accio.datagen.lang.AccioEnglishLangProvider;
import com.hezaerd.accio.datagen.lang.AccioFrenchLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class AccioDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        
        registerLangs(pack);
        
        ModLib.LOGGER.info("testing Accio data generator");
    }
    
    private void registerLangs(FabricDataGenerator.Pack pack) {
        pack.addProvider(AccioEnglishLangProvider::new);
        pack.addProvider(AccioFrenchLangProvider::new);
    }
}