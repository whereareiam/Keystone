package me.whereareiam.keystone.common.serializer.adapter;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MiniMessageAdapterTest {
	private static final PlainTextComponentSerializer PLAIN_TEXT = PlainTextComponentSerializer.plainText();

	private final MiniMessageAdapter adapter = new MiniMessageAdapter();

	@Test
	void deserializePrefersMiniMessageForTaggedQrUrl() {
		String input = "<white>Open <click:open_url:'https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=otpauth%3A%2F%2Ftotp%2FIdentica'><green>[QR]</green></click></white>";

		assertEquals("Open [QR]", PLAIN_TEXT.serialize(adapter.deserialize(input, true)));
	}

	@Test
	void deserializePrefersMiniMessageForTaggedOtpAuthUri() {
		String input = "<gray>otpauth://totp/Identica:PlayerOne?secret=ABC123&issuer=Identica&digits=6</gray>";

		assertEquals(
				"otpauth://totp/Identica:PlayerOne?secret=ABC123&issuer=Identica&digits=6",
				PLAIN_TEXT.serialize(adapter.deserialize(input, true))
		);
	}

	@Test
	void deserializeStillSupportsLegacyWhenNoMiniMessageTagsArePresent() {
		assertEquals("Enabled", PLAIN_TEXT.serialize(adapter.deserialize("&aEnabled", true)));
	}

	@Test
	void deserializeDoesNotTreatGenericAnglePlaceholderAsMiniMessage() {
		assertEquals("Hello <player>", PLAIN_TEXT.serialize(adapter.deserialize("&aHello <player>", true)));
	}

	@Test
	void deserializeSupportsLegacyAndMiniMessageInSameInput() {
		String input = "&aHello <click:run_command:'/x'><white>[X]</white></click>";
		String serialized = adapter.serialize(adapter.deserialize(input, true));

		assertEquals("Hello [X]", PLAIN_TEXT.serialize(adapter.deserialize(input, true)));
		assertTrue(serialized.contains("<green>Hello "));
		assertTrue(serialized.contains("<click:run_command:'/x'>"));
	}
}
