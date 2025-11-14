package me.whereareiam.keystone.common.serializer.adapter;

import me.whereareiam.keystone.serializer.SerializerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * Gson adapter for serializing components.
 */
public final class GsonAdapter implements SerializerAdapter {
	private static final GsonComponentSerializer GSON = GsonComponentSerializer.gson();

	@Override
	@NotNull
	public String getId() {
		return "GSON";
	}

	@Override
	@NotNull
	public Component deserialize(@NotNull String input, boolean legacyInputEnabled) {
		return GSON.deserialize(input);
	}

	@Override
	@NotNull
	public String serialize(@NotNull Component component) {
		return GSON.serialize(component);
	}
}

