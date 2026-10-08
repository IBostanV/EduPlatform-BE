package com.play.quiz.appearance;

import java.time.Duration;

import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A player's uploaded background, by its address. Open to anyone, the way a stylesheet's url()
 * is fetched (with no Authorization header); the address is random and changes with every
 * upload, so it cannot be guessed, and what is at one address never changes: the browser keeps
 * it for a year and never asks again.
 */
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_BACKGROUND)
@RequiredArgsConstructor
public class BackgroundController {

    private final AppearanceService appearanceService;

    @GetMapping("/{publicId}")
    public ResponseEntity<byte[]> get(@PathVariable final String publicId) {
        return appearanceService.background(publicId)
                .map(picture -> ResponseEntity.ok()
                        .cacheControl(CacheControl.maxAge(Duration.ofDays(365)).cachePublic().immutable())
                        .contentType(MediaType.parseMediaType(picture.contentType()))
                        .body(picture.bytes()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
