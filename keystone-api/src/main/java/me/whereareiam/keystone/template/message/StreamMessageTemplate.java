package me.whereareiam.keystone.template.message;

import me.whereareiam.keystone.template.TemplateEngine;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Fluent helper for rendering a template across a stream and joining the rendered lines with newlines.
 *
 * @param <T> The stream item type
 */
@SuppressWarnings("unused")
public final class StreamMessageTemplate<T> {
	private final TemplateEngine engine;
	private final String template;
	private final Map<String, String> basePlaceholders;
	private final Stream<T> stream;
	private final Function<T, Map<String, String>> placeholders;

	StreamMessageTemplate(
			@NotNull TemplateEngine engine,
			@NotNull String template,
			@NotNull Map<String, String> basePlaceholders,
			@NotNull Stream<T> stream
	) {
		this(engine, template, basePlaceholders, stream, value -> Map.of());
	}

	private StreamMessageTemplate(
			@NotNull TemplateEngine engine,
			@NotNull String template,
			@NotNull Map<String, String> basePlaceholders,
			@NotNull Stream<T> stream,
			@NotNull Function<T, Map<String, String>> placeholders
	) {
		this.engine = Objects.requireNonNull(engine, "engine");
		this.template = Objects.requireNonNull(template, "template");
		this.basePlaceholders = Map.copyOf(basePlaceholders);
		this.stream = Objects.requireNonNull(stream, "stream");
		this.placeholders = Objects.requireNonNull(placeholders, "placeholders");
	}

	/**
	 * Returns a copy of this stream template with a placeholder mapper for each stream item.
	 *
	 * @param placeholders Maps a stream item to its template placeholders
	 * @return A configured stream template renderer
	 */
	@NotNull
	public StreamMessageTemplate<T> placeholders(@NotNull Function<T, Map<String, String>> placeholders) {
		return new StreamMessageTemplate<>(engine, template, basePlaceholders, stream, placeholders);
	}

	/**
	 * Renders each stream item using the configured template and joins the rendered lines with newlines.
	 *
	 * @return The rendered multi-line string
	 */
	@NotNull
	public String render() {
		return stream
				.map(value -> {
					Map<String, String> resolvedPlaceholders = placeholders.apply(value);
					if (basePlaceholders.isEmpty()) return engine.renderTemplate(template, resolvedPlaceholders);
					if (resolvedPlaceholders.isEmpty()) return engine.renderTemplate(template, basePlaceholders);

					Map<String, String> mergedPlaceholders = new HashMap<>(basePlaceholders);
					mergedPlaceholders.putAll(resolvedPlaceholders);
					return engine.renderTemplate(template, mergedPlaceholders);
				})
				.collect(Collectors.joining("\n"));
	}
}
