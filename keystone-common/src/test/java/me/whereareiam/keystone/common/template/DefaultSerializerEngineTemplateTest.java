package me.whereareiam.keystone.common.template;

import me.whereareiam.keystone.common.serializer.DefaultSerializerEngine;
import me.whereareiam.keystone.model.SerializerContent;
import me.whereareiam.keystone.model.SerializerOptions;
import me.whereareiam.keystone.serializer.MessageDecorator;
import me.whereareiam.keystone.common.serializer.adapter.PlainAdapter;
import me.whereareiam.keystone.template.message.TemplateSection;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultSerializerEngineTemplateTest {
	@Test
	void templateRendersSingleMessageWithPlaceholders() {
		DefaultSerializerEngine engine = createEngine(SerializerOptions.builder()
				.defaultAdapter("plain")
				.build());

		assertEquals(
				"Hello Alex",
				engine.template("Hello {name}")
						.placeholders(Map.of("name", "Alex"))
						.render()
		);
	}

	@Test
	void templateStreamRendersEachItemJoinedByNewlines() {
		DefaultSerializerEngine engine = createEngine(SerializerOptions.builder()
				.defaultAdapter("plain")
				.build());

		String rendered = engine.template("<green>{name}</green> [{version}]")
				.stream(Stream.of(
						new TestModule("core", "1.0.0"),
						new TestModule("chat", "1.2.0")
				))
				.placeholders(module -> Map.of(
						"name", module.name(),
						"version", module.version()
				))
				.render();

		assertEquals("<green>core</green> [1.0.0]\n<green>chat</green> [1.2.0]", rendered);
	}

	@Test
	void templateRespectsConfiguredPlaceholderFormat() {
		DefaultSerializerEngine engine = createEngine(SerializerOptions.builder()
				.defaultAdapter("plain")
				.placeholderFormat(SerializerOptions.PlaceholderFormat.PERCENT)
				.build());

		assertEquals(
				"Hello Alex",
				engine.template("Hello %name%")
						.placeholders(Map.of("name", "Alex"))
						.render()
		);
	}

	@Test
	void templateAppliesDecoratorsBeforePlaceholderReplacement() {
		DefaultSerializerEngine engine = createEngine(SerializerOptions.builder()
				.defaultAdapter("plain")
				.build());
		engine.addDecorator(new StaticPlaceholderDecorator("prefix", "[Dev] "));

		assertEquals("[Dev] Modules", engine.template("{prefix}Modules").render());
	}

	@Test
	void templateSectionReplacesNamedSectionWhenPresent() {
		DefaultSerializerEngine engine = createEngine(SerializerOptions.builder()
				.defaultAdapter("plain")
				.build());

		assertEquals(
				"Modules for Alex\ncore\nchat",
				engine.template("Modules for {name}\n{entries}")
						.placeholders(Map.of("name", "Alex"))
						.section("entries", section -> section.lines(java.util.List.of("core", "chat")))
						.render()
		);
	}

	@Test
	void templateSectionAppendsWhenMissing() {
		DefaultSerializerEngine engine = createEngine(SerializerOptions.builder()
				.defaultAdapter("plain")
				.build());

		assertEquals(
				"Modules for Alex\ncore\nchat",
				engine.template("Modules for {name}")
						.placeholders(Map.of("name", "Alex"))
						.section("entries", section -> section
								.lines(java.util.List.of("core", "chat"))
								.onMissing(TemplateSection.MissingSectionPolicy.APPEND))
						.render()
		);
	}

	@Test
	void templateSectionUsesConfiguredPlaceholderFormat() {
		DefaultSerializerEngine engine = createEngine(SerializerOptions.builder()
				.defaultAdapter("plain")
				.placeholderFormat(SerializerOptions.PlaceholderFormat.PERCENT)
				.build());

		assertEquals(
				"Modules\ncore",
				engine.template("Modules\n%entries%")
						.section("entries", section -> section.text("core"))
						.render()
		);
	}

	@Test
	void templateSectionCanFailWhenMissing() {
		DefaultSerializerEngine engine = createEngine(SerializerOptions.builder()
				.defaultAdapter("plain")
				.build());

		assertThrows(
				IllegalStateException.class,
				() -> engine.template("Modules")
						.section("entries", section -> section
								.text("core")
								.onMissing(TemplateSection.MissingSectionPolicy.ERROR))
						.render()
		);
	}

	private DefaultSerializerEngine createEngine(SerializerOptions options) {
		DefaultSerializerEngine engine = new DefaultSerializerEngine(options);
		engine.registerAdapter(new PlainAdapter());
		return engine;
	}

	private record TestModule(String name, String version) {
	}

	private static final class StaticPlaceholderDecorator implements MessageDecorator {
		private final String key;
		private final String value;

		private StaticPlaceholderDecorator(String key, String value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public @NotNull SerializerContent decorate(SerializerContent content) {
			content.addPlaceholder(key, value);
			return content;
		}
	}
}
