package com.play.quiz.security.jwt;

import com.play.quiz.config.RequestLoggingFilter;
import com.play.quiz.controller.RestEndpoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_LANGUAGE;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_TRANSLATION;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${application.security.jwt.token.prefix:Bearer}")
    private String jwtTokenPrefix;

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    private static final String ACTUATOR_PATH = "/actuator";
    private static final String ACTIVATE_ACCOUNT_LINK = "/activate-account";

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        return (!request.getRequestURI().startsWith(RestEndpoint.CONTEXT_PATH)
                || startsWith(request.getRequestURI())
                || request.getRequestURI().endsWith(ACTIVATE_ACCOUNT_LINK))
                && !request.getRequestURI().startsWith(ACTUATOR_PATH);
    }

    // Paths that never read the user. Category and knowledge base are NOT here any more: their
    // reads are public but their writes are admin-only, and a role can only be checked if the
    // token has been read.
    private boolean startsWith(String requestURI) {
        // Quiz is NOT here: /quiz/custom needs its creator, so the token must be read there.
        return Stream.of(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_LANGUAGE,
                        RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_TRANSLATION)
                .anyMatch(requestURI::startsWith);
    }

    // Authenticates when a valid token comes with the request, and otherwise lets it through
    // anonymously: the authorization rules in WebSecurity then decide. So a public read works
    // without a token, or with a stale one, while a protected endpoint answers 403 instead of
    // failing with an exception on the missing token.
    @Override
    protected void doFilterInternal(final @NotNull HttpServletRequest request,
                                    final @NotNull HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String token = parseRequest(request);
        if (token != null) {
            try {
                final String emailAsUsername = jwtProvider.getUsernameFromToken(token);
                final UserDetails userDetails = userDetailsService.loadUserByUsername(emailAsUsername);
                // Blocked since the token was handed out: the token is still signed and still
                // in date, so this has to be checked here and not only at login.
                if (!userDetails.isEnabled()) {
                    throw new DisabledException("Account is blocked: " + emailAsUsername);
                }
                SecurityContextHolder.getContext().setAuthentication(createAuthentication(request, userDetails));
                ThreadContext.put(RequestLoggingFilter.USER, emailAsUsername);
            } catch (RuntimeException exception) {
                log.info("Ignoring unusable token for {}: {}", request.getRequestURI(), exception.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

    private Authentication createAuthentication(final HttpServletRequest request, final UserDetails userDetails) {
        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    private String parseRequest(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(jwtTokenPrefix))
                .map(header -> header.replace(jwtTokenPrefix, ""))
                .orElse(parseCookie(request));
    }

    private static String parseCookie(final HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .map(Arrays::stream)
                .map(JwtAuthenticationFilter::getCookie)
                .map(optionalCookie -> optionalCookie.map(Cookie::getValue))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElse(null);
    }

    private static Optional<Cookie> getCookie(final Stream<Cookie> cookieStream) {
        return cookieStream
                .filter(cookie -> Objects.equals(cookie.getName(), HttpHeaders.AUTHORIZATION.toLowerCase()))
                .findFirst();
    }
}
