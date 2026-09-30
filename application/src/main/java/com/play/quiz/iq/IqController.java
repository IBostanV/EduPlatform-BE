package com.play.quiz.iq;

import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// The IQ test. Everything about a run is the server's: which question comes next, which answer is
// right, and how long the question was on screen. The browser only says which option was clicked.
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_IQ)
@RequiredArgsConstructor
public class IqController {

    private final IqService iqService;

    /** Starts a test, or hands back the one already in progress. */
    @PostMapping(value = "/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IqService.IqState> start() {
        return ResponseEntity.ok(iqService.start());
    }

    @PostMapping(value = "/answer", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IqService.IqState> answer(@RequestBody final IqAnswerInput input) {
        return ResponseEntity.ok(iqService.answer(input.chosen()));
    }

    /** The player's last score, or no content if they have never finished one. */
    @GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IqResult> latest() {
        return iqService.latest().map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    /** -1 for a question given up on, which is scored as wrong. */
    public record IqAnswerInput(int chosen) {}
}
