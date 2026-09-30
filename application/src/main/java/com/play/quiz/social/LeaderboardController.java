package com.play.quiz.social;

import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** The site-wide tables. Open to guests (see WebSecurity): they are the home page's. */
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_LEADERBOARD)
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LeaderboardService.Leaderboard> read(
            @RequestParam(defaultValue = "QUIZZES") final LeaderboardService.Board board,
            @RequestParam(defaultValue = "WEEK") final LeaderboardService.Period period) {
        return ResponseEntity.ok(leaderboardService.read(board, period));
    }
}
