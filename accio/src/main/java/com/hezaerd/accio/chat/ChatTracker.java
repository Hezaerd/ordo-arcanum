package com.hezaerd.accio.chat;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.server.network.ServerPlayerEntity;

public final class ChatTracker {
	private static final Logger LOGGER = LoggerFactory.getLogger("accio-chat-tracker");

	// Array of trigger words to check for
	private static final List<String> TRIGGER_WORDS = Arrays.asList(
		"quoi",
		"pk",
		"pourquoi",
		"kwa"
	);

	private static ServerPlayerEntity lastTriggerPlayer = null;
	private static long lastTriggerTime = 0;
	private static final long TRIGGER_TIMEOUT = 15000; // 15 seconds timeout
	private static String lastTriggerWord = null;

	private ChatTracker() {}

	/**
	 * Process a chat message to check for trigger words
	 * @param player The player who sent the message
	 * @param message The message content
	 */
	public static void processMessage(ServerPlayerEntity player, String message) {
		String lowerMessage = message.toLowerCase();

		for (String triggerWord : TRIGGER_WORDS) {
			if (lowerMessage.contains(triggerWord)) {
				lastTriggerPlayer = player;
				lastTriggerTime = System.currentTimeMillis();
				lastTriggerWord = triggerWord;
				LOGGER.debug("Player {} said '{}' at {}", player.getName().getString(), triggerWord, lastTriggerTime);
				return; // Exit after finding the first match
			}
		}
	}

	/**
	 * Get the last player who said a trigger word if within timeout
	 * @return The player who said a trigger word, or null if no valid player
	 */
	public static ServerPlayerEntity getLastTriggerPlayer() {
		if (lastTriggerPlayer == null) {
			return null;
		}

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastTriggerTime > TRIGGER_TIMEOUT) {
			// Timeout expired, clear the player
			lastTriggerPlayer = null;
			lastTriggerWord = null;
			return null;
		}

		return lastTriggerPlayer;
	}

	/**
	 * Get the last trigger word that was said
	 * @return The trigger word, or null if no valid trigger
	 */
	public static String getLastTriggerWord() {
		if (lastTriggerWord == null) {
			return null;
		}

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastTriggerTime > TRIGGER_TIMEOUT) {
			// Timeout expired, clear the word
			lastTriggerWord = null;
			return null;
		}

		return lastTriggerWord;
	}

	/**
	 * Clear the last trigger player (used when /feur is executed)
	 */
	public static void clearLastTriggerPlayer() {
		lastTriggerPlayer = null;
		lastTriggerTime = 0;
		lastTriggerWord = null;
	}

	/**
	 * Check if there's a valid trigger player
	 * @return true if there's a valid player who said a trigger word
	 */
	public static boolean hasValidTriggerPlayer() {
		return getLastTriggerPlayer() != null;
	}

	/**
	 * Get the list of trigger words
	 * @return The list of trigger words
	 */
	public static List<String> getTriggerWords() {
		return TRIGGER_WORDS;
	}

	/**
	 * Add a new trigger word to the list
	 * @param word The word to add
	 */
	public static void addTriggerWord(String word) {
		if (word != null && !word.trim().isEmpty() && !TRIGGER_WORDS.contains(word.toLowerCase())) {
			TRIGGER_WORDS.add(word.toLowerCase());
			LOGGER.info("Added trigger word: {}", word.toLowerCase());
		}
	}
}