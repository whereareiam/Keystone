package me.whereareiam.keystone.serializer;

import me.whereareiam.keystone.Actor;
import me.whereareiam.keystone.model.SerializerContent;
import me.whereareiam.keystone.model.SerializerOptions;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Main interface for serializing messages into Adventure Components.
 * Handles placeholder replacement, prefix injection, and adapter selection.
 */
@SuppressWarnings("unused")
public interface SerializerEngine {
	/**
	 * Serializes a message string into a Component using the configured adapter.
	 *
	 * @param content The serialization content containing message, actor, and placeholders
	 * @return The serialized Component
	 */
	@NotNull
	Component serialize(@NotNull SerializerContent content);

	/**
	 * Convenience method to serialize a message for an actor.
	 * Automatically handles prefix and playerName placeholders.
	 *
	 * @param actor   The actor to serialize the message for
	 * @param message The message template
	 * @return The serialized Component
	 */
	@NotNull
	default Component serialize(@NotNull Actor actor, @NotNull String message) {
		return serialize(SerializerContent.builder()
				.receiver(actor)
				.message(message)
				.build());
	}

	/**
	 * Convenience method to serialize a message with custom placeholders.
	 *
	 * @param actor      The actor to serialize the message for
	 * @param message    The message template
	 * @param customizer Consumer to customize the SerializerContent builder
	 * @return The serialized Component
	 */
	@NotNull
	default Component serialize(@NotNull Actor actor, @NotNull String message, @NotNull Consumer<SerializerContent.Builder> customizer) {
		SerializerContent.Builder builder = SerializerContent.builder()
				.receiver(actor)
				.message(message);
		customizer.accept(builder);
		return serialize(builder.build());
	}

	/**
	 * Sends a serialized message directly to an actor.
	 *
	 * @param actor   The actor to send the message to
	 * @param message The message template
	 */
	default void send(@NotNull Actor actor, @NotNull String message) {
		actor.sendMessage(serialize(actor, message));
	}

	/**
	 * Sends a serialized message with custom placeholders directly to an actor.
	 *
	 * @param actor      The actor to send the message to
	 * @param message    The message template
	 * @param customizer Consumer to customize the SerializerContent builder
	 */
	default void send(@NotNull Actor actor, @NotNull String message, @NotNull Consumer<SerializerContent.Builder> customizer) {
		actor.sendMessage(serialize(actor, message, customizer));
	}

	/**
	 * Serializes a message without requiring a receiver actor.
	 * Useful for messages that don't need actor-specific placeholders like playerName or prefix.
	 *
	 * @param message The message template
	 * @return The serialized Component
	 */
	@NotNull
	default Component serialize(@NotNull String message) {
		return serialize(SerializerContent.builder()
				.message(message)
				.build());
	}

	/**
	 * Serializes a message with custom placeholders without requiring a receiver actor.
	 *
	 * @param message    The message template
	 * @param customizer Consumer to customize the SerializerContent builder
	 * @return The serialized Component
	 */
	@NotNull
	default Component serialize(@NotNull String message, @NotNull Consumer<SerializerContent.Builder> customizer) {
		SerializerContent.Builder builder = SerializerContent.builder()
				.message(message);
		customizer.accept(builder);

		return serialize(builder.build());
	}

	/**
	 * Gets the placeholder format used by this serializer.
	 *
	 * @return The placeholder format
	 */
	@NotNull
	default SerializerOptions.PlaceholderFormat getPlaceholderFormat() {
		return SerializerOptions.PlaceholderFormat.CURLY_BRACES;
	}
}

