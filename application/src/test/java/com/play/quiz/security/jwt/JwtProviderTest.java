package com.play.quiz.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.List;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.play.quiz.exception.TokenProcessException;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

// A token is good for its hour and no longer, however well it is signed.
class JwtProviderTest {

    private static final String SECRET = "a-test-secret-long-enough-for-hs256-signing";

    private final JwtProvider provider = new JwtProvider();

    JwtProviderTest() {
        ReflectionTestUtils.setField(provider, "jwtSecret", SECRET);
    }

    @Test
    void given_a_fresh_token_then_it_names_its_owner() {
        String token = provider.generate(new TestingAuthenticationToken(new User("me@playquiz.io", "x", List.of()), null));
        assertEquals("me@playquiz.io", provider.getUsernameFromToken(token));
    }

    @Test
    void given_an_expired_token_then_it_is_refused() throws Exception {
        assertThrows(TokenProcessException.class, () -> provider.getUsernameFromToken(signed(new Date(System.currentTimeMillis() - 1000))));
    }

    @Test
    void given_a_token_without_expiry_then_it_is_refused() throws Exception {
        assertThrows(TokenProcessException.class, () -> provider.getUsernameFromToken(signed(null)));
    }

    private static String signed(final Date expiry) throws Exception {
        SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256),
                new JWTClaimsSet.Builder().subject("me@playquiz.io").expirationTime(expiry).build());
        jwt.sign(new MACSigner(SECRET.getBytes()));
        return jwt.serialize();
    }
}
