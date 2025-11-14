package me.whereareiam.keystone.common.serializer.decorator;

import me.whereareiam.keystone.model.SerializerContent;
import me.whereareiam.keystone.serializer.MessageDecorator;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Decorator that injects {prefix} placeholder from a supplier.
 */
public final class PrefixDecorator implements MessageDecorator {
	private final Supplier<String> prefixSupplier;

	public PrefixDecorator(@NotNull Supplier<String> prefixSupplier) {
		this.prefixSupplier = prefixSupplier;
	}

	@Override
	@NotNull
	public SerializerContent decorate(@NotNull SerializerContent content) {
		String prefix = prefixSupplier.get();
		if (prefix != null && !prefix.isEmpty())
			content.setMessage(content.getMessage().replace("{prefix}", prefix));

		return content;
	}
}

