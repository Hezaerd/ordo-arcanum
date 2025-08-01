package com.hezaerd.lumos;

import com.hezaerd.lumos.database.SharedDatabase;
import net.fabricmc.api.ModInitializer;

public class Lumos implements ModInitializer {
	@Override
	public void onInitialize() {
		try {
			SharedDatabase.initialize();
			
			ModLib.LOGGER.info("{} mod initialized successfully", ModLib.MOD_NAME);
		}
		catch (Exception e) {
			ModLib.LOGGER.error("Failed to initialize {} mod: {}", ModLib.MOD_NAME, e.getMessage(), e);
		}
	}
}