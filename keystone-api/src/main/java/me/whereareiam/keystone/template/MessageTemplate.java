package me.whereareiam.keystone.template;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Fluent helper for rendering a single string template or mapping a template across a stream.
 */
@SuppressWarnings("unused")
public final class MessageTemplate {
	private final TemplateEngine engine;
	private final String template;
	private final Map<String, String> placeholders;

	MessageTemplate(
			@NotNull TemplateEngine engine,
			@NotNull String template
	) {
		this(engine, template, Map.of());
	}

	private MessageTemplate(
			@NotNull TemplateEngine engine,
			@NotNull String template,
			@NotNull Map<String, String> placeholders
	) {
		this.engine = Objects.requireNonNull(engine, "engine");
		this.template = Objects.requireNonNull(template, "template");
		this.placeholders = Map.copyOf(placeholders);
	}

	/**
	 * Returns a copy of this template with the provided placeholders.
	 *
	 * @param placeholders The placeholders to apply during rendering
	 * @return A configured template renderer
	 */
	@NotNull
	public MessageTemplate placeholders(@NotNull Map<String, String> placeholders) {
		return new MessageTemplate(engine, template, placeholders);
	}

	/**
	 * Renders the configured template into a string.
	 *
	 * @return The rendered string
	 */
	@NotNull
	public String render() {
		return engine.renderTemplate(template, placeholders);
	}

	/**
	 * Creates a stream-aware template renderer that renders the same template for every stream item.
	 *
	 * @param stream The stream of values to render
	 * @param <T> The stream item type
	 * @return A stream-aware template renderer
	 */
	@NotNull
	public <T> StreamMessageTemplate<T> stream(@NotNull Stream<T> stream) {
		return new StreamMessageTemplate<>(engine, template, placeholders, stream);
	}
}
