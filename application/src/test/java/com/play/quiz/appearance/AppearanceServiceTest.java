package com.play.quiz.appearance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.quiz.domain.Account;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.User;

// A player's own look of the site: what is refused, and that a reset stores nothing at all.
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AppearanceServiceTest {

    private static final long ACCOUNT_ID = 7L;

    @Mock private UserService userService;
    @Mock private AuthenticationFacade authenticationFacade;
    @Mock private AccountRepository accountRepository;

    private AppearanceService service;
    // What the repository holds, so a save can be read back.
    private String stored;

    @BeforeEach
    void init() {
        // The application's own mapper leaves nulls out; so does this one.
        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        service = new AppearanceService(userService, authenticationFacade, accountRepository, mapper);

        when(authenticationFacade.getPrincipal()).thenReturn(new User("me@playquiz.io", "x", List.of()));
        when(userService.findByEmail("me@playquiz.io")).thenReturn(Account.builder().accountId(ACCOUNT_ID).build());
        when(accountRepository.findAppearance(ACCOUNT_ID)).thenAnswer(invocation -> stored);
        doAnswer(invocation -> stored = invocation.getArgument(1))
                .when(accountRepository).setAppearance(eq(ACCOUNT_ID), any());
    }

    @Test
    void given_settings_then_store_only_the_ones_changed_and_read_them_back() {
        Appearance mine = new Appearance("#ff6600", 110, Appearance.Motion.ON, true, Appearance.Side.RIGHT, null, "#000000");

        assertEquals(mine, service.save(mine));
        assertEquals("{\"accent\":\"#ff6600\",\"textSize\":110,\"motion\":\"ON\",\"compactNav\":true,"
                + "\"friendsDock\":\"RIGHT\",\"accentText\":\"#000000\"}", stored);
    }

    @Test
    void given_everything_reset_then_the_column_goes_back_to_empty() {
        stored = "{\"accent\":\"#ff6600\"}";

        assertEquals(Appearance.defaults(), service.save(Appearance.defaults()));
        verify(accountRepository).setAppearance(ACCOUNT_ID, null);
    }

    // The colour ends up in a style rule, so only the one exact shape gets through.
    @Test
    void given_something_that_is_not_a_colour_then_refuse_it() {
        Appearance sneaky = new Appearance("red; background: url(x)", null, null, null, null, null, null);
        Appearance sneakyText = new Appearance(null, null, null, null, null, null, "red; background: url(x)");

        assertThrows(IllegalArgumentException.class, () -> service.save(sneaky));
        assertThrows(IllegalArgumentException.class, () -> service.save(sneakyText));
        verify(accountRepository, never()).setAppearance(anyLong(), any());
    }

    @Test
    void given_a_text_size_that_is_not_offered_then_refuse_it() {
        assertThrows(IllegalArgumentException.class,
                () -> service.save(new Appearance(null, 300, null, null, null, null, null)));
    }

    // A stored value that no longer reads is the site as it comes, not an error on every page.
    @Test
    void given_an_unreadable_stored_value_then_read_the_defaults() {
        stored = "{\"friendsDock\":\"TOP\"}";

        assertEquals(Appearance.defaults(), service.get());
    }
}
