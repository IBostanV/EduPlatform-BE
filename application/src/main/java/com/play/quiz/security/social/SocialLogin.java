package com.play.quiz.security.social;

import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.endpoint.RestClientAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Component;

/**
 * Turns on "sign in with …" for the providers that have credentials; with none, nothing changes.
 *
 * <p>The round trip: the site links to /oauth2/authorization/{provider}, the provider sends the
 * player back to /login/oauth2/code/{provider}, and {@link SocialLoginHandler} takes it from there.
 * Remembering the request between the two (the state that stops a forged callback) needs a
 * session, the only one this otherwise stateless server keeps; the handler ends it.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class SocialLogin {

    private final SocialProviders providers;
    private final SocialUserService userService;
    private final SocialLoginHandler handler;

    @Value("${application.domain.host.url}")
    private String domainHostUrl;

    public void configure(final HttpSecurity http) throws Exception {
        if (providers.registrations().isEmpty()) {
            log.info("Social sign-in off: no provider has credentials");
            return;
        }
        log.info("Social sign-in on for {}", providers.ids());
        ClientRegistrationRepository repository = new InMemoryClientRegistrationRepository(providers.registrations());

        RestClientAuthorizationCodeTokenResponseClient tokens = new RestClientAuthorizationCodeTokenResponseClient();
        tokens.addHeadersConverter(grant -> {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.USER_AGENT, SocialUserService.USER_AGENT);
            return headers;
        });

        http.oauth2Login(login -> login
                .clientRegistrationRepository(repository)
                // The site's own page, not the one Spring would otherwise serve from this server.
                .loginPage(domainHostUrl + "/login")
                .tokenEndpoint(endpoint -> endpoint.accessTokenResponseClient(tokens))
                .userInfoEndpoint(endpoint -> endpoint.userService(userService))
                .successHandler(handler)
                .failureHandler(handler));
    }
}
