package com.play.quiz.security.social;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * The sign-in providers this server has credentials for. The keys come from the environment
 * (application.yml), never from a committed file, and a provider without them is simply left
 * out: the sign-in page only offers what is here, and a server with none starts as it always did.
 */
@Component
public class SocialProviders {

    public static final String GOOGLE = "google";
    public static final String GITHUB = "github";
    public static final String FACEBOOK = "facebook";

    private final List<ClientRegistration> registrations = new ArrayList<>();

    public SocialProviders(@Value("${application.security.social.google.client-id:}") final String googleId,
                           @Value("${application.security.social.google.client-secret:}") final String googleSecret,
                           @Value("${application.security.social.github.client-id:}") final String githubId,
                           @Value("${application.security.social.github.client-secret:}") final String githubSecret,
                           @Value("${application.security.social.facebook.client-id:}") final String facebookId,
                           @Value("${application.security.social.facebook.client-secret:}") final String facebookSecret) {
        if (configured(googleId, googleSecret)) {
            // Without "openid": that would route Google through Spring's OpenID Connect user service,
            // past SocialUserService and its verified-email check. The userinfo reply still says
            // email_verified.
            registrations.add(CommonOAuth2Provider.GOOGLE.getBuilder(GOOGLE)
                    .clientId(googleId).clientSecret(googleSecret).scope("profile", "email").build());
        }
        if (configured(githubId, githubSecret)) {
            // user:email: the address on the profile may be hidden, the verified list is not.
            registrations.add(CommonOAuth2Provider.GITHUB.getBuilder(GITHUB)
                    .clientId(githubId).clientSecret(githubSecret).scope("read:user", "user:email").build());
        }
        if (configured(facebookId, facebookSecret)) {
            // Spring's preset is pinned to Graph API v2.8, long retired; the unversioned addresses
            // follow whatever version the Facebook app is set to. Birthday and photo fill in a
            // new player's profile (SocialLoginHandler).
            registrations.add(CommonOAuth2Provider.FACEBOOK.getBuilder(FACEBOOK)
                    .clientId(facebookId).clientSecret(facebookSecret)
                    .scope("public_profile", "email", "user_birthday", "user_age_range")
                    .authorizationUri("https://www.facebook.com/dialog/oauth")
                    .tokenUri("https://graph.facebook.com/oauth/access_token")
                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,birthday,age_range,picture.type(large)")
                    .build());
        }
    }

    private static boolean configured(final String id, final String secret) {
        return StringUtils.hasText(id) && StringUtils.hasText(secret);
    }

    public List<ClientRegistration> registrations() {
        return registrations;
    }

    /** What the sign-in page offers, by registration id. */
    public List<String> ids() {
        return registrations.stream().map(ClientRegistration::getRegistrationId).toList();
    }
}
