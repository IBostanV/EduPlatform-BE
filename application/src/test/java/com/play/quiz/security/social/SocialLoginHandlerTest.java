package com.play.quiz.security.social;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.play.quiz.domain.Account;
import com.play.quiz.repository.UserRepository;
import com.play.quiz.security.jwt.JwtProvider;
import com.play.quiz.security.service.PQUserDetailsService;
import com.play.quiz.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.test.util.ReflectionTestUtils;

// Who a provider sign-in lets in: a confirmed or new account yes; a blocked one, or one somebody
// registered with a password and never confirmed, no.
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SocialLoginHandlerTest {

    private static final String EMAIL = "me@playquiz.io";
    private static final String SITE = "http://site";

    @Mock private UserService userService;
    @Mock private UserRepository userRepository;
    @Mock private PQUserDetailsService userDetailsService;
    @Mock private JwtProvider jwtProvider;
    @InjectMocks private SocialLoginHandler handler;

    private final MockHttpServletResponse response = new MockHttpServletResponse();

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(handler, "domainHostUrl", SITE);
        when(userDetailsService.loadByEmail(EMAIL)).thenReturn(new User(EMAIL, "x", List.of()));
        when(jwtProvider.generate(any())).thenReturn("jwt");
    }

    private void signIn() throws Exception {
        DefaultOAuth2User player = new DefaultOAuth2User(List.of(),
                Map.of("sub", "1", SocialUserService.EMAIL, EMAIL, SocialUserService.NAME, "Me"), "sub");
        handler.onAuthenticationSuccess(new MockHttpServletRequest(), response, new TestingAuthenticationToken(player, null));
    }

    private void existing(final boolean enabled, final boolean blocked) {
        Account account = Account.builder().email(EMAIL).build();
        account.setIsEnabled(enabled);
        account.setIsBlocked(blocked);
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(account));
    }

    @Test
    void given_a_confirmed_account_then_it_is_signed_in() throws Exception {
        existing(true, false);
        signIn();
        assertEquals(SITE + "/login/social#token=jwt", response.getRedirectedUrl());
        verify(userService, never()).createAccount(any());
    }

    @Test
    void given_no_account_then_one_is_made_and_signed_in() throws Exception {
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());
        signIn();
        verify(userService).createAccount(any());
        assertEquals(SITE + "/login/social#token=jwt", response.getRedirectedUrl());
    }

    @Test
    void given_an_unconfirmed_account_then_the_provider_does_not_hand_it_over() throws Exception {
        existing(false, false);
        signIn();
        assertEquals(SITE + "/login/social?error=unverified", response.getRedirectedUrl());
        verify(jwtProvider, never()).generate(any());
    }

    @Test
    void given_a_blocked_account_then_it_is_refused() throws Exception {
        existing(true, true);
        signIn();
        assertEquals(SITE + "/login/social?error=blocked", response.getRedirectedUrl());
        verify(jwtProvider, never()).generate(any());
    }

    @Test
    void given_no_verified_email_then_the_page_says_so() throws Exception {
        handler.onAuthenticationFailure(new MockHttpServletRequest(), response,
                new OAuth2AuthenticationException(new OAuth2Error(SocialLoginHandler.NO_EMAIL)));
        assertEquals(SITE + "/login/social?error=no-email", response.getRedirectedUrl());
    }
}
