package com.play.quiz.email.helper;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_AUTH;

import java.util.Map;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.VerificationToken;
import com.play.quiz.util.ServerText;
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

    private static final String ACCOUNT_ACTIVATION_EMAIL_TEMPLATE = "confirmation-email.html";
    private static final String ACTIVATE_ACCOUNT_PATH = REQUEST_MAPPING_AUTH + "/activate-account";

    public EmailMessage createAccountVerificationEmailMessage(final Account account, final VerificationToken verificationToken) {
        // Built while the sign-up request is still being answered, so in the language it came in.
        Map<String, Object> properties = Map.of(
                "token", verificationToken.getToken(),
                "activateAccountServerHost", serverHostUrl + ACTIVATE_ACCOUNT_PATH,
                "greeting", ServerText.t("email_greeting", "Hello human!"),
                "instruction", ServerText.t("email_activate_instruction", "To activate your account please use the following link:"),
                "linkText", ServerText.t("email_activate_link", "Activate account"),
                "regards", ServerText.t("email_regards", "Kind Regards,"),
                "team", ServerText.t("email_team", "The Play Quiz Team"));
        log.info("Creating account verification email for {}", account.getEmail());

        return EmailMessage.builder()
                .to(account.getEmail())
                .from(defaultSenderEmailAddress)
                .subject(ServerText.t("email_activate_subject", "Activate your account"))
                .template(ACCOUNT_ACTIVATION_EMAIL_TEMPLATE)
                .properties(properties)
                .build();
    }
}
