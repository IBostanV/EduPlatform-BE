package com.play.quiz.security.social;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.Test;

// What a Facebook reply adds to the profile: only a whole birthday, and only a real photo.
class SocialUserServiceTest {

    @Test
    void only_a_full_birthday_is_kept() {
        assertEquals(LocalDate.of(1993, 4, 21), SocialUserService.facebookBirthday("04/21/1993"));
        assertNull(SocialUserService.facebookBirthday("04/21"));
        assertNull(SocialUserService.facebookBirthday("1993"));
        assertNull(SocialUserService.facebookBirthday(null));
    }

    @Test
    void the_silhouette_is_not_a_photo() {
        assertEquals("http://pic", SocialUserService.facebookPhoto(
                Map.of("data", Map.of("url", "http://pic", "is_silhouette", false))));
        assertNull(SocialUserService.facebookPhoto(
                Map.of("data", Map.of("url", "http://pic", "is_silhouette", true))));
        assertNull(SocialUserService.facebookPhoto(null));
    }
}
