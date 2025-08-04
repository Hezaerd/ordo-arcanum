package com.hezaerd.protego.managers;

import java.util.List;

import com.hezaerd.lumos.text.RichText;
import com.hezaerd.lumos.text.TextHelper;
import com.hezaerd.protego.text.TranslationKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class BroadcastManager {
	private static final Logger LOGGER = LoggerFactory.getLogger("protego-broadcast");

	public enum BroadcastType {
		ANNOUNCEMENT(TranslationKeys.Broadcast.ANNOUNCEMENT_PREFIX, Formatting.GOLD),
		ALERT(TranslationKeys.Broadcast.ALERT_PREFIX, Formatting.RED),
		INFO(TranslationKeys.Broadcast.INFO_PREFIX, Formatting.AQUA);

		private final String prefixKey;
		private final Formatting color;

		BroadcastType(String prefixKey, Formatting color) {
			this.prefixKey = prefixKey;
			this.color = color;
		}

		public String getPrefix() {
			return TextHelper.getTranslatedString(prefixKey);
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
			if (com.hezaerd.lumos.permissions.Permissions.check(player.getCommandSource(), permission, false)) {
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
		// Create colored prefix with RichText support
		Text prefixText = RichText.fromColorCodes(type.getPrefix());

		// Create message text with rich text support
		// Add reset formatting at the beginning to prevent inheriting prefix formatting
		String messageWithReset = "&f" + message;
		Text messageText = RichText.fromColorCodes(messageWithReset);

		// Combine prefix and message with a space
		return Text.literal("").append(prefixText).append(Text.literal(" ")).append(messageText);
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
