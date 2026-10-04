package com.play.quiz.util;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * What a new password must be: at least 8 characters, from at least 3 of the 4 kinds (lowercase,
 * uppercase, digits, symbols), and not a common word or the player's own email name dressed up
 * ("Password1!", "P@ssw0rd", "Welcome2024"). The registration page shows the same rules as its
 * strength meter (utils/password-strength.js, which keeps its own copy of the word list); this is
 * the check that counts.
 *
 * <p>Applied where a password is chosen — registering, changing it — never at login, so an
 * account made before the rule keeps working.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PasswordPolicy {

    public static final int MIN_LENGTH = 8;
    public static final int MIN_KINDS = 3;

    /**
     * The words the passwords people guess first are built on. Not a leaked-password list: most of
     * those ("123456", "qwerty") already fail the rules above. What gets past them is one of these
     * with a capital, a number or a symbol added, which {@link #isCommon} sees through.
     */
    static final Set<String> COMMON_WORDS = Set.of(
            "password", "passwd", "passw", "pass", "parola", "parol", "passwort", "motdepasse", "contrasena",
            "qwerty", "qwertyuiop", "qwertz", "azerty", "asdf", "asdfgh", "asdfghjkl", "zxcvbn", "zxcvbnm",
            "qazwsx", "abc", "abcd", "abcde", "abcdef", "abcdefg", "abcdefgh",
            "welcome", "hello", "admin", "administrator", "root", "user", "guest", "test", "testing", "demo",
            "login", "letmein", "secret", "access", "changeme", "default", "master", "whatever", "nothing",
            "iloveyou", "love", "lovely", "loveme", "trustno", "trustnoone", "freedom", "sunshine", "princess",
            "monkey", "dragon", "shadow", "superman", "batman", "spiderman", "starwars", "pokemon", "matrix",
            "football", "baseball", "soccer", "hockey", "basketball", "liverpool", "chelsea", "arsenal", "barcelona",
            "michael", "jennifer", "jordan", "charlie", "daniel", "andrew", "thomas", "robert", "jessica", "ashley",
            "hunter", "killer", "ninja", "mustang", "harley", "ranger", "tigger", "buster", "maggie", "ginger",
            "pepper", "cookie", "cheese", "banana", "orange", "purple", "silver", "flower", "summer", "winter",
            "spring", "autumn", "computer", "internet", "google", "facebook", "samsung", "apple", "iphone",
            "playquiz", "quiz", "player", "game", "gamer", "moldova", "romania", "bucuresti", "chisinau");

    // Characters written in place of letters: p@ssw0rd.
    private static final Map<Character, Character> LEET = Map.of(
            '@', 'a', '4', 'a', '3', 'e', '0', 'o', '$', 's', '5', 's', '7', 't', '!', 'i', '8', 'b');

    public static boolean isStrong(final char[] password) {
        if (Objects.isNull(password) || password.length < MIN_LENGTH) {
            return false;
        }
        boolean lower = false, upper = false, digit = false, symbol = false;
        for (char c : password) {
            if (Character.isLowerCase(c)) lower = true;
            else if (Character.isUpperCase(c)) upper = true;
            else if (Character.isDigit(c)) digit = true;
            else if (!Character.isWhitespace(c)) symbol = true;
        }
        int kinds = (lower ? 1 : 0) + (upper ? 1 : 0) + (digit ? 1 : 0) + (symbol ? 1 : 0);
        return kinds >= MIN_KINDS;
    }

    /**
     * Whether the password is a common word, or the name in the player's own email, with the usual
     * dressing: numbers and symbols around it, a capital, letters swapped for look-alikes.
     */
    public static boolean isCommon(final char[] password, final String email) {
        if (Objects.isNull(password)) {
            return false;
        }
        String emailName = Objects.isNull(email) ? "" : email.split("@")[0].toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
        for (String core : cores(new String(password).toLowerCase(Locale.ROOT))) {
            if (COMMON_WORDS.contains(core) || (emailName.length() >= 3 && core.equals(emailName))) {
                return true;
            }
        }
        return false;
    }

    // The word inside: with digits and symbols trimmed from both ends, or only digits from the front
    // ("@dmin1" is admin), or only from the end ("1l0veyou" is iloveyou), and look-alikes turned
    // back into letters, "1" read as both i and l.
    private static Set<String> cores(final String password) {
        Set<String> cores = new HashSet<>();
        // A list, not Set.of: the two trims are often the same, which Set.of refuses.
        for (String trimmed : List.of(password.replaceAll("^[^\\p{L}]+|[^\\p{L}]+$", ""),
                password.replaceAll("^\\d+|[^\\p{L}]+$", ""),
                password.replaceAll("[^\\p{L}]+$", ""))) {
            StringBuilder plain = new StringBuilder();
            for (char c : trimmed.toCharArray()) {
                plain.append(LEET.getOrDefault(c, c));
            }
            cores.add(plain.toString().replace('1', 'i'));
            cores.add(plain.toString().replace('1', 'l'));
        }
        return cores;
    }

    public static void requireStrong(final char[] password, final String email) {
        if (!isStrong(password)) {
            throw new IllegalArgumentException(ServerText.t("err_password_weak", "The password must be at least {{min}} characters and mix at least {{kinds}} of: lowercase, uppercase, digits, symbols",
                    "min", MIN_LENGTH, "kinds", MIN_KINDS));
        }
        if (isCommon(password, email)) {
            throw new IllegalArgumentException(ServerText.t("err_password_common", "That password is too common and easy to guess: choose another"));
        }
    }
}
