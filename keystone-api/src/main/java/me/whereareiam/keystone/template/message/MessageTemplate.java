package me.whereareiam.keystone.template.message;

import me.whereareiam.keystone.template.TemplateEngine;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Fluent helper for rendering a single string template or mapping a template across a stream.
 */
@SuppressWarnings("unused")
public final class MessageTemplate {
	private final TemplateEngine engine;
	private final String template;
	private final Map<String, String> placeholders;
	private final Map<String, TemplateSection> sections;

	public MessageTemplate(
            @NotNull TemplateEngine engine,
            @NotNull String template
    ) {
		this(engine, template, Map.of(), Map.of());
	}

	private MessageTemplate(
			@NotNull TemplateEngine engine,
			@NotNull String template,
			@NotNull Map<String, String> placeholders,
			@NotNull Map<String, TemplateSection> sections
	) {
		this.engine = Objects.requireNonNull(engine, "engine");
		this.template = Objects.requireNonNull(template, "template");
		this.placeholders = Map.copyOf(placeholders);
		this.sections = Map.copyOf(sections);
	}

	/**
	 * Returns a copy of this template with the provided placeholders.
	 *
	 * @param placeholders The placeholders to apply during rendering
	 * @return A configured template renderer
	 */
	@NotNull
	public MessageTemplate placeholders(@NotNull Map<String, String> placeholders) {
		return new MessageTemplate(engine, template, placeholders, sections);
	}

	/**
	 * Returns a copy of this template with a configured named section.
	 *
	 * @param name Section placeholder name without delimiters
	 * @param configurer Section configuration callback
	 * @return A configured template renderer
	 */
	@NotNull
	public MessageTemplate section(
			@NotNull String name,
			@NotNull Consumer<TemplateSection.Builder> configurer
	) {
		Objects.requireNonNull(name, "name");
		Objects.requireNonNull(configurer, "configurer");

		TemplateSection.Builder builder = TemplateSection.builder();
		configurer.accept(builder);

		Map<String, TemplateSection> configuredSections = new LinkedHashMap<>(sections);
		configuredSections.put(name, builder.build());
		return new MessageTemplate(engine, template, placeholders, configuredSections);
	}

	/**
	 * Renders the configured template into a string.
	 *
	 * @return The rendered string
	 */
	@NotNull
	public String render() {
		String rendered = engine.renderTemplate(template, placeholders);

		for (Map.Entry<String, TemplateSection> entry : sections.entrySet()) {
			String sectionToken = engine.placeholder(entry.getKey());
			String sectionContent = renderSection(entry.getValue());
			if (rendered.contains(sectionToken)) {
				rendered = rendered.replace(sectionToken, sectionContent);
				continue;
			}

			switch (entry.getValue().missingPolicy()) {
				case IGNORE -> {
				}
				case APPEND -> {
					if (!sectionContent.isEmpty())
						rendered = rendered.isEmpty() ? sectionContent : rendered + "\n" + sectionContent;
				}
				case ERROR -> throw new IllegalStateException("Missing template section token: " + sectionToken);
			}
		}

		return rendered;
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

	@NotNull
	private String renderSection(@NotNull TemplateSection section) {
		String content = section.content();
		if (content.isEmpty() || placeholders.isEmpty()) return content;
		return engine.renderTemplate(content, placeholders);
	}
}
