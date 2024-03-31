package com.play.quiz.email.helper;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_AUTH;

import java.util.Map;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.VerificationToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class EmailMessageFactory {
    @Value("${application.server.host.url}")
    private String serverHostUrl;
    @Value("${application.email.sending-address}")
    private String defaultSenderEmailAddress;

    private static final String ACCOUNT_ACTIVATION_SUBJECT = "Activate your account";
    private static final String ACCOUNT_ACTIVATION_EMAIL_TEMPLATE = "confirmation-email.html";
    private static final String ACTIVATE_ACCOUNT_PATH = REQUEST_MAPPING_AUTH + "/activate-account";

    public EmailMessage createAccountVerificationEmailMessage(final Account account, final VerificationToken verificationToken) {
        Map<String, Object> properties = Map.of(
                "token", verificationToken.getToken(),
                "activateAccountServerHost", serverHostUrl + ACTIVATE_ACCOUNT_PATH);
        log.info("Creating EmailMessage with properties: {}", properties);

        return EmailMessage.builder()
                .to(account.getEmail())
                .from(defaultSenderEmailAddress)
                .subject(ACCOUNT_ACTIVATION_SUBJECT)
                .template(ACCOUNT_ACTIVATION_EMAIL_TEMPLATE)
                .properties(properties)
                .build();
    }
}
