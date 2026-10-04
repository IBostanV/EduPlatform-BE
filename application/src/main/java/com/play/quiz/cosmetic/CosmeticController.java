package com.play.quiz.cosmetic;

import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The cosmetics shop and wardrobe. Every answer is the catalog as it now stands. */
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_COSMETICS)
@RequiredArgsConstructor
public class CosmeticController {

    private final CosmeticService cosmeticService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CosmeticService.Catalog> catalog() {
        return ResponseEntity.ok(cosmeticService.catalog());
    }

    @PostMapping(value = "/{code}/buy", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CosmeticService.Catalog> buy(@PathVariable final String code) {
        return ResponseEntity.ok(cosmeticService.buy(code));
    }

    @PostMapping(value = "/{code}/wear", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CosmeticService.Catalog> wear(@PathVariable final String code) {
        return ResponseEntity.ok(cosmeticService.wear(code));
    }

    @DeleteMapping(value = "/worn/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CosmeticService.Catalog> takeOff(@PathVariable final Cosmetic.Type type) {
        return ResponseEntity.ok(cosmeticService.takeOff(type));
    }
}
