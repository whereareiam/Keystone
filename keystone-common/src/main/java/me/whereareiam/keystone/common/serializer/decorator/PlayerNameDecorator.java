package me.whereareiam.keystone.common.serializer.decorator;

import me.whereareiam.keystone.model.SerializerContent;
import me.whereareiam.keystone.serializer.MessageDecorator;
import org.jetbrains.annotations.NotNull;

/**
 * Decorator that injects the playerName placeholder from the receiver if it's a Player.
 */
public final class PlayerNameDecorator implements MessageDecorator {
	@Override
	@NotNull
	public SerializerContent decorate(@NotNull SerializerContent content) {
		if (content.getReceiver() == null)
			return content;

		content.addPlaceholder("playerName", content.getReceiver().getUsername());

		return content;
	}
}
