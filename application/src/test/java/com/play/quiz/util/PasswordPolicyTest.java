package com.play.quiz.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

// At least 8 characters from at least 3 of the 4 kinds, and not a common word dressed up. Kept in
// step with the page's meter (utils/password-strength.js).
class PasswordPolicyTest {

    private static boolean strong(final String password) {
        return PasswordPolicy.isStrong(password.toCharArray());
    }

    @Test
    void given_three_kinds_and_eight_characters_then_it_is_strong() {
        assertTrue(strong("Abcdefg1"));
        assertTrue(strong("abcdef1!"));
        assertTrue(strong("Aa1!Aa1!"));
    }

    @Test
    void given_too_short_then_it_is_not_strong_whatever_it_mixes() {
        assertFalse(strong("Aa1!Aa1"));
        assertFalse(strong(""));
        assertFalse(PasswordPolicy.isStrong(null));
    }

    @Test
    void given_two_kinds_then_length_alone_does_not_make_it_strong() {
        assertFalse(strong("abcdefghijklmnop"));
        assertFalse(strong("abcdefgh12345678"));
    }

    @Test
    void given_a_weak_password_then_it_is_refused() {
        assertThrows(IllegalArgumentException.class, () -> PasswordPolicy.requireStrong("password".toCharArray(), null));
    }

    private static boolean common(final String password) {
        return PasswordPolicy.isCommon(password.toCharArray(), "ivan.b@mail.com");
    }

    @Test
    void given_a_common_word_dressed_up_then_it_is_common() {
        assertTrue(common("Password1"));
        assertTrue(common("P@ssw0rd!"));
        assertTrue(common("Pa$$word2024"));
        assertTrue(common("Welcome2024!"));
        assertTrue(common("!Qwerty123"));
        assertTrue(common("@dmin123"));
        assertTrue(common("1L0veY0u!"));
    }

    @Test
    void given_the_email_name_dressed_up_then_it_is_common() {
        assertTrue(common("Ivanb1993!"));
    }

    @Test
    void given_an_ordinary_strong_password_then_it_is_not_common() {
        assertFalse(common("Kettle9-blue"));
        assertFalse(common("Mango!Tree42"));
        assertFalse(common("MyPassword-is-long1"));
    }

    @Test
    void given_a_strong_but_common_password_then_it_is_refused() {
        assertTrue(PasswordPolicy.isStrong("P@ssw0rd!".toCharArray()));
        assertThrows(IllegalArgumentException.class, () -> PasswordPolicy.requireStrong("P@ssw0rd!".toCharArray(), null));
    }
}
