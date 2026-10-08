package com.play.quiz.appearance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

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
    @Mock private UserBackgroundRepository backgroundRepository;

    private AppearanceService service;
    // What the repository holds, so a save can be read back.
    private String stored;

    @BeforeEach
    void init() {
        // The application's own mapper leaves nulls out; so does this one.
        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        service = new AppearanceService(userService, authenticationFacade, accountRepository, mapper, backgroundRepository);

        when(authenticationFacade.getPrincipal()).thenReturn(new User("me@playquiz.io", "x", List.of()));
        when(userService.findByEmail("me@playquiz.io")).thenReturn(Account.builder().accountId(ACCOUNT_ID).build());
        when(accountRepository.findAppearance(ACCOUNT_ID)).thenAnswer(invocation -> stored);
        doAnswer(invocation -> stored = invocation.getArgument(1))
                .when(accountRepository).setAppearance(eq(ACCOUNT_ID), any());
    }

    @Test
    void given_settings_then_store_only_the_ones_changed_and_read_them_back() {
        Appearance mine = new Appearance("#ff6600", 110, Appearance.Motion.ON, true, Appearance.Side.RIGHT, null, "#000000", null, null, null);

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
        Appearance sneaky = new Appearance("red; background: url(x)", null, null, null, null, null, null, null, null, null);
        Appearance sneakyText = new Appearance(null, null, null, null, null, null, "red; background: url(x)", null, null, null);

        assertThrows(IllegalArgumentException.class, () -> service.save(sneaky));
        assertThrows(IllegalArgumentException.class, () -> service.save(sneakyText));
        verify(accountRepository, never()).setAppearance(anyLong(), any());
    }

    @Test
    void given_a_text_size_that_is_not_offered_then_refuse_it() {
        assertThrows(IllegalArgumentException.class,
                () -> service.save(new Appearance(null, 300, null, null, null, null, null, null, null, null)));
    }

    @Test
    void given_a_background_then_only_the_sites_own_or_an_uploaded_one() {
        Appearance aurora = Appearance.defaults().withBackground("aurora");
        assertEquals(aurora, service.save(aurora));

        assertThrows(IllegalArgumentException.class,
                () -> service.save(Appearance.defaults().withBackground("../../etc/passwd")));
        // Custom with nothing uploaded.
        assertThrows(IllegalArgumentException.class,
                () -> service.save(Appearance.defaults().withBackground(Appearance.CUSTOM)));
    }

    // Known by its first bytes, whatever it claims to be; an SVG could carry a script.
    @Test
    void given_an_upload_then_only_a_jpeg_png_or_webp_under_the_limit() {
        byte[] svg = "<svg xmlns='http://www.w3.org/2000/svg'><script>alert(1)</script></svg>".getBytes();
        byte[] tooBig = new byte[AppearanceService.MAX_BACKGROUND_BYTES + 1];
        tooBig[0] = (byte) 0xFF;
        tooBig[1] = (byte) 0xD8;
        tooBig[2] = (byte) 0xFF;

        assertThrows(IllegalArgumentException.class, () -> service.uploadBackground(svg));
        assertThrows(IllegalArgumentException.class, () -> service.uploadBackground(tooBig));
        assertThrows(IllegalArgumentException.class, () -> service.uploadBackground(new byte[0]));
        verify(backgroundRepository, never()).save(any());
    }

    @Test
    void given_a_webp_upload_then_store_it_and_use_it() {
        byte[] webp = {'R', 'I', 'F', 'F', 0, 0, 0, 0, 'W', 'E', 'B', 'P', 'V', 'P', '8', ' '};
        when(backgroundRepository.findPublicId(ACCOUNT_ID)).thenReturn(Optional.of("abc"));

        Appearance result = service.uploadBackground(webp);

        verify(backgroundRepository).save(argThat(saved -> "image/webp".equals(saved.getContentType())));
        assertEquals(Appearance.CUSTOM, result.background());
        assertEquals("abc", result.customBackground());
    }

    // A stored value that no longer reads is the site as it comes, not an error on every page.
    @Test
    void given_an_unreadable_stored_value_then_read_the_defaults() {
        stored = "{\"friendsDock\":\"TOP\"}";

        assertEquals(Appearance.defaults(), service.get());
    }
}
