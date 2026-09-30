package com.play.quiz.donation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.quiz.domain.Property;
import com.play.quiz.repository.PropertyRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/** What an admin may store as donation details, and that it comes back as saved. */
class DonationSettingsTest {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    private static final String BTC = "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq";

    private static int problems(final DonationSettings settings) {
        return VALIDATOR.validate(settings).size();
    }

    private static DonationSettings withLink(final String link) {
        return new DonationSettings(new DonationSettings.PayPal(null, link), List.of());
    }

    private static DonationSettings withAddress(final String address) {
        return new DonationSettings(null, List.of(new DonationSettings.Wallet("Bitcoin", "BTC", null, address)));
    }

    @Test
    void links_must_be_https() {
        assertEquals(0, problems(withLink("https://paypal.me/playquiz")));
        assertEquals(1, problems(withLink("javascript:alert(1)")));
        assertEquals(1, problems(withLink("http://paypal.me/playquiz")));
    }

    @Test
    void addresses_are_letters_and_digits() {
        assertEquals(0, problems(withAddress(BTC)));
        assertEquals(0, problems(withAddress("0x71C7656EC7ab88b098defB751B7401B5f6d8976F")));
        assertEquals(1, problems(withAddress("<img src=x onerror=alert(1)>")));
        assertEquals(1, problems(withAddress("short")));
    }

    @Test
    void saved_settings_read_back_and_oversized_ones_are_refused() {
        PropertyRepository repository = mock(PropertyRepository.class);
        DonationService service = new DonationService(repository, new ObjectMapper());
        DonationSettings settings = new DonationSettings(new DonationSettings.PayPal("give@playquiz.com", null),
                List.of(new DonationSettings.Wallet("Bitcoin", "BTC", "Bitcoin", BTC)));

        service.save(settings);
        ArgumentCaptor<Property> saved = ArgumentCaptor.forClass(Property.class);
        verify(repository).save(saved.capture());
        when(repository.findByName(DonationService.PROPERTY_NAME)).thenReturn(saved.getValue());
        assertEquals(settings, service.get());

        DonationSettings tooMuch = new DonationSettings(null,
                Collections.nCopies(10, new DonationSettings.Wallet("Bitcoin", "BTC", "Bitcoin", BTC)));
        assertThrows(IllegalArgumentException.class, () -> service.save(tooMuch));
        assertTrue(service.get().crypto().size() == 1);
    }
}
