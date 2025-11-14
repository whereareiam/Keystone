package me.whereareiam.keystone.common.serializer.adapter;

import me.whereareiam.keystone.serializer.SerializerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * Legacy ampersand (&) adapter for serializing components.
 * Supports & color codes and hex colors (&#RRGGBB).
 */
public final class LegacyAmpersandAdapter implements SerializerAdapter {
	private static final LegacyComponentSerializer LEGACY_AMPERSAND = LegacyComponentSerializer.builder()
			.hexColors()
			.character('&')
			.build();

	@Override
	@NotNull
	public String getId() {
		return "LEGACY_AMPERSAND";
	}

	@Override
	@NotNull
	public Component deserialize(@NotNull String input, boolean legacyInputEnabled) {
		// This adapter already handles legacy codes natively, legacyInputEnabled flag is ignored
		return LEGACY_AMPERSAND.deserialize(input);
	}

	@Override
	@NotNull
	public String serialize(@NotNull Component component) {
		return LEGACY_AMPERSAND.serialize(component);
	}
}

