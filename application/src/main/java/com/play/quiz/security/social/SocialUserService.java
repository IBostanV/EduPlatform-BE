package com.play.quiz.security.social;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Reads who signed in from the provider and boils it down to the two things an account here is
 * made of: an email ({@link #EMAIL}) and a display name ({@link #NAME}).
 *
 * <p>The email has to be one the provider has verified. Accounts here are found by email, so an
 * unverified one would let anybody sign in to somebody else's account by typing their address
 * into a provider that does not check it.
 */
@Log4j2
@Component
public class SocialUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    public static final String EMAIL = "playquiz_email";
    public static final String NAME = "playquiz_name";
    /** Optional extras for the profile, Facebook only: a LocalDate and the address of a photo. */
    public static final String BIRTHDAY = "playquiz_birthday";
    public static final String PHOTO = "playquiz_photo";

    // Facebook gives "MM/dd/yyyy", or only part of it when the owner hides the rest.
    private static final DateTimeFormatter FACEBOOK_BIRTHDAY = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /** GitHub insists on a user agent. */
    public static final String USER_AGENT = "web:play-quiz:v1 (sign-in)";

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final RestTemplate restTemplate = new RestTemplate();

    public SocialUserService() {
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set(HttpHeaders.USER_AGENT, USER_AGENT);
            return execution.execute(request, body);
        });
        delegate.setRestOperations(restTemplate);
    }

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest request) {
        OAuth2User user = delegate.loadUser(request);
        Map<String, Object> attributes = new HashMap<>(user.getAttributes());
        String provider = request.getClientRegistration().getRegistrationId();

        attributes.put(EMAIL, switch (provider) {
            // Google says whether it checked the address.
            case SocialProviders.GOOGLE -> Boolean.TRUE.equals(attributes.get("email_verified"))
                    ? attributes.get("email") : null;
            case SocialProviders.GITHUB -> githubVerifiedEmail(request);
            // Facebook only hands out an address its owner has confirmed.
            case SocialProviders.FACEBOOK -> attributes.get("email");
            default -> null;
        });
        attributes.put(NAME, switch (provider) {
            case SocialProviders.GITHUB -> Objects.requireNonNullElse(attributes.get("name"), attributes.get("login"));
            default -> attributes.get("name");
        });

        if (SocialProviders.FACEBOOK.equals(provider)) {
            attributes.put(BIRTHDAY, facebookBirthday(attributes.get("birthday")));
            attributes.put(PHOTO, facebookPhoto(attributes.get("picture")));
        }

        if (attributes.get(EMAIL) == null) {
            log.info("Social sign-in through {} gave no verified email", provider);
            throw new OAuth2AuthenticationException(new OAuth2Error(SocialLoginHandler.NO_EMAIL));
        }
        String nameKey = request.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        return new DefaultOAuth2User(user.getAuthorities(), attributes, nameKey);
    }

    static LocalDate facebookBirthday(final Object birthday) {
        try {
            return birthday instanceof String text ? LocalDate.parse(text, FACEBOOK_BIRTHDAY) : null;
        } catch (DateTimeParseException partial) {
            return null;
        }
    }

    /** {"data": {"url": …, "is_silhouette": …}}; the silhouette is Facebook's stand-in, not a photo. */
    static String facebookPhoto(final Object picture) {
        if (picture instanceof Map<?, ?> wrapper && wrapper.get("data") instanceof Map<?, ?> data
                && !Boolean.TRUE.equals(data.get("is_silhouette")) && data.get("url") instanceof String url) {
            return url;
        }
        return null;
    }

    /** The photo at this address, or null when it cannot be had: a missing photo never stops a sign-in. */
    public byte[] download(final String url) {
        try {
            return restTemplate.getForObject(URI.create(url), byte[].class);
        } catch (RuntimeException failed) {
            // The message can quote the address, whose query string carries the provider's signature.
            log.warn("Profile photo download failed: {}",
                    String.valueOf(failed.getMessage()).replaceAll("\\?\\S*", ""));
            return null;
        }
    }

    /** The primary address, if GitHub has verified it; the one on the profile may be hidden. */
    private String githubVerifiedEmail(final OAuth2UserRequest request) {
        RequestEntity<Void> call = RequestEntity.method(HttpMethod.GET, "https://api.github.com/user/emails")
                .headers(headers -> headers.setBearerAuth(request.getAccessToken().getTokenValue()))
                .build();
        List<Map<String, Object>> emails;
        try {
            emails = restTemplate.exchange(call,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}).getBody();
        } catch (RuntimeException failed) {
            log.warn("GitHub email lookup failed: {}", failed.getMessage());
            throw failed;
        }
        return emails == null ? null : emails.stream()
                .filter(email -> Boolean.TRUE.equals(email.get("primary")) && Boolean.TRUE.equals(email.get("verified")))
                .map(email -> (String) email.get("email"))
                .findFirst().orElse(null);
    }
}
