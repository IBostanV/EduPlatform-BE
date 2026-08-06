package com.play.quiz.trophy;

import java.util.List;

import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// The trophy shelf, and the one trophy a player chooses to show beside their name. Reading the
// shelf is also what records anything newly earned, so there is nothing to claim.
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_TROPHY)
@RequiredArgsConstructor
public class TrophyController {

    private final TrophyService trophyService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TrophyProgress>> getShelf() {
        return ResponseEntity.ok(trophyService.shelf());
    }

    /** A code to show it, nothing to show none. Only an earned trophy may be chosen. */
    @PatchMapping(value = "/preferred", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TrophyProgress>> prefer(@RequestBody final PreferredInput input) {
        return ResponseEntity.ok(trophyService.prefer(input.code()));
    }

    public record PreferredInput(String code) {}
}
