package com.hezaerd.accio;

import com.hezaerd.accio.commands.AccioCommandManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Accio implements ModInitializer {
	@Override
	public void onInitialize() {
		try {
			CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
				AccioCommandManager.registerCommands(dispatcher);
			});

			ModLib.LOGGER.info("{} mod initialized successfully", ModLib.MOD_NAME);
		}
		catch (Exception e) {
			ModLib.LOGGER.error("Failed to initialize {} mod: {}", ModLib.MOD_NAME, e.getMessage(), e);
		}
	}
}