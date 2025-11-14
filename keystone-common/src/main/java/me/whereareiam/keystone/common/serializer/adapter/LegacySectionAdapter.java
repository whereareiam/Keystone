package me.whereareiam.keystone.common.serializer.adapter;

import me.whereareiam.keystone.serializer.SerializerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * Legacy section symbol (§) adapter for serializing components.
 * Supports § color codes and hex colors (§#RRGGBB).
 */
public final class LegacySectionAdapter implements SerializerAdapter {
	private static final LegacyComponentSerializer LEGACY_SECTION = LegacyComponentSerializer.builder()
			.hexColors()
			.character('§')
			.build();

	@Override
	@NotNull
	public String getId() {
		return "LEGACY_SECTION";
	}

	@Override
	@NotNull
	public Component deserialize(@NotNull String input, boolean legacyInputEnabled) {
		return LEGACY_SECTION.deserialize(input);
	}

	@Override
	@NotNull
	public String serialize(@NotNull Component component) {
		return LEGACY_SECTION.serialize(component);
	}
}

