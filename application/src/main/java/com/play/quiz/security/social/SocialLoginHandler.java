package com.play.quiz.security.social;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.play.quiz.domain.Account;
import com.play.quiz.enums.UserRole;
import com.play.quiz.record.ManagedAccountInput;
import com.play.quiz.repository.UserRepository;
import com.play.quiz.security.jwt.JwtProvider;
import com.play.quiz.security.service.PQUserDetailsService;
import com.play.quiz.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Where a provider sends the player back to once they have said who they are. The account is the
 * one with that email, or a new one; either way the browser gets the same token a password login
 * gives, and the session the provider round trip needed is thrown away.
 *
 * <p>The token goes back in the fragment of the address (#token=…), which the browser never sends
 * to any server, so it stays out of access logs and referrers. /login/social on the site puts it
 * where the password login puts it.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class SocialLoginHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    // Why a sign-in was turned away, as /login/social?error=… explains it.
    public static final String NO_EMAIL = "no-email";
    public static final String UNVERIFIED = "unverified";
    public static final String BLOCKED = "blocked";
    public static final String FAILED = "failed";

    @Value("${application.domain.host.url}")
    private String domainHostUrl;

    private final UserService userService;
    private final UserRepository userRepository;
    private final PQUserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final SocialUserService socialUserService;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        endSession(request);
        OAuth2User player = (OAuth2User) authentication.getPrincipal();
        String email = player.getAttribute(SocialUserService.EMAIL);

        Optional<Account> existing = userRepository.findUserByEmail(email);
        if (existing.isPresent() && existing.get().isBlocked()) {
            log.info("Social sign-in refused for {}: account is blocked", email);
            redirect(response, "?error=" + BLOCKED);
            return;
        }
        // Somebody registered this address with a password and never confirmed it. It may not be
        // the person signing in now, so the provider's word does not hand them that account.
        if (existing.isPresent() && !existing.get().isEnabled()) {
            log.info("Social sign-in refused for {}: account registered with a password and never verified", email);
            redirect(response, "?error=" + UNVERIFIED);
            return;
        }
        if (existing.isEmpty()) {
            createAccount(email, player.getAttribute(SocialUserService.NAME));
        }
        String photo = player.getAttribute(SocialUserService.PHOTO);
        userService.fillSocialProfile(email, player.getAttribute(SocialUserService.BIRTHDAY),
                () -> photo == null ? null : socialUserService.download(photo));

        UserDetails user = userDetailsService.loadByEmail(email);
        String token = jwtProvider.generate(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        log.info("Signed in {} through {}", email, request.getRequestURI());
        redirect(response, "#token=" + token);
    }

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
                                        final AuthenticationException exception) throws IOException {
        endSession(request);
        String reason = exception instanceof OAuth2AuthenticationException oauth
                && NO_EMAIL.equals(oauth.getError().getErrorCode()) ? NO_EMAIL : FAILED;
        log.warn("Social sign-in refused ({}): {}", reason, exception.getMessage());
        redirect(response, "?error=" + reason);
    }

    // The provider already vouched for the address, so the account starts out confirmed. Its
    // password is random and never shown: the player signs in through the provider.
    private void createAccount(final String email, final String name) {
        char[] password = UUID.randomUUID().toString().toCharArray();
        userService.createAccount(new ManagedAccountInput(email, name, password, List.of(UserRole.ROLE_USER)));
        log.info("Created an account for {} from a social sign-in", email);
    }

    private void redirect(final HttpServletResponse response, final String suffix) throws IOException {
        response.sendRedirect(domainHostUrl + "/login/social" + suffix);
    }

    private static void endSession(final HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
