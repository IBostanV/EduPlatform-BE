package com.play.quiz.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.VerificationToken;
import com.play.quiz.repository.VerificationTokenRepository;
import com.play.quiz.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Value("${application.email.token.validity.period}")
    private Integer verificationTokenValidityPeriod;

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken createVerificationToken(final Account account) {
        VerificationToken token = verificationTokenRepository.save(buildVerificationToken(account));
        log.info("Created verification token for account id: {}, valid for: {}", account.getAccountId(), verificationTokenValidityPeriod);
        return token;
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        Optional<VerificationToken> found = Optional.ofNullable(verificationTokenRepository.findByToken(token));
        if (found.isEmpty()) {
            log.info("Verification token not found");
        }
        return found;
    }

    @Override
    public VerificationToken save(final VerificationToken verificationToken) {
        return verificationTokenRepository.save(verificationToken);
    }

    private VerificationToken buildVerificationToken(final Account account) {
        return VerificationToken.builder()
                .account(account)
                .issuedDate(LocalDateTime.now())
                .token(UUID.randomUUID().toString())
                .validityPeriod(verificationTokenValidityPeriod)
                .build();
    }
}
