package me.whereareiam.keystone.common.serializer;

import me.whereareiam.keystone.model.SerializerContent;
import me.whereareiam.keystone.model.SerializerOptions;
import me.whereareiam.keystone.serializer.MessageDecorator;
import me.whereareiam.keystone.serializer.SerializerAdapter;
import me.whereareiam.keystone.serializer.SerializerEngine;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Default implementation of SerializerEngine.
 * Handles the serialization pipeline: decorators -> placeholders -> adapter.
 */
public final class DefaultSerializerEngine implements SerializerEngine {
    private final Map<String, SerializerAdapter> adapters = new ConcurrentHashMap<>();
    private final List<MessageDecorator> decorators = new ArrayList<>();
    private final Map<String, List<MessageDecorator>> scopedDecorators = new ConcurrentHashMap<>();
    private final SerializerOptions options;

	public DefaultSerializerEngine(@NotNull SerializerOptions options) {
		this.options = options;
	}

	/**
	 * Registers a serializer adapter.
	 *
	 * @param adapter The adapter to register
	 */
	public void registerAdapter(@NotNull SerializerAdapter adapter) {
		adapters.put(adapter.getId(), adapter);
	}

	/**
	 * Registers a message decorator.
	 *
	 * @param decorator The decorator to register
	 */
    public void addDecorator(@NotNull MessageDecorator decorator) {
        decorators.add(decorator);
    }

    /**
     * Registers a decorator for a specific scope.
     *
     * @param scope     The scope identifier (e.g., module name)
     * @param decorator The decorator to register
     */
    public void addDecorator(@NotNull String scope, @NotNull MessageDecorator decorator) {
        scopedDecorators
                .computeIfAbsent(scope, key -> new CopyOnWriteArrayList<>())
                .add(decorator);
    }

	/**
	 * Gets the adapter by ID, falling back to default if not found.
	 *
	 * @param adapterId The adapter ID
	 * @return The adapter, or null if not found
	 */
	@NotNull
	private SerializerAdapter getAdapter(@NotNull String adapterId) {
		SerializerAdapter adapter = adapters.get(adapterId);
		if (adapter == null) adapter = adapters.get(options.getDefaultAdapterId());
		if (adapter == null)
			throw new IllegalStateException("No adapter found for ID: " + adapterId + " and no default adapter available");

		return adapter;
	}

	@NotNull
	@Override
	public Component serialize(@NotNull SerializerContent content) {
		if (content.getMessage().isEmpty()) return Component.empty();

        // Apply decorators first (prefix injection, integrations, etc.)
        for (MessageDecorator decorator : decorators)
            if (decorator.isAvailable())
                content = decorator.decorate(content);

        // Apply scoped decorators (if scope is set)
        String scope = content.getScope();
        if (scope != null && !scope.isBlank()) {
            List<MessageDecorator> scoped = scopedDecorators.get(scope);
            if (scoped != null) {
                for (MessageDecorator decorator : scoped) {
                    if (decorator.isAvailable())
                        content = decorator.decorate(content);
                }
            }
        }

		// Apply placeholders with configured format
		String message = content.getMessage();
		for (Map.Entry<String, String> entry : content.getPlaceholders().entrySet()) {
			String formattedPlaceholder = options.getPlaceholderFormat().format(entry.getKey());
			message = message.replace(formattedPlaceholder, entry.getValue());
		}

		SerializerAdapter adapter = getAdapter(options.getDefaultAdapterId());

		return adapter.deserialize(message, options.isEnableLegacyColors());
	}

	@NotNull
	@Override
	public String serialize(@NotNull Component component) {
		SerializerAdapter adapter = getAdapter(options.getDefaultAdapterId());
		return adapter.serialize(component);
	}

	@NotNull
	@Override
	public SerializerOptions.PlaceholderFormat getPlaceholderFormat() {
		return options.getPlaceholderFormat();
	}
}

