package com.play.quiz.donation;

import java.util.List;
import java.util.Objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Where donations go, as the admin set it up and the /donate page shows it. Everything is
 * optional: the page shows only what is filled in.
 *
 * <p>The PayPal link and the addresses end up in an href and on the clipboard of whoever donates,
 * so they are held to a strict shape: an https link, and an address of letters, digits and the few
 * separators addresses use (a "javascript:" link or a pasted note cannot get through).
 */
public record DonationSettings(@Valid PayPal paypal, @Valid @Size(max = 10) List<Wallet> crypto) {

    public static final DonationSettings EMPTY = new DonationSettings(null, List.of());

    public DonationSettings {
        crypto = Objects.requireNonNullElse(crypto, List.of());
    }

    /** A PayPal account to send to by address, and/or a link (paypal.me or a donate button). */
    public record PayPal(@Email @Size(max = 254) String email,
                         @Pattern(regexp = "^https://[^\s\"'<>]+$", message = "The PayPal link must start with https://")
                         @Size(max = 300) String link) {
    }

    /** One crypto wallet: Bitcoin (BTC) on the Bitcoin network, USDT on ERC-20, and so on. */
    public record Wallet(@NotBlank @Size(max = 40) String name,
                         @Size(max = 12) String symbol,
                         @Size(max = 40) String network,
                         @NotBlank @Pattern(regexp = "^[A-Za-z0-9:._-]{10,130}$",
                                 message = "A wallet address is 10 to 130 letters and digits") String address) {
    }
}
