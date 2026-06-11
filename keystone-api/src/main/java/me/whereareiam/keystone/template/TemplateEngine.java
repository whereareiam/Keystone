package me.whereareiam.keystone.template;

import me.whereareiam.keystone.model.SerializerContent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Renders string templates using serializer content and placeholder resolution without deserializing to components.
 */
@SuppressWarnings("unused")
public interface TemplateEngine {
	/**
	 * Renders a template into a plain string using the serializer pipeline up to placeholder resolution.
	 * This applies decorators and placeholder replacement, but does not deserialize the result into a component.
	 *
	 * @param content The template content containing message, actor, and placeholders
	 * @return The rendered string template
	 */
	@NotNull
	String renderTemplate(@NotNull SerializerContent content);

	/**
	 * Renders a template without requiring a receiver actor.
	 *
	 * @param template The template message
	 * @return The rendered string
	 */
	@NotNull
	default String renderTemplate(@NotNull String template) {
		return renderTemplate(SerializerContent.builder()
				.message(template)
				.build());
	}

	/**
	 * Renders a template with multiple placeholders.
	 *
	 * @param template The template message
	 * @param placeholders The placeholders to apply
	 * @return The rendered string
	 */
	@NotNull
	default String renderTemplate(@NotNull String template, @NotNull Map<String, String> placeholders) {
		return renderTemplate(SerializerContent.builder()
				.message(template)
				.placeholders(placeholders)
				.build());
	}

	/**
	 * Renders a template with custom content configuration.
	 *
	 * @param template The template message
	 * @param customizer Consumer to customize the SerializerContent builder
	 * @return The rendered string
	 */
	@NotNull
	default String renderTemplate(@NotNull String template, @NotNull Consumer<SerializerContent.Builder> customizer) {
		SerializerContent.Builder builder = SerializerContent.builder()
				.message(template);

		customizer.accept(builder);
		return renderTemplate(builder.build());
	}

	/**
	 * Creates a fluent template renderer for the provided template string.
	 *
	 * @param template The template message
	 * @return A fluent template renderer
	 */
	@NotNull
	default MessageTemplate template(@NotNull String template) {
		return new MessageTemplate(this, template);
	}
}
