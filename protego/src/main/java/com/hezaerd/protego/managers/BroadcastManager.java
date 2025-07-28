package com.hezaerd.protego.managers;

import com.hezaerd.lumos.text.RichText;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class BroadcastManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("protego-broadcast");

    public enum BroadcastType {
        ANNOUNCEMENT("[Announcement]", Formatting.GOLD),
        ALERT("[Alert]", Formatting.RED),
        INFO("[Info]", Formatting.AQUA);

        private final String prefix;
        private final Formatting color;

        BroadcastType(String prefix, Formatting color) {
            this.prefix = prefix;
            this.color = color;
        }

        public String getPrefix() {
            return prefix;
        }

        public Formatting getColor() {
            return color;
        }
    }

    private BroadcastManager() {}

    /**
     * Broadcast a message to all online players
     * @param server The Minecraft server instance
     * @param type The type of broadcast
     * @param message The message to broadcast (supports & color codes)
     */
    public static void broadcast(MinecraftServer server, BroadcastType type, String message) {
        if (server == null || message == null || message.trim().isEmpty()) {
            LOGGER.warn("Invalid broadcast parameters");
            return;
        }

        Text broadcastText = createBroadcastText(type, message);

        // Send to all players
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player : players) {
            player.sendMessage(broadcastText);
        }

        // Log the broadcast
        LOGGER.info("Broadcast [{}]: {}", type.name(), message);
    }

    /**
     * Broadcast a message to specific players
     * @param players List of players to send the message to
     * @param type The type of broadcast
     * @param message The message to broadcast (supports & color codes)
     */
    public static void broadcastToPlayers(List<ServerPlayerEntity> players, BroadcastType type, String message) {
        if (players == null || message == null || message.trim().isEmpty()) {
            LOGGER.warn("Invalid broadcast parameters");
            return;
        }

        Text broadcastText = createBroadcastText(type, message);

        for (ServerPlayerEntity player : players) {
            player.sendMessage(broadcastText);
        }

        LOGGER.info("Broadcast [{}] to {} players: {}", type.name(), players.size(), message);
    }

    /**
     * Broadcast a message to players with specific permission
     * @param server The Minecraft server instance
     * @param type The type of broadcast
     * @param message The message to broadcast
     * @param permission The permission required to receive the broadcast
     */
    public static void broadcastToPermission(MinecraftServer server, BroadcastType type, String message, String permission) {
        if (server == null || message == null || message.trim().isEmpty()) {
            LOGGER.warn("Invalid broadcast parameters");
            return;
        }

        Text broadcastText = createBroadcastText(type, message);
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        int sentCount = 0;

        for (ServerPlayerEntity player : players) {
            if (com.hezaerd.protego.permissions.PermissionManager.hasPermission(player, permission)) {
                player.sendMessage(broadcastText);
                sentCount++;
            }
        }

        LOGGER.info("Broadcast [{}] to {} players with permission {}: {}", type.name(), sentCount, permission, message);
    }

    /**
     * Create a formatted broadcast text with colored prefix and rich text message
     * @param type The broadcast type
     * @param message The message content (supports & color codes)
     * @return Formatted Text component
     */
    private static Text createBroadcastText(BroadcastType type, String message) {
        // Create colored prefix
        MutableText prefixText = Text.literal(type.getPrefix()).formatted(type.getColor());

        // Create message text with rich text support (no pre-formatting)
        // Add reset formatting at the beginning to prevent inheriting prefix formatting
        String messageWithReset = "&f" + message;
        Text messageText = RichText.fromColorCodes(messageWithReset);

        // Combine prefix and message with a space
        return prefixText.append(Text.literal(" ")).append(messageText);
    }

    /**
     * Send a raw message to all players (no prefix)
     * @param server The Minecraft server instance
     * @param message The message to send (supports & color codes)
     */
    public static void sendRawMessage(MinecraftServer server, String message) {
        if (server == null || message == null || message.trim().isEmpty()) {
            LOGGER.warn("Invalid message parameters");
            return;
        }

        Text messageText = RichText.fromColorCodes(message);
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();

        for (ServerPlayerEntity player : players) {
            player.sendMessage(messageText);
        }

        LOGGER.info("Raw message sent to {} players: {}", players.size(), message);
    }

    /**
     * Send a message to console
     * @param server The Minecraft server instance
     * @param message The message to send
     */
    public static void sendToConsole(MinecraftServer server, String message) {
        if (server == null || message == null || message.trim().isEmpty()) {
            LOGGER.warn("Invalid console message parameters");
            return;
        }

        Text messageText = RichText.fromColorCodes(message);
        server.sendMessage(messageText);

        LOGGER.info("Console message: {}", message);
    }
}
