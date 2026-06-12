package me.whereareiam.keystone.template.message;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Named template section content and fallback behavior used by {@link MessageTemplate}.
 */
@SuppressWarnings("unused")
public final class TemplateSection {
	private final String content;
	private final MissingSectionPolicy missingPolicy;

	private TemplateSection(@NotNull Builder builder) {
		this.content = Objects.requireNonNull(builder.content, "content");
		this.missingPolicy = Objects.requireNonNull(builder.missingPolicy, "missingPolicy");
	}

	@NotNull
	String content() {
		return content;
	}

	@NotNull
	MissingSectionPolicy missingPolicy() {
		return missingPolicy;
	}

	/**
	 * Creates a builder for configuring template section content and fallback behavior.
	 *
	 * @return A new section builder
	 */
	@NotNull
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for {@link TemplateSection}.
	 */
	public static final class Builder {
		private String content = "";
		private MissingSectionPolicy missingPolicy = MissingSectionPolicy.IGNORE;

		private Builder() {
		}

		/**
		 * Sets the section content as plain text.
		 *
		 * @param content Section content
		 * @return This builder
		 */
		@NotNull
		public Builder text(@NotNull String content) {
			this.content = Objects.requireNonNull(content, "content");
			return this;
		}

		/**
		 * Sets the section content from lines joined by newlines.
		 *
		 * @param lines Section lines
		 * @return This builder
		 */
		@NotNull
		public Builder lines(@NotNull List<String> lines) {
			Objects.requireNonNull(lines, "lines");
			this.content = String.join("\n", lines.stream()
					.map(line -> line == null ? "" : line)
					.toList());
			return this;
		}

		/**
		 * Sets the behavior used when the section token is missing from the template.
		 *
		 * @param missingPolicy Missing section policy
		 * @return This builder
		 */
		@NotNull
		public Builder onMissing(@NotNull MissingSectionPolicy missingPolicy) {
			this.missingPolicy = Objects.requireNonNull(missingPolicy, "missingPolicy");
			return this;
		}

		/**
		 * Builds the immutable section configuration.
		 *
		 * @return The configured template section
		 */
		@NotNull
		public TemplateSection build() {
			return new TemplateSection(this);
		}
	}

	/**
	 * Policy used when a configured section token is missing from the outer template.
	 */
	public enum MissingSectionPolicy {
		IGNORE,
		APPEND,
		ERROR
	}
}
