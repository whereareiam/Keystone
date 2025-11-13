package me.whereareiam.keystone.model;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.UUID;

/**
 * Base interface for any entity that can act and interact in the system.
 * This includes players, console, command blocks, and other sources.
 * <p>
 * Provides common functionality for identification, messaging, permissions, and localization.
 */
@SuppressWarnings("unused")
public interface Actor {
	/**
	 * Gets the unique identifier for this actor.
	 *
	 * @return The actor's UUID
	 */
	@NotNull
	UUID getUniqueId();

	/**
	 * Gets the username of this actor.
	 *
	 * @return The actor's username or identifier
	 */
	@NotNull
	String getUsername();

	/**
	 * Sends a message to this actor.
	 *
	 * @param message The message to send
	 */
	void sendMessage(@NotNull Component message);

	/**
	 * Checks if this actor has a specific permission.
	 *
	 * @param permission The permission to check
	 * @return true if the actor has the permission
	 */
	boolean hasPermission(@NotNull String permission);

	/**
	 * Gets the actor's preferred locale.
	 *
	 * @return The actor's locale
	 */
	@NotNull
	Locale getLocale();

	/**
	 * Gets the underlying Adventure Audience for this actor.
	 * Used for integration with Adventure API and messaging frameworks.
	 *
	 * @return The Adventure audience
	 */
	@NotNull
	Audience getAudience();
}