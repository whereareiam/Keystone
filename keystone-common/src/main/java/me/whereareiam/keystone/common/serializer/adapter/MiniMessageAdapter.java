package me.whereareiam.keystone.common.serializer.adapter;

import me.whereareiam.keystone.serializer.SerializerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * MiniMessage adapter for serializing components.
 * Supports legacy color code input when enabled.
 */
public final class MiniMessageAdapter implements SerializerAdapter {
	private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
	private static final MiniMessage LEGACY_AWARE_MINI_MESSAGE = MiniMessage.builder()
			.postProcessor(MiniMessageAdapter::applyLegacyFormatting)
			.build();
	private static final Pattern LEGACY_DETECTION = Pattern.compile("[&§][0-9a-fk-orx#]", Pattern.CASE_INSENSITIVE);

	@Override
	@NotNull
	public String getId() {
		return "MINIMESSAGE";
	}

	@Override
	@NotNull
	public Component deserialize(@NotNull String input, boolean legacyInputEnabled) {
		if (!legacyInputEnabled) return MINI_MESSAGE.deserialize(input);

		return LEGACY_AWARE_MINI_MESSAGE.deserialize(input);
	}

	@NotNull
	private static Component applyLegacyFormatting(@NotNull Component component) {
		List<Component> rewrittenChildren = new ArrayList<>(component.children().size());
		for (Component child : component.children()) {
			rewrittenChildren.add(applyLegacyFormatting(child));
		}

		if (component instanceof TextComponent textComponent) {
			Component rewrittenText = rewriteLegacyText(textComponent.content());
			if (rewrittenText == null)
				return textComponent.children(rewrittenChildren);

			Component result = rewrittenText.applyFallbackStyle(textComponent.style());
			for (Component child : rewrittenChildren) {
				result = result.append(child);
			}
			return result;
		}

		return component.children(rewrittenChildren);
	}

	private static Component rewriteLegacyText(@NotNull String input) {
		String miniMessage = legacyToMiniMessage(input);
		if (miniMessage == null)
			return null;

		return MINI_MESSAGE.deserialize(miniMessage);
	}

	private static String legacyToMiniMessage(@NotNull String input) {
		if (!LEGACY_DETECTION.matcher(input).find())
			return null;

		StringBuilder output = new StringBuilder(input.length());
		StringBuilder literal = new StringBuilder(input.length());
		boolean converted = false;

		for (int index = 0; index < input.length(); index++) {
			char marker = input.charAt(index);
			if ((marker == '&' || marker == '§') && isLikelyLegacyFormatting(input, index)) {
				int consumed = appendLegacyTag(input, index, output, literal);
				if (consumed > 0) {
					converted = true;
					index += consumed - 1;
					continue;
				}
			}

			literal.append(marker);
		}

		flushLiteral(output, literal);
		return converted ? output.toString() : null;
	}

	private static boolean isLikelyLegacyFormatting(@NotNull String input, int index) {
		if (index == 0)
			return true;

		return Character.isWhitespace(input.charAt(index - 1));
	}

	private static int appendLegacyTag(
			@NotNull String input,
			int index,
			@NotNull StringBuilder output,
			@NotNull StringBuilder literal
	) {
		if (index + 1 >= input.length())
			return 0;

		char code = Character.toLowerCase(input.charAt(index + 1));
		String tag = switch (code) {
			case '0' -> "<black>";
			case '1' -> "<dark_blue>";
			case '2' -> "<dark_green>";
			case '3' -> "<dark_aqua>";
			case '4' -> "<dark_red>";
			case '5' -> "<dark_purple>";
			case '6' -> "<gold>";
			case '7' -> "<gray>";
			case '8' -> "<dark_gray>";
			case '9' -> "<blue>";
			case 'a' -> "<green>";
			case 'b' -> "<aqua>";
			case 'c' -> "<red>";
			case 'd' -> "<light_purple>";
			case 'e' -> "<yellow>";
			case 'f' -> "<white>";
			case 'k' -> "<obfuscated>";
			case 'l' -> "<bold>";
			case 'm' -> "<strikethrough>";
			case 'n' -> "<underlined>";
			case 'o' -> "<italic>";
			case 'r' -> "<reset>";
			default -> null;
		};

		if (tag != null) {
			flushLiteral(output, literal);
			output.append(tag);
			return 2;
		}

		if (code == '#' && index + 7 < input.length()) {
			String hex = input.substring(index + 2, index + 8);
			if (isHexColor(hex)) {
				flushLiteral(output, literal);
				output.append("<#").append(hex).append(">");
				return 8;
			}
		}

		if (code == 'x') {
			String hex = readLegacyHexSequence(input, index);
			if (hex != null) {
				flushLiteral(output, literal);
				output.append("<#").append(hex).append(">");
				return 14;
			}
		}

		return 0;
	}

	private static void flushLiteral(@NotNull StringBuilder output, @NotNull StringBuilder literal) {
		if (literal.isEmpty())
			return;

		output.append(MINI_MESSAGE.escapeTags(literal.toString()));
		literal.setLength(0);
	}

	private static boolean isHexColor(@NotNull String value) {
		for (int index = 0; index < value.length(); index++) {
			if (isHexDigit(value.charAt(index)))
				return false;
		}

		return true;
	}

	private static String readLegacyHexSequence(@NotNull String input, int index) {
		if (index + 13 >= input.length())
			return null;

		StringBuilder hex = new StringBuilder(6);
		for (int offset = 2; offset < 14; offset += 2) {
			char marker = input.charAt(index + offset);
			char digit = input.charAt(index + offset + 1);
			if ((marker != '&' && marker != '§') || isHexDigit(digit))
				return null;

			hex.append(digit);
		}

		return hex.toString();
	}

	private static boolean isHexDigit(char value) {
		return (value < '0' || value > '9')
				&& (value < 'a' || value > 'f')
				&& (value < 'A' || value > 'F');
	}

	@Override
	@NotNull
	public String serialize(@NotNull Component component) {
		return MINI_MESSAGE.serialize(component);
	}
}
