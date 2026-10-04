package com.play.quiz.donation;

import static com.play.quiz.controller.RestEndpoint.CONTEXT_PATH;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_DONATION;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Reading where to donate is open to everyone, guests too; changing it is admin-only (WebSecurity).
@RestController
@RequestMapping(CONTEXT_PATH + REQUEST_MAPPING_DONATION)
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DonationSettings> get() {
        return ResponseEntity.ok(donationService.get());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DonationSettings> save(@Valid @RequestBody final DonationSettings settings) {
        return ResponseEntity.ok(donationService.save(settings));
    }
}
