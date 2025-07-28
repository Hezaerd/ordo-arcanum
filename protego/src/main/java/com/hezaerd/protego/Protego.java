package com.hezaerd.protego;

import net.fabricmc.api.ModInitializer;

public class Protego implements ModInitializer {
	@Override
	public void onInitialize() {
		try {
			ModLib.LOGGER.info("{} mod initialized successfully", ModLib.MOD_NAME);
		}
		catch (Exception e) {
			ModLib.LOGGER.error("Failed to initialize {} mod: {}", ModLib.MOD_NAME, e.getMessage(), e);
		}
	}
}