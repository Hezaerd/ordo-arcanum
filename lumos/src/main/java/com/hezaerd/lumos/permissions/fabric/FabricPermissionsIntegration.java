package com.hezaerd.lumos.permissions.fabric;

import java.util.function.Predicate;

import com.hezaerd.lumos.ModLib;
import me.lucko.fabric.api.permissions.v0.Permissions;

import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;

/**
 * Fabric Permissions API integration - only loaded when Fabric Permissions API is installed
 */
public final class FabricPermissionsIntegration {

	private FabricPermissionsIntegration() {}

	/**
	 * Initialize Fabric Permissions API integration
	 */
	public static void initialize() {
		ModLib.LOGGER.info("Fabric Permissions API integration initialized successfully");
	}

	/**
	 * Check if Fabric Permissions API is available
	 */
	public static boolean isAvailable() {
		return true;
	}

	/**
	 * Require permission with boolean default
	 */
	public static Predicate<ServerCommandSource> require(String permission, boolean defaultValue) {
		return Permissions.require(permission, defaultValue);
	}

	/**
	 * Require permission with level
	 */
	public static Predicate<ServerCommandSource> require(String permission, int level) {
		return Permissions.require(permission, level);
	}

	/**
	 * Check permission for ServerCommandSource with boolean fallback
	 */
	public static boolean check(ServerCommandSource source, String permission, boolean fallback) {
		return Permissions.check(source, permission, fallback);
	}

	/**
	 * Check permission for ServerCommandSource with level fallback
	 */
	public static boolean check(ServerCommandSource source, String permission, int level) {
		return Permissions.check(source, permission, level);
	}

	/**
	 * Check permission for Entity with boolean fallback
	 */
	public static boolean check(Entity entity, String permission, boolean fallback) {
		return Permissions.check(entity, permission, fallback);
	}

	/**
	 * Check permission for Entity with level fallback
	 */
	public static boolean check(Entity entity, String permission, int level) {
		return Permissions.check(entity, permission, level);
	}
}