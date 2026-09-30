package com.play.quiz.record;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.play.quiz.domain.Account;
import org.junit.jupiter.api.Test;

/** Who other players see, and where their picture travels with them. */
class UserSummaryTest {

    private static final Account ACCOUNT = Account.builder()
            .accountId(4L)
            .username("Ion")
            .email("ion@playquiz.com")
            .avatar(new byte[]{1, 2, 3})
            .build();

    @Test
    void given_the_friends_list_when_summarising_then_carry_the_picture() {
        UserSummary summary = UserSummary.withPhoto(ACCOUNT);

        assertEquals("Ion", summary.displayName());
        assertArrayEquals(new byte[]{1, 2, 3}, summary.photo());
    }

    @Test
    void given_a_picker_or_a_longer_list_when_summarising_then_leave_the_picture_out() {
        // Those lists show names, not faces, and can be long.
        assertNull(UserSummary.of(ACCOUNT).photo());
    }

    @Test
    void given_an_account_with_no_username_when_summarising_then_fall_back_to_a_name() {
        Account noUsername = Account.builder().accountId(9L).name("Ana").surname("Pop").build();

        assertEquals("Ana Pop", UserSummary.of(noUsername).displayName());
        assertEquals("Player #9", UserSummary.of(Account.builder().accountId(9L).build()).displayName());
    }
}
