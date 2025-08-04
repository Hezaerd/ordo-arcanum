package com.hezaerd.lumos.text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class RichText {
	private static final Map<Character, Formatting> COLOR_CODES = new HashMap<>();
	private static final Pattern COLOR_PATTERN = Pattern.compile("&([0-9a-fk-or])", Pattern.CASE_INSENSITIVE);

	static {
		// Color codes (0-9, a-f)
		COLOR_CODES.put('0', Formatting.BLACK);
		COLOR_CODES.put('1', Formatting.DARK_BLUE);
		COLOR_CODES.put('2', Formatting.DARK_GREEN);
		COLOR_CODES.put('3', Formatting.DARK_AQUA);
		COLOR_CODES.put('4', Formatting.DARK_RED);
		COLOR_CODES.put('5', Formatting.DARK_PURPLE);
		COLOR_CODES.put('6', Formatting.GOLD);
		COLOR_CODES.put('7', Formatting.GRAY);
		COLOR_CODES.put('8', Formatting.DARK_GRAY);
		COLOR_CODES.put('9', Formatting.BLUE);
		COLOR_CODES.put('a', Formatting.GREEN);
		COLOR_CODES.put('b', Formatting.AQUA);
		COLOR_CODES.put('c', Formatting.RED);
		COLOR_CODES.put('d', Formatting.LIGHT_PURPLE);
		COLOR_CODES.put('e', Formatting.YELLOW);
		COLOR_CODES.put('f', Formatting.WHITE);

		// Formatting codes (k-o, r)
		COLOR_CODES.put('k', Formatting.OBFUSCATED);
		COLOR_CODES.put('l', Formatting.BOLD);
		COLOR_CODES.put('m', Formatting.STRIKETHROUGH);
		COLOR_CODES.put('n', Formatting.UNDERLINE);
		COLOR_CODES.put('o', Formatting.ITALIC);
		COLOR_CODES.put('r', Formatting.RESET);
	}

	private RichText() {} // Utility class

	/**
	 * Converts a string with & color codes to a Minecraft Text component
	 * @param text The text with & color codes (e.g., "&cHello &lWorld&r")
	 * @return Minecraft Text component with proper formatting
	 */
	public static Text fromColorCodes(String text) {
		if (text == null || text.isEmpty()) {
			return Text.empty();
		}

		MutableText result = Text.empty();
		StringBuilder currentText = new StringBuilder();
		Formatting currentFormatting = null;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			if (c == '&' && i + 1 < text.length()) {
				char colorCode = Character.toLowerCase(text.charAt(i + 1));
				Formatting formatting = COLOR_CODES.get(colorCode);

				if (formatting != null) {
					// Add accumulated text before applying new formatting
					if (!currentText.isEmpty()) {
						MutableText textComponent = Text.literal(currentText.toString());
						if (currentFormatting != null) {
							textComponent.formatted(currentFormatting);
						}
						result.append(textComponent);
						currentText.setLength(0);
					}

					currentFormatting = formatting;
					i++; // Skip the color code
					continue;
				}
			}

			currentText.append(c);
		}

		// Add any remaining text
		if (!currentText.isEmpty()) {
			MutableText textComponent = Text.literal(currentText.toString());
			if (currentFormatting != null) {
				textComponent.formatted(currentFormatting);
			}
			result.append(textComponent);
		}

		return result;
	}

	/**
	 * Converts a string with & color codes to a Minecraft Text component with a prefix
	 * @param prefix The prefix text (will be formatted with the specified color)
	 * @param prefixColor The color for the prefix (e.g., Formatting.GOLD)
	 * @param message The message with & color codes
	 * @return Minecraft Text component with prefix and formatted message
	 */
	public static Text fromColorCodesWithPrefix(String prefix, Formatting prefixColor, String message) {
		MutableText prefixText = Text.literal(prefix).formatted(prefixColor);
		Text messageText = fromColorCodes(message);
		return prefixText.append(messageText);
	}

	/**
	 * Strips all & color codes from a string
	 * @param text The text with color codes
	 * @return The text without color codes
	 */
	public static String stripColorCodes(String text) {
		if (text == null) return "";
		return COLOR_PATTERN.matcher(text).replaceAll("");
	}

	/**
	 * Checks if a string contains color codes
	 * @param text The text to check
	 * @return true if the text contains & color codes
	 */
	public static boolean hasColorCodes(String text) {
		return text != null && COLOR_PATTERN.matcher(text).find();
	}
}
