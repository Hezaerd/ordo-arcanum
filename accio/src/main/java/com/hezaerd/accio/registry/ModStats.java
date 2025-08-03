package com.hezaerd.accio.registry;

import com.hezaerd.accio.ModLib;
import eu.pb4.polymer.core.api.other.PolymerStat;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;

public final class ModStats {
    public static final Identifier FAILED_FEUR = register("failed_feur", StatFormatter.DEFAULT);
    public static final Identifier SUCCESS_FEUR = register("success_feur", StatFormatter.DEFAULT);
    public static final Identifier DICE_DUELS_WON = register("dice_duels_won", StatFormatter.DEFAULT);
    public static final Identifier DICE_DUELS_LOST = register("dice_duels_lost", StatFormatter.DEFAULT);
    public static final Identifier DICE_DUELS_TIED = register("dice_duels_tied", StatFormatter.DEFAULT);

    public static void init() {
        ModLib.LOGGER.info("Registering mod stats for {}", ModLib.MOD_NAME);
    }
    
    private static Identifier register(String name, StatFormatter formatter) {
        return PolymerStat.registerStat(ModLib.id(name), formatter);
    }
}
