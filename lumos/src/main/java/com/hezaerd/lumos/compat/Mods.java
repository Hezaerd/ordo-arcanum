package com.hezaerd.lumos.compat;

import net.fabricmc.loader.api.FabricLoader;

import java.util.Optional;
import java.util.function.Supplier;

public enum Mods {
    FABRIC_PERMISSIONS_API("fabric-permissions-api-v0"),
    LUCK_PERMS("luckperms");

    private final String id;
    private final boolean loaded;

    Mods(String modId) {
        id = modId;
        loaded = FabricLoader.getInstance().isModLoaded(modId);
    }

    /**
     * @return the mod id
     */
    public String id() {
        return id;
    }

    /**
     * @return true if the mod is loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Hook to run code if a mod is installed
     * @param toRun will be executed ONLY if the mod is installed
     * @return Optional.empty() if the mod is not loaded, otherwise an Optional of the return value of the given supplier
     */
    public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
        if (isLoaded())
            return Optional.of(toRun.get().get());
        return Optional.empty();
    }

    /**
     * Hook to execute code if a mod is installed
     * @param toExecute will be executed ONLY if the mod is installed
     */
    public void executeIfInstalled(Supplier<Runnable> toExecute) {
        if (isLoaded()) {
            toExecute.get().run();
        }
    }

    /**
     * Unsecure version that skips runtime check - use only when you've already verified the mod is loaded
     * @param toRun will be executed (assumes mod is loaded)
     * @return Optional.empty() if the mod is not loaded, otherwise an Optional of the return value of the given supplier
     */
    public <T> Optional<T> runUnsecure(Supplier<Supplier<T>> toRun) {
        return Optional.of(toRun.get().get());
    }

    /**
     * Unsecure version that skips runtime check - use only when you've already verified the mod is loaded
     * @param toExecute will be executed (assumes mod is loaded)
     */
    public void executeUnsecure(Supplier<Runnable> toExecute) {
        toExecute.get().run();
    }
}
