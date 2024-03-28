package com.play.quiz.security.jwt;

import com.play.quiz.controller.RestEndpoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_CATEGORY;
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
                || request.getRequestURI().startsWith(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_CATEGORY)
                || request.getRequestURI().startsWith(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_TRANSLATION)
                || request.getRequestURI().startsWith(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_LANGUAGE)
                || request.getRequestURI().endsWith(ACTIVATE_ACCOUNT_LINK))
                && !request.getRequestURI().startsWith(ACTUATOR_PATH);
    }

    @Override
    protected void doFilterInternal(final @NotNull HttpServletRequest request,
                                    final @NotNull HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final @NonNull String token = parseRequest(request);
        final @NonNull String emailAsUsername = jwtProvider.getUsernameFromToken(token);
        final @NonNull UserDetails userDetails = userDetailsService.loadUserByUsername(emailAsUsername);

        SecurityContextHolder.getContext().setAuthentication(createAuthentication(request, userDetails));
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
