package com.hezaerd.protego.permissions;

import com.hezaerd.lumos.compat.luckperms.LuckPermsCompat;
import com.hezaerd.lumos.compat.luckperms.LuckPermsProvider;
import com.hezaerd.lumos.compat.luckperms.LuckPermsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.server.network.ServerPlayerEntity;

public final class PermissionManager {
	private static final Logger LOGGER = LoggerFactory.getLogger("protego-permissions");
	private static LuckPermsCompat luckPermsCompat;

	private PermissionManager() {}

	/**
	 * Initialize the permission manager
	 */
	public static void initialize() {
		luckPermsCompat = LuckPermsProvider.get();
		LOGGER.info("Permission manager initialized with {}",
			luckPermsCompat.isAvailable() ? "LuckPerms" : "fallback system");
	}

	/**
	 * Check if a player has a specific permission
	 * @param player The player to check
	 * @param permission The permission node to check
	 * @return true if the player has the permission
	 */
	public static boolean hasPermission(ServerPlayerEntity player, String permission) {
		if (luckPermsCompat == null) {
			initialize();
		}
		return luckPermsCompat.hasPermission(player, permission);
	}

	/**
	 * Check if a player has any of the specified permissions
	 * @param player The player to check
	 * @param permissions Array of permission nodes to check
	 * @return true if the player has any of the permissions
	 */
	public static boolean hasAnyPermission(ServerPlayerEntity player, String... permissions) {
		return LuckPermsUtils.hasAnyPermission(player, permissions);
	}

	/**
	 * Check if a player has all the specified permissions
	 * @param player The player to check
	 * @param permissions Array of permission nodes to check
	 * @return true if the player has all permissions
	 */
	public static boolean hasAllPermissions(ServerPlayerEntity player, String... permissions) {
		return LuckPermsUtils.hasAllPermissions(player, permissions);
	}

	/**
	 * Get the permission level of a player (0-4, where 4 is operator)
	 * @param player The player to check
	 * @return The permission level
	 */
	public static int getPermissionLevel(ServerPlayerEntity player) {
		if (luckPermsCompat == null) {
			initialize();
		}
		return luckPermsCompat.getPermissionLevel(player);
	}

	/**
	 * Check if LuckPerms is available
	 * @return true if LuckPerms is loaded and available
	 */
	public static boolean isLuckPermsAvailable() {
		if (luckPermsCompat == null) {
			initialize();
		}
		return luckPermsCompat.isAvailable();
	}
}
