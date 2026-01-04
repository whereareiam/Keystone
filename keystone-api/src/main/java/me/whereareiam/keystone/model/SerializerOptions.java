package me.whereareiam.keystone.model;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Configuration options for creating a SerializerEngine.
 */
@SuppressWarnings("unused")
public final class SerializerOptions {
	private final String defaultAdapterId;
	private final Supplier<String> prefixSupplier;
	@Getter
	private final boolean enableLegacyColors;
	@Getter
	private final boolean enablePlayerNamePlaceholder;
	@Getter
	private final PlaceholderFormat placeholderFormat;

	private SerializerOptions(@NotNull Builder builder) {
		this.defaultAdapterId = builder.defaultAdapterId;
		this.prefixSupplier = builder.prefixSupplier;
		this.enableLegacyColors = builder.enableLegacyColors;
		this.enablePlayerNamePlaceholder = builder.enablePlayerNamePlaceholder;
		this.placeholderFormat = builder.placeholderFormat;
	}

	/**
	 * Gets the default adapter identifier to use.
	 *
	 * @return The adapter ID (e.g., "minimessage", "gson")
	 */
	@NotNull
	public String getDefaultAdapterId() {
		return defaultAdapterId;
	}

	/**
	 * Gets the prefix supplier for prefix placeholder injection.
	 *
	 * @return The prefix supplier, or null if prefix injection is disabled
	 */
	@Nullable
	public Supplier<String> getPrefixSupplier() {
		return prefixSupplier;
	}

	/**
	 * Creates a new builder for SerializerOptions.
	 *
	 * @return A new builder instance
	 */
	@NotNull
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Creates default options with MiniMessage adapter and prefix injection enabled.
	 *
	 * @param prefixSupplier Supplier for the prefix string
	 * @return Default SerializerOptions
	 */
	@NotNull
	public static SerializerOptions defaults(@NotNull Supplier<String> prefixSupplier) {
		return builder()
				.defaultAdapter("MINIMESSAGE")
				.prefixSupplier(prefixSupplier)
				.enableLegacyColors(true)
				.enablePlayerNamePlaceholder(true)
				.placeholderFormat(PlaceholderFormat.CURLY_BRACES)
				.build();
	}

	/**
	 * Builder for creating SerializerOptions instances.
	 */
	public static final class Builder {
		private String defaultAdapterId = "MINIMESSAGE";
		private Supplier<String> prefixSupplier;
		private boolean enableLegacyColors = true;
		private boolean enablePlayerNamePlaceholder = true;
		private PlaceholderFormat placeholderFormat = PlaceholderFormat.CURLY_BRACES;

		private Builder() {
		}

		/**
		 * Sets the default adapter identifier.
		 *
		 * @param adapterId The adapter ID (e.g., "MINIMESSAGE", "GSON", "PLAIN")
		 * @return This builder
		 */
		@NotNull
		public Builder defaultAdapter(@NotNull String adapterId) {
			this.defaultAdapterId = adapterId;
			return this;
		}

		/**
		 * Sets the prefix supplier for prefix placeholder injection.
		 *
		 * @param prefixSupplier Supplier that provides the prefix string
		 * @return This builder
		 */
		@NotNull
		public Builder prefixSupplier(@NotNull Supplier<String> prefixSupplier) {
			this.prefixSupplier = prefixSupplier;
			return this;
		}

		/**
		 * Disables prefix injection.
		 *
		 * @return This builder
		 */
		@NotNull
		public Builder disablePrefix() {
			this.prefixSupplier = null;
			return this;
		}

		/**
		 * Enables or disables legacy color code parsing.
		 *
		 * @param enable true to enable legacy colors
		 * @return This builder
		 */
		@NotNull
		public Builder enableLegacyColors(boolean enable) {
			this.enableLegacyColors = enable;
			return this;
		}

		/**
		 * Enables or disables automatic playerName placeholder injection.
		 *
		 * @param enable true to enable player name placeholder
		 * @return This builder
		 */
		@NotNull
		public Builder enablePlayerNamePlaceholder(boolean enable) {
			this.enablePlayerNamePlaceholder = enable;
			return this;
		}

		/**
		 * Sets the placeholder format to use for placeholder replacement.
		 * <p>
		 * Examples:
		 * <ul>
		 *   <li>CURLY_BRACES: {placeholder}</li>
		 *   <li>PERCENT: %placeholder%</li>
		 *   <li>DOLLAR: $placeholder</li>
		 * </ul>
		 *
		 * @param format The placeholder format
		 * @return This builder
		 */
		@NotNull
		public Builder placeholderFormat(@NotNull PlaceholderFormat format) {
			this.placeholderFormat = format;
			return this;
		}

		/**
		 * Builds the SerializerOptions instance.
		 *
		 * @return The built SerializerOptions
		 */
		@NotNull
		public SerializerOptions build() {
			return new SerializerOptions(this);
		}
	}

	/**
	 * Defines the format for placeholder replacement.
	 */
	public static class PlaceholderFormat {
		/**
		 * Curly braces format: {placeholder}
		 */
		public static final PlaceholderFormat CURLY_BRACES = new PlaceholderFormat("{", "}");

		/**
		 * Percent format: %placeholder%
		 */
		public static final PlaceholderFormat PERCENT = new PlaceholderFormat("%", "%");

		/**
		 * Dollar format: $placeholder
		 */
		public static final PlaceholderFormat DOLLAR = new PlaceholderFormat("$", "");

		private final String prefix;
		private final String suffix;

		private PlaceholderFormat(String prefix, String suffix) {
			this.prefix = prefix;
			this.suffix = suffix;
		}

		/**
		 * Gets the prefix for this placeholder format.
		 *
		 * @return The prefix string
		 */
		@NotNull
		public String getPrefix() {
			return prefix;
		}

		/**
		 * Gets the suffix for this placeholder format.
		 *
		 * @return The suffix string
		 */
		@NotNull
		public String getSuffix() {
			return suffix;
		}

		/**
		 * Formats a placeholder name with this format.
		 *
		 * @param placeholderName The placeholder name (e.g., "content")
		 * @return The formatted placeholder (e.g., "{content}", "$content", "%content%")
		 */
		@NotNull
		public String format(@NotNull String placeholderName) {
			return prefix + placeholderName + suffix;
		}

		/**
		 * Creates a custom placeholder format.
		 *
		 * @param prefix The prefix (e.g., "{{", "<", "$")
		 * @param suffix The suffix (e.g., "}}", ">", "")
		 * @return A custom PlaceholderFormat
		 */
		@NotNull
		public static PlaceholderFormat custom(@NotNull String prefix, @NotNull String suffix) {
			return new PlaceholderFormat(prefix, suffix);
		}
	}
}

