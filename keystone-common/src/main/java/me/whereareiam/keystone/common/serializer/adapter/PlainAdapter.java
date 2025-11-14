package me.whereareiam.keystone.common.serializer.adapter;

import me.whereareiam.keystone.serializer.SerializerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * Plain text adapter for serializing components.
 * This adapter strips all formatting and returns plain text.
 */
public final class PlainAdapter implements SerializerAdapter {
	private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();

	@NotNull
	@Override
	public String getId() {
		return "plain";
	}

	@NotNull
	@Override
	public Component deserialize(@NotNull String input, boolean legacyInputEnabled) {
		if (legacyInputEnabled) {
			LegacyComponentSerializer legacy = LegacyComponentSerializer.legacyAmpersand();
			return legacy.deserialize(input);
		}

		return Component.text(input);
	}

	@NotNull
	@Override
	public String serialize(@NotNull Component component) {
		return PLAIN.serialize(component);
	}
}

