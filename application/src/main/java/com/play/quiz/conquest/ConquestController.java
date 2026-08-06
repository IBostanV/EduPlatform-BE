package com.play.quiz.conquest;

import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Conquer the world by answering questions about it. Reading the map is open to everyone, so a
// guest sees who holds what; entering a run needs an account (WebSecurity).
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_CONQUEST)
@RequiredArgsConstructor
public class ConquestController {

    private final ConquestService conquestService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConquestState> getState() {
        return ResponseEntity.ok(conquestService.getState());
    }

    // Enters a finished quiz run as a go at this country. The run carries its own score and time,
    // so there is nothing here for a client to claim.
    @PostMapping(value = "/{countryId}/attempt", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConquestState> attempt(@PathVariable final Long countryId,
                                                 @RequestBody final ConquestAttemptInput input) {
        return ResponseEntity.ok(conquestService.recordAttempt(countryId, input.historyId()));
    }

    public record ConquestAttemptInput(Long historyId) {}

    // Which chat group the reader plays for; a null groupId plays on their own again.
    @PutMapping(value = "/team", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConquestState> setTeam(@RequestBody final TeamInput input) {
        return ResponseEntity.ok(conquestService.setTeam(input.groupId()));
    }

    public record TeamInput(Long groupId) {}
}
