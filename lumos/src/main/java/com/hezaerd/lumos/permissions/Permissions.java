package com.hezaerd.lumos.permissions;

import com.hezaerd.lumos.ModLib;
import com.hezaerd.lumos.compat.Mods;
import com.hezaerd.lumos.permissions.fabric.FabricPermissionsIntegration;
import com.hezaerd.lumos.permissions.luckperms.LuckPermsIntegration;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public final class Permissions {

    /**
     * Enum representing available permission APIs
     */
    public enum PermissionAPI {
        FABRIC_PERMISSIONS_API,
        LUCK_PERMS,
        VANILLA
    }

    private static PermissionAPI detectedAPI = null;

    /**
     * Initialize optional permission integrations
     */
    public static void initialize() {
        Mods.FABRIC_PERMISSIONS_API.executeIfInstalled(() -> FabricPermissionsIntegration::initialize);
        Mods.LUCK_PERMS.executeIfInstalled(() -> LuckPermsIntegration::initialize);

        String api = getDetectedAPI();
        ModLib.LOGGER.info("Detected permission API: {}", api);
    }

    /**
     * Detect which permission API is available
     * Priority: Fabric Permissions API > LuckPerms > Vanilla
     */
    private static PermissionAPI getAvailableAPI() {
        if (detectedAPI == null) {
            if (Mods.FABRIC_PERMISSIONS_API.isLoaded()) {
                detectedAPI = PermissionAPI.FABRIC_PERMISSIONS_API;
            } else if (Mods.LUCK_PERMS.isLoaded()) {
                detectedAPI = PermissionAPI.LUCK_PERMS;
            } else {
                detectedAPI = PermissionAPI.VANILLA;
            }
        }
        return detectedAPI;
    }

    /**
     * Create a permission predicate for command registration
     * @param permission The permission node to check
     * @param fallback The fallback value when no permission plugin is available
     * @return A predicate that can be used for command registration
     */
    public static Predicate<ServerCommandSource> require(@NotNull String permission, boolean fallback) {
        return switch (getAvailableAPI()) {
            case FABRIC_PERMISSIONS_API -> Mods.FABRIC_PERMISSIONS_API.runUnsecure(() -> () ->
                FabricPermissionsIntegration.require(permission, fallback))
                    .orElse(_ignored -> fallback);
            case LUCK_PERMS -> source -> Mods.LUCK_PERMS.runUnsecure(() -> () ->
                LuckPermsIntegration.hasPermission(source, permission, fallback))
                    .orElse(fallback);
            case VANILLA -> _ignored -> fallback;
        };
    }

    /**
     * Check if a command source has a specific permission
     * @param source The command source to check
     * @param permission The permission node to check
     * @param fallback The fallback value when no permission plugin is available
     * @return true if the source has permission
     */
    public static boolean check(@NotNull ServerCommandSource source, @NotNull String permission, boolean fallback) {
        return switch (getAvailableAPI()) {
            case FABRIC_PERMISSIONS_API -> Mods.FABRIC_PERMISSIONS_API.runUnsecure(() -> () ->
                FabricPermissionsIntegration.check(source, permission, fallback))
                    .orElse(fallback);
            case LUCK_PERMS -> Mods.LUCK_PERMS.runUnsecure(() -> () ->
                LuckPermsIntegration.hasPermission(source, permission, fallback))
                    .orElse(fallback);
            case VANILLA -> fallback;
        };
    }

    /**
     * Check if a command source has a specific permission with op level fallback
     * @param source The command source to check
     * @param permission The permission node to check
     * @param opLevel The op level to fall back to (0-4)
     * @return true if the source has permission or the required op level
     */
    public static boolean check(@NotNull ServerCommandSource source, @NotNull String permission, int opLevel) {
        return switch (getAvailableAPI()) {
            case FABRIC_PERMISSIONS_API -> Mods.FABRIC_PERMISSIONS_API.runUnsecure(() -> () ->
                FabricPermissionsIntegration.check(source, permission, opLevel))
                    .orElse(source.hasPermissionLevel(opLevel));
            case LUCK_PERMS -> Mods.LUCK_PERMS.runUnsecure(() -> () ->
                LuckPermsIntegration.hasPermission(source, permission, opLevel))
                    .orElse(source.hasPermissionLevel(opLevel));
            case VANILLA -> source.hasPermissionLevel(opLevel);
        };
    }

    /**
     * Get the currently detected permission API
     * @return The detected permission API
     */
    public static String getDetectedAPI() {
        return getAvailableAPI().name();
    }

    /**
     * Check if a specific permission API is available
     * @param api The permission API to check
     * @return true if the API is available
     */
    public static boolean isAPIAvailable(PermissionAPI api) {
        return getAvailableAPI() == api;
    }
}
