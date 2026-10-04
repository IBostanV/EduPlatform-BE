package com.play.quiz.duel;

import java.util.List;

import com.play.quiz.controller.RestEndpoint;
import com.play.quiz.dto.QuizDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Turn-based duels. Needs an account, which the security chain's catch-all rule asks for. */
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_DUELS)
@RequiredArgsConstructor
public class DuelController {

    private final DuelService duelService;

    public record DuelInput(Long opponentId) {}

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DuelService.DuelView>> mine() {
        return ResponseEntity.ok(duelService.mine());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DuelService.DuelView> start(@RequestBody final DuelInput input) {
        return ResponseEntity.ok(duelService.start(input.opponentId()));
    }

    @GetMapping(value = "/{duelId}/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizDto> quiz(@PathVariable final Long duelId) {
        return ResponseEntity.ok(duelService.quiz(duelId));
    }
}
