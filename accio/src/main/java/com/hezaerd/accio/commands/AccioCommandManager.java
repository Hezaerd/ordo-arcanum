package com.hezaerd.accio.commands;

import com.hezaerd.lumos.text.RichText;
import com.hezaerd.accio.ModLib;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AccioCommandManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("accio-commands");

    private AccioCommandManager() {}

    /**
     * Register all Accio commands
     * @param dispatcher The command dispatcher
     */
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        FeurCommand.register(dispatcher);
        DiceCommand.register(dispatcher);
        LOGGER.info("Accio commands registered successfully");
    }

    /**
     * Send a formatted error message to the command source
     * @param source The command source
     * @param message The error message
     */
    public static void sendError(ServerCommandSource source, String message) {
        Text errorText = RichText.fromColorCodesWithPrefix("[Accio] ", Formatting.RED, message);
        source.sendMessage(errorText);
    }

    /**
     * Send a formatted success message to the command source
     * @param source The command source
     * @param message The success message
     */
    public static void sendSuccess(ServerCommandSource source, String message) {
        Text successText = RichText.fromColorCodesWithPrefix("[Accio] ", Formatting.GREEN, message);
        source.sendMessage(successText);
    }

    /**
     * Send a formatted info message to the command source
     * @param source The command source
     * @param message The info message
     */
    public static void sendInfo(ServerCommandSource source, String message) {
        Text infoText = RichText.fromColorCodesWithPrefix("[Accio] ", Formatting.AQUA, message);
        source.sendMessage(infoText);
    }
}