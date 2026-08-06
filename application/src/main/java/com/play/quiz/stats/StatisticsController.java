package com.play.quiz.stats;

import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// A player's statistics, over a day, a week or a month: their own, or another player's for that
// player's profile page. Only the totals travel; the runs they are counted from stay private.
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_STATISTICS)
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PeriodStatistics> getStatistics(
            @RequestParam(required = false) final String period) {
        return ResponseEntity.ok(statisticsService.of(StatisticsService.periodOf(period)));
    }

    @GetMapping(value = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PeriodStatistics> getStatistics(@PathVariable final Long accountId,
                                                          @RequestParam(required = false) final String period) {
        return ResponseEntity.ok(statisticsService.ofAccount(accountId, StatisticsService.periodOf(period)));
    }
}
