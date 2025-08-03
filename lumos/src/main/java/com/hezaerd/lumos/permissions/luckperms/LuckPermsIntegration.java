package com.hezaerd.lumos.permissions.luckperms;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * LuckPerms integration - only loaded when LuckPerms is installed
 */
public final class LuckPermsIntegration {
    private static final Logger LOGGER = LoggerFactory.getLogger("lumos-luckperms");
    private static LuckPerms api = null;

    private LuckPermsIntegration() {}

    /**
     * Initialize LuckPerms integration
     */
    public static void initialize() {
        try {
            api = LuckPermsProvider.get();
            LOGGER.info("LuckPerms integration initialized successfully");
        } catch (Exception e) {
            LOGGER.warn("Failed to initialize LuckPerms integration: {}", e.getMessage());
            api = null;
        }
    }

    /**
     * Check if LuckPerms is available
     */
    public static boolean isAvailable() {
        return api != null;
    }

    /**
     * Get a player's UUID from ServerCommandSource
     */
    private static UUID getPlayerUUID(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        return player != null ? player.getUuid() : null;
    }

    /**
     * Get a player's UUID from Entity
     */
    private static UUID getPlayerUUID(Entity entity) {
        if (entity instanceof ServerPlayerEntity player) {
            return player.getUuid();
        }
        return null;
    }

    /**
     * Check permission using LuckPerms
     */
    private static boolean checkLuckPermsPermission(UUID playerUUID, String permission) {
        if (!isAvailable() || playerUUID == null) {
            return false;
        }

        try {
            User user = api.getUserManager().loadUser(playerUUID).join();
            if (user == null) {
                return false;
            }

            CachedDataManager cachedData = user.getCachedData();
            CachedPermissionData permissionData = cachedData.getPermissionData();

            return permissionData.checkPermission(permission).asBoolean();

        } catch (Exception e) {
            LOGGER.warn("Error checking LuckPerms permission: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check permission for ServerCommandSource with boolean fallback
     */
    public static boolean hasPermission(ServerCommandSource source, String permission, boolean fallback) {
        UUID playerUUID = getPlayerUUID(source);
        if (playerUUID == null) {
            return fallback;
        }

        boolean result = checkLuckPermsPermission(playerUUID, permission);
        return result || fallback;
    }

    /**
     * Check permission for ServerCommandSource with level fallback
     */
    public static boolean hasPermission(ServerCommandSource source, String permission, int level) {
        UUID playerUUID = getPlayerUUID(source);
        if (playerUUID == null) {
            return source.hasPermissionLevel(level);
        }

        boolean result = checkLuckPermsPermission(playerUUID, permission);
        return result || source.hasPermissionLevel(level);
    }

    /**
     * Check permission for Entity with boolean fallback
     */
    public static boolean hasPermission(Entity entity, String permission, boolean fallback) {
        UUID playerUUID = getPlayerUUID(entity);
        if (playerUUID == null) {
            return fallback;
        }

        boolean result = checkLuckPermsPermission(playerUUID, permission);
        return result || fallback;
    }

    /**
     * Check permission for Entity with level fallback
     */
    public static boolean hasPermission(Entity entity, String permission, int level) {
        UUID playerUUID = getPlayerUUID(entity);
        if (playerUUID == null) {
            return entity.getCommandSource().hasPermissionLevel(level);
        }

        boolean result = checkLuckPermsPermission(playerUUID, permission);
        return result || entity.getCommandSource().hasPermissionLevel(level);
    }
}