package com.play.quiz.controller;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_USER_HISTORY;

import com.play.quiz.dto.UserQuizHistoryDto;
import com.play.quiz.record.PageResponse;
import com.play.quiz.record.QuizHistoryEntry;
import com.play.quiz.record.QuizStatistics;
import com.play.quiz.service.UserQuizHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_USER_HISTORY)
@RequiredArgsConstructor
public class UserQuizHistoryController {

    private final UserQuizHistoryService userQuizHistoryService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserQuizHistoryDto> saveUserQuiz(@RequestBody final UserQuizHistoryDto userQuizHistoryDto) {
        return ResponseEntity.ok(userQuizHistoryService.save(userQuizHistoryDto));
    }

    @GetMapping(value = "/history/{historyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserQuizHistoryDto> getUserQuiz(@PathVariable final Long historyId) {
        return ResponseEntity.ok(userQuizHistoryService.getById(historyId));
    }

    // One page of the signed-in player's own runs, newest first, for the history on their
    // profile. No account id in the path, so nobody can list someone else's. `page` is 0-based.
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<QuizHistoryEntry>> getOwnHistory(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size) {
        return ResponseEntity.ok(userQuizHistoryService.getOwnHistory(page, size));
    }

    // What their whole history adds up to, which the page above cannot say on its own.
    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizStatistics> getOwnStatistics() {
        return ResponseEntity.ok(userQuizHistoryService.getOwnStatistics());
    }
}
