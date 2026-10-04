package com.play.quiz.season;

import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The season pass. Needs an account, which the security chain's catch-all rule asks for. */
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_SEASON)
@RequiredArgsConstructor
public class SeasonController {

    private final SeasonService seasonService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SeasonService.Status> status() {
        return ResponseEntity.ok(seasonService.status());
    }

    @PostMapping(value = "/tiers/{tier}/claim", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SeasonService.Status> claim(@PathVariable final int tier) {
        return ResponseEntity.ok(seasonService.claim(tier));
    }
}
