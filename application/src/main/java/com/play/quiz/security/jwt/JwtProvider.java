package com.play.quiz.security.jwt;

import java.text.ParseException;
import java.util.Date;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.play.quiz.exception.TokenGenerationException;
import com.play.quiz.exception.TokenProcessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${application.security.jwt.secret}")
    private String jwtSecret;

    public String getUsernameFromToken(String jwtToken) {
        Assert.hasText(jwtToken, "Authentication token can not be empty.");
        try {
            return verifyAndGetUsernameFromToken(jwtToken);
        } catch (ParseException | JOSEException exception) {
            log.warn("Error while parsing the token: {}", exception.getMessage());
            throw new TokenProcessException(exception);
        }
    }

    private String verifyAndGetUsernameFromToken(String jwtToken) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(jwtToken);
        JWSVerifier verifier = new MACVerifier(jwtSecret.getBytes());

        if (signedJWT.verify(verifier)) {
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            // verify() checks the signature only: without this a token, leaked or not, works forever.
            Date expiry = claimsSet.getExpirationTime();
            if (expiry == null || expiry.before(new Date())) {
                throw new TokenProcessException("Token expired");
            }
            return claimsSet.getSubject();
        } else {
            log.warn("Token signature verification failed");
            throw new TokenProcessException("Could not process provided token");
        }
    }

    public String generate(final Authentication authentication) {
        return generateToken(authentication);
    }

    private String generateToken(final Authentication authentication) {
        User principal = (User)authentication.getPrincipal();
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = getJwtClaimsSet(principal);
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        try {
            MACSigner signer = new MACSigner(jwtSecret.getBytes());
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException exception) {
            log.warn("Exception during token generation: {}", exception.getMessage());
            throw new TokenGenerationException(exception);
        }
    }

    private static JWTClaimsSet getJwtClaimsSet(final User principal) {
        Date expirationTimeOneHour = new Date(new Date().getTime() + 3600 * 1000);
        return new JWTClaimsSet.Builder()
                .issuer("play-quiz")
                .subject(principal.getUsername())
                .expirationTime(expirationTimeOneHour)
                .build();
    }
}
