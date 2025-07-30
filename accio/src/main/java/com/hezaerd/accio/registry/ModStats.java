package com.hezaerd.accio.registry;

import com.hezaerd.accio.ModLib;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public final class ModStats {
    public static final Identifier FAILED_FEUR = register("failed_feur", StatFormatter.DEFAULT);
    public static final Identifier SUCCESS_FEUR = register("sucess_feur", StatFormatter.DEFAULT);

    public static void init() {
        ModLib.LOGGER.info("Registering mod stats for {}", ModLib.MOD_NAME);
    }

    private static Identifier register(String name, StatFormatter formatter) {
        Identifier id = ModLib.id(name);
        Registry.register(Registries.CUSTOM_STAT, name, id);
        Stats.CUSTOM.getOrCreateStat(id, formatter);
        return id;
    }
}
