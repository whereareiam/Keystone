package me.whereareiam.keystone.serializer;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for serialization adapters that convert strings to Adventure Components.
 * Adapters can be registered to provide different serialization formats (MiniMessage, Gson, etc.).
 */
@SuppressWarnings("unused")
public interface SerializerAdapter {
	/**
	 * Gets the unique identifier for this adapter.
	 * Used when registering and selecting adapters.
	 *
	 * @return The adapter identifier (e.g., "MINIMESSAGE", "GSON", "PLAIN")
	 */
	@NotNull
	String getId();

	/**
	 * Deserializes a string into a Component using this adapter's format.
	 * If legacy input support is enabled and the input contains legacy color codes,
	 * the adapter should handle the conversion appropriately.
	 *
	 * @param input              The serialized string to deserialize
	 * @param legacyInputEnabled Whether legacy color code input should be processed
	 * @return The deserialized Component
	 */
	@NotNull
	Component deserialize(@NotNull String input, boolean legacyInputEnabled);

	/**
	 * Serializes a Component into a string using this adapter's format.
	 *
	 * @param component The Component to serialize
	 * @return The serialized string
	 */
	@NotNull
	String serialize(@NotNull Component component);
}

