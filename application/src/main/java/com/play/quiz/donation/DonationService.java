package com.play.quiz.donation;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.quiz.domain.Property;
import com.play.quiz.repository.PropertyRepository;
import com.play.quiz.util.ServerText;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The donation settings, kept as JSON in one Q_PROPERTY row, created on the first save.
 *
 * <p>ponytail: Q_PROPERTY.VALUE is a VARCHAR(800), which holds PayPal and about six wallets; widen
 * the column (and VALUE_LIMIT) if admins need more.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class DonationService {
    static final String PROPERTY_NAME = "DONATION_SETTINGS";
    private static final int VALUE_LIMIT = 800;

    private final PropertyRepository propertyRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public DonationSettings get() {
        Property property = propertyRepository.findByName(PROPERTY_NAME);
        if (Objects.isNull(property)) return DonationSettings.EMPTY;
        try {
            return objectMapper.readValue(property.getValue(), DonationSettings.class);
        } catch (JsonProcessingException exception) {
            log.warn("Donation settings could not be read, showing none: {}", exception.getMessage());
            return DonationSettings.EMPTY;
        }
    }

    @Transactional
    public DonationSettings save(final DonationSettings settings) {
        String json = toJson(settings);
        if (json.length() > VALUE_LIMIT) {
            throw new IllegalArgumentException(ServerText.t("err_donation_too_much", "Too much to store: remove a wallet or shorten the names"));
        }
        Property existing = propertyRepository.findByName(PROPERTY_NAME);
        Property property = Objects.isNull(existing)
                ? Property.builder().name(PROPERTY_NAME).value(json).createdDate(LocalDateTime.now()).build()
                : existing.toBuilder().oldValue(existing.getValue()).value(json).updatedDate(LocalDateTime.now()).build();
        propertyRepository.save(property);
        log.info("Donation settings saved: PayPal {}, {} wallets",
                Objects.nonNull(settings.paypal()) ? "set" : "none", settings.crypto().size());
        return settings;
    }

    private String toJson(final DonationSettings settings) {
        try {
            return objectMapper.writeValueAsString(settings);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException(ServerText.t("err_donation_not_stored", "Donation settings could not be stored"), exception);
        }
    }
}
