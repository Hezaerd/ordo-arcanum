package com.hezaerd.protego;

import com.hezaerd.protego.commands.ProtegoCommandManager;
import com.hezaerd.protego.managers.WhitelistManager;
import com.hezaerd.protego.permissions.PermissionManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Protego implements ModInitializer {
	@Override
	public void onInitialize() {
		try {
			PermissionManager.initialize();
			WhitelistManager.getInstance().initialize();

			CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
				ProtegoCommandManager.registerCommands(dispatcher);
			});

			ModLib.LOGGER.info("{} mod initialized successfully", ModLib.MOD_NAME);
		}
		catch (Exception e) {
			ModLib.LOGGER.error("Failed to initialize {} mod: {}", ModLib.MOD_NAME, e.getMessage(), e);
		}
	}
}