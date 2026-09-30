package com.play.quiz.daily;

import java.util.List;

import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Today's goals for the signed-in player. Reading them is also what pays the finished ones, so
// there is nothing for a client to claim and no second endpoint to guard.
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_DAILY_TASK)
@RequiredArgsConstructor
public class DailyTaskController {

    private final DailyTaskService dailyTaskService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DailyTaskProgress>> getToday() {
        return ResponseEntity.ok(dailyTaskService.getToday());
    }
}
