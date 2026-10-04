package com.play.quiz.social;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DailyChallengeStreakTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 10, 6);

    @Test
    void given_played_days_when_counting_then_the_run_ends_today_or_waits_for_it() {
        // Today played: three in a row, the gap before them ends it.
        assertEquals(3, DailyChallengeService.streak(List.of(TODAY, TODAY.minusDays(1), TODAY.minusDays(2), TODAY.minusDays(4)), TODAY));
        // Today not played yet: the run up to yesterday still stands.
        assertEquals(2, DailyChallengeService.streak(List.of(TODAY.minusDays(1), TODAY.minusDays(2)), TODAY));
        // Missed yesterday: gone.
        assertEquals(0, DailyChallengeService.streak(List.of(TODAY.minusDays(2)), TODAY));
        assertEquals(0, DailyChallengeService.streak(List.of(), TODAY));
    }
}
