package com.play.quiz.appearance;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The signed-in player's own look of the site, kept on their account as JSON. */
@Log4j2
@Service
@RequiredArgsConstructor
public class AppearanceService {

    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Appearance get() {
        String stored = accountRepository.findAppearance(currentAccountId());
        if (Objects.isNull(stored) || stored.isBlank()) {
            return Appearance.defaults();
        }
        try {
            return objectMapper.readValue(stored, Appearance.class);
        } catch (JsonProcessingException exception) {
            // A value that no longer reads (a setting renamed since) is the site as it comes,
            // not an error on every page the player opens.
            log.warn("Unreadable appearance on account {}, using the defaults", currentAccountId());
            return Appearance.defaults();
        }
    }

    /** Replaces the lot; the settings left out (null) go back to how the site comes. */
    @Transactional
    public Appearance save(final Appearance appearance) {
        Appearance settings = Objects.isNull(appearance) ? Appearance.defaults() : appearance.validated();
        try {
            // Nothing changed is stored as nothing, so a full reset leaves the column as it started.
            accountRepository.setAppearance(currentAccountId(),
                    settings.isDefault() ? null : objectMapper.writeValueAsString(settings));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not store the appearance", exception);
        }
        return get();
    }

    private Long currentAccountId() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
    }
}
