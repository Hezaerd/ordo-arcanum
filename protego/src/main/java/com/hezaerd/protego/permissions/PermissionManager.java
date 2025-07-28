package com.hezaerd.protego.permissions;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PermissionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("protego-permissions");
    private static boolean luckPermsAvailable = false;

    private PermissionManager() {}

    /**
     * Initialize the permission manager and check if LuckPerms is available
     */
    public static void initialize() {
        try {
            // Use reflection to check if LuckPerms is available
            Class.forName("net.luckperms.api.LuckPermsProvider");
            luckPermsAvailable = true;
            LOGGER.info("LuckPerms integration enabled");
        } catch (Exception e) {
            LOGGER.warn("LuckPerms not available, using fallback permission system: {}", e.getMessage());
            luckPermsAvailable = false;
        }
    }

    /**
     * Check if a player has a specific permission
     * @param player The player to check
     * @param permission The permission node to check
     * @return true if the player has the permission
     */
    public static boolean hasPermission(ServerPlayerEntity player, String permission) {
        if (!luckPermsAvailable) {
            // Fallback: check if player is operator
            return player.hasPermissionLevel(4);
        }

        try {
            // Use reflection to access LuckPerms API
            Class<?> luckPermsProviderClass = Class.forName("net.luckperms.api.LuckPermsProvider");
            Object luckPermsProvider = luckPermsProviderClass.getMethod("get").invoke(null);

            Class<?> luckPermsClass = Class.forName("net.luckperms.api.LuckPerms");
            Object userManager = luckPermsClass.getMethod("getUserManager").invoke(luckPermsProvider);
            Object user = userManager.getClass().getMethod("getUser", java.util.UUID.class).invoke(userManager, player.getUuid());

            if (user == null) {
                return false;
            }

            Object cachedData = user.getClass().getMethod("getCachedData").invoke(user);
            Object permissionData = cachedData.getClass().getMethod("getPermissionData").invoke(cachedData);
            Object result = permissionData.getClass().getMethod("checkPermission", String.class).invoke(permissionData, permission);

            return (Boolean) result.getClass().getMethod("asBoolean").invoke(result);
        } catch (Exception e) {
            LOGGER.error("Error checking permission {} for player {}: {}", permission, player.getName().getString(), e.getMessage());
            return false;
        }
    }

    /**
     * Check if a player has any of the specified permissions
     * @param player The player to check
     * @param permissions Array of permission nodes to check
     * @return true if the player has any of the permissions
     */
    public static boolean hasAnyPermission(ServerPlayerEntity player, String... permissions) {
        for (String permission : permissions) {
            if (hasPermission(player, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a player has all the specified permissions
     * @param player The player to check
     * @param permissions Array of permission nodes to check
     * @return true if the player has all permissions
     */
    public static boolean hasAllPermissions(ServerPlayerEntity player, String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(player, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the permission level of a player (0-4, where 4 is operator)
     * @param player The player to check
     * @return The permission level
     */
    public static int getPermissionLevel(ServerPlayerEntity player) {
        if (!luckPermsAvailable) {
            return player.server.getPermissionLevel(player.getGameProfile());
        }

        try {
            // Use reflection to access LuckPerms API
            Class<?> luckPermsProviderClass = Class.forName("net.luckperms.api.LuckPermsProvider");
            Object luckPermsProvider = luckPermsProviderClass.getMethod("get").invoke(null);

            Class<?> luckPermsClass = Class.forName("net.luckperms.api.LuckPerms");
            Object userManager = luckPermsClass.getMethod("getUserManager").invoke(luckPermsProvider);
            Object user = userManager.getClass().getMethod("getUser", java.util.UUID.class).invoke(userManager, player.getUuid());

            if (user == null) {
                return 0;
            }

            // Check for specific permission levels using reflection
            if (hasPermission(player, "protego.admin")) return 4;
            if (hasPermission(player, "protego.mod")) return 3;
            if (hasPermission(player, "protego.helper")) return 2;
            if (hasPermission(player, "protego.user")) return 1;

            return 0;
        } catch (Exception e) {
            LOGGER.error("Error getting permission level for player {}: {}", player.getName().getString(), e.getMessage());
            return 0;
        }
    }

    /**
     * Check if LuckPerms is available
     * @return true if LuckPerms is loaded and available
     */
    public static boolean isLuckPermsAvailable() {
        return luckPermsAvailable;
    }
}
