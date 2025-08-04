package com.hezaerd.lumos.text;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Utility class for handling translated text and common text operations
 */
public final class TextHelper {

	private TextHelper() {} // Utility class

	/**
	 * Get a translated text for the given key
	 * @param key The translation key
	 * @param args The format arguments
	 * @return The translated text
	 */
	public static Text getTranslatedText(String key, Object... args) {
		return Text.translatable(key, args);
	}

	/**
	 * Get a translated text with a colored prefix
	 * @param prefix The prefix text
	 * @param prefixColor The color for the prefix
	 * @param key The translation key
	 * @param args The format arguments
	 * @return The translated text with prefix
	 */
	public static Text getTranslatedTextWithPrefix(String prefix, Formatting prefixColor, String key, Object... args) {
		return RichText.fromColorCodesWithPrefix(prefix, prefixColor, getTranslatedText(key, args).getString());
	}

	/**
	 * Get a translated text as a string
	 * @param key The translation key
	 * @param args The format arguments
	 * @return The translated text as a string
	 */
	public static String getTranslatedString(String key, Object... args) {
		return getTranslatedText(key, args).getString();
	}

	/**
	 * Get a translated text with prefix as a string
	 * @param prefix The prefix text
	 * @param prefixColor The color for the prefix
	 * @param key The translation key
	 * @param args The format arguments
	 * @return The translated text with prefix as a string
	 */
	public static String getTranslatedStringWithPrefix(String prefix, Formatting prefixColor, String key, Object... args) {
		return getTranslatedTextWithPrefix(prefix, prefixColor, key, args).getString();
	}

	/**
	 * Get a translated text that supports RichText color codes
	 * @param key The translation key
	 * @param args The format arguments
	 * @return The translated text with RichText formatting
	 */
	public static Text getTranslatedRichText(String key, Object... args) {
		String translatedString = getTranslatedString(key, args);
		return RichText.fromColorCodes(translatedString);
	}

	/**
	 * Get a translated text with prefix that supports RichText color codes
	 * @param prefix The prefix text
	 * @param prefixColor The color for the prefix
	 * @param key The translation key
	 * @param args The format arguments
	 * @return The translated text with prefix and RichText formatting
	 */
	public static Text getTranslatedRichTextWithPrefix(String prefix, Formatting prefixColor, String key, Object... args) {
		String translatedString = getTranslatedString(key, args);
		return RichText.fromColorCodesWithPrefix(prefix, prefixColor, translatedString);
	}
}