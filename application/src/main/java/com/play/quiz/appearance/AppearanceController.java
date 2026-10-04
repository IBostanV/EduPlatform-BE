package com.play.quiz.appearance;

import java.io.IOException;

import com.play.quiz.controller.RestEndpoint;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// The signed-in player's own look of the site. Under /user, so it needs an account like the rest.
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_USER + "/appearance")
@RequiredArgsConstructor
public class AppearanceController {

    private final AppearanceService appearanceService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Appearance> get() {
        return ResponseEntity.ok(appearanceService.get());
    }

    /** Replaces every setting; a setting sent as null (or left out) is reset. */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Appearance> save(@RequestBody final Appearance appearance) {
        return ResponseEntity.ok(appearanceService.save(appearance));
    }

    /**
     * The player's own background picture, sent as the bare image (the browser has already scaled
     * and compressed it). Read no further than the limit: a body past it is refused without being
     * held in memory whole.
     */
    @PostMapping(value = "/background", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Appearance> uploadBackground(final HttpServletRequest request) throws IOException {
        byte[] image = request.getInputStream().readNBytes(AppearanceService.MAX_BACKGROUND_BYTES + 1);
        return ResponseEntity.ok(appearanceService.uploadBackground(image));
    }

    @DeleteMapping(value = "/background", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Appearance> removeBackground() {
        return ResponseEntity.ok(appearanceService.removeBackground());
    }
}
