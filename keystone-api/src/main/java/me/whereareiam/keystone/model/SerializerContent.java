package me.whereareiam.keystone.model;

import me.whereareiam.keystone.Actor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the content to be serialized, containing message, actor context, and placeholders.
 * This is the data structure that flows through the serialization pipeline.
 */
@SuppressWarnings("unused")
	public final class SerializerContent {
    @Nullable
    private final Actor receiver;
    @Nullable
    private final String scope;
    private final Map<String, String> placeholders;
    private String message;

	private SerializerContent(@NotNull Builder builder) {
        this.receiver = builder.receiver;
        this.scope = builder.scope;
        this.placeholders = new HashMap<>(builder.placeholders);
        this.message = builder.message;
    }

	/**
	 * Gets the actor receiving this message.
	 *
	 * @return The receiver actor, or null if no actor is set
	 */
    @Nullable
    public Actor getReceiver() {
        return receiver;
    }

    /**
     * Gets the optional scope for this message (e.g., module or feature name).
     *
     * @return The scope string, or null if no scope is set
     */
    @Nullable
    public String getScope() {
        return scope;
    }

	/**
	 * Gets the message template to be serialized.
	 *
	 * @return The message template
	 */
	@NotNull
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message template.
	 *
	 * @param message The new message template
	 */
	public void setMessage(@NotNull String message) {
		this.message = message;
	}

	/**
	 * Gets all registered placeholders.
	 *
	 * @return A map of placeholder keys to their values
	 */
	@NotNull
	public Map<String, String> getPlaceholders() {
		return new HashMap<>(placeholders);
	}

	/**
	 * Adds a placeholder to be replaced during serialization.
	 *
	 * @param key   The placeholder key (e.g., "coins")
	 * @param value The value to replace it with
	 */
	public void addPlaceholder(@NotNull String key, @NotNull String value) {
		placeholders.put(key, value);
	}

	/**
	 * Gets a placeholder value by key.
	 *
	 * @param key The placeholder key
	 * @return The placeholder value, or null if not found
	 */
	@Nullable
	public String getPlaceholder(@NotNull String key) {
		return placeholders.get(key);
	}

	/**
	 * Creates a new builder for SerializerContent.
	 *
	 * @return A new builder instance
	 */
	@NotNull
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for creating SerializerContent instances.
	 */
    public static final class Builder {
        private Actor receiver;
        private String scope;
        private final Map<String, String> placeholders = new HashMap<>();
        private String message = "";

		private Builder() {
		}

		/**
		 * Sets the receiver actor.
		 *
		 * @param receiver The actor receiving the message (can be null)
		 * @return This builder
		 */
        @NotNull
        public Builder receiver(@Nullable Actor receiver) {
            this.receiver = receiver;
            return this;
        }

        /**
         * Sets the optional scope for this message (e.g., module or feature name).
         *
         * @param scope The scope identifier
         * @return This builder
         */
        @NotNull
        public Builder scope(@Nullable String scope) {
            this.scope = scope;
            return this;
        }

		/**
		 * Sets the message template.
		 *
		 * @param message The message template
		 * @return This builder
		 */
		@NotNull
		public Builder message(@NotNull String message) {
			this.message = message;
			return this;
		}

		/**
		 * Adds a placeholder to be replaced.
		 *
		 * @param key   The placeholder key (e.g., "coins")
		 * @param value The value to replace it with
		 * @return This builder
		 */
		@NotNull
		public Builder placeholder(@NotNull String key, @NotNull String value) {
			this.placeholders.put(key, value);
			return this;
		}

		/**
		 * Adds multiple placeholders at once.
		 *
		 * @param placeholders Map of placeholder keys to values
		 * @return This builder
		 */
		@NotNull
		public Builder placeholders(@NotNull Map<String, String> placeholders) {
			this.placeholders.putAll(placeholders);
			return this;
		}

		/**
		 * Builds the SerializerContent instance.
		 *
		 * @return The built SerializerContent
		 */
		@NotNull
		public SerializerContent build() {
			return new SerializerContent(this);
		}
	}
}

