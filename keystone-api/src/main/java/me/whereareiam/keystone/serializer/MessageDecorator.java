package me.whereareiam.keystone.serializer;

import me.whereareiam.keystone.model.SerializerContent;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for message decorators that can modify messages during serialization.
 * Decorators run in the pipeline before the final adapter deserializes the message.
 * Examples: PlaceholderAPI integration, MiniPlaceholders, custom placeholder systems.
 */
public interface MessageDecorator {
	/**
	 * Decorates a message by modifying the SerializerContent.
	 * This method is called during the serialization pipeline.
	 *
	 * @param content The serialization content to decorate
	 * @return The decorated content (can be the same instance or a new one)
	 */
	@NotNull
	SerializerContent decorate(@NotNull SerializerContent content);

	/**
	 * Checks if this decorator is available and should be used.
	 * Useful for optional integrations that may not be present.
	 *
	 * @return true if this decorator is available and should be used
	 */
	default boolean isAvailable() {
		return true;
	}
}

