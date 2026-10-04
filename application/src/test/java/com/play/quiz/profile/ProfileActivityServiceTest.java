package com.play.quiz.profile;

import java.util.List;

import com.play.quiz.domain.Account;
import com.play.quiz.enums.ProfileVisibility;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProfileActivityServiceTest {

    private static final Long OWNER = 1L;
    private static final Long FRIEND = 2L;
    private static final Long STRANGER = 3L;
    private static final List<Account> FRIENDS = List.of(Account.builder().accountId(FRIEND).build());

    @Test
    void given_each_setting_when_viewing_then_only_the_chosen_people_see_the_activity() {
        assertTrue(ProfileActivityService.mayView(ProfileVisibility.PUBLIC, OWNER, STRANGER, FRIENDS));

        assertTrue(ProfileActivityService.mayView(ProfileVisibility.FRIENDS, OWNER, FRIEND, FRIENDS));
        assertFalse(ProfileActivityService.mayView(ProfileVisibility.FRIENDS, OWNER, STRANGER, FRIENDS));

        assertFalse(ProfileActivityService.mayView(ProfileVisibility.PRIVATE, OWNER, FRIEND, FRIENDS));
        // Always their own.
        assertTrue(ProfileActivityService.mayView(ProfileVisibility.PRIVATE, OWNER, OWNER, FRIENDS));
    }
}
