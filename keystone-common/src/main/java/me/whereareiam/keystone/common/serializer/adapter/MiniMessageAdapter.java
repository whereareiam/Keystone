package me.whereareiam.keystone.common.serializer.adapter;

import me.whereareiam.keystone.serializer.SerializerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * MiniMessage adapter for serializing components.
 * Supports legacy color code input when enabled.
 */
public final class MiniMessageAdapter implements SerializerAdapter {
	private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
	private static final Pattern LEGACY_DETECTION = Pattern.compile("[&§][0-9a-fk-orx#]", Pattern.CASE_INSENSITIVE);

	private static final LegacyComponentSerializer LEGACY_AMPERSAND = LegacyComponentSerializer.builder()
			.hexColors()
			.character('&')
			.build();

	private static final LegacyComponentSerializer LEGACY_SECTION = LegacyComponentSerializer.builder()
			.hexColors()
			.character('§')
			.build();

	@Override
	@NotNull
	public String getId() {
		return "MINIMESSAGE";
	}

	@Override
	@NotNull
	public Component deserialize(@NotNull String input, boolean legacyInputEnabled) {
		if (legacyInputEnabled && LEGACY_DETECTION.matcher(input).find())
			return parseLegacy(input);

		return MINI_MESSAGE.deserialize(input);
	}

	/**
	 * Parses a legacy-formatted string into a Component.
	 * Automatically detects whether to use & or § based on what's present.
	 *
	 * @param input The legacy-formatted string
	 * @return The parsed Component
	 */
	@NotNull
	private Component parseLegacy(@NotNull String input) {
		if (input.contains("§")) return LEGACY_SECTION.deserialize(input);

		return LEGACY_AMPERSAND.deserialize(input);
	}

	@Override
	@NotNull
	public String serialize(@NotNull Component component) {
		return MINI_MESSAGE.serialize(component);
	}
}

