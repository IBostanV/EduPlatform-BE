package com.play.quiz.trophy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

/** What counts as a run of days, which is what the playing-streak trophies are judged on. */
class StreaksTest {

    private static final LocalDate MONDAY = LocalDate.of(2026, 9, 21);

    private static List<LocalDate> days(final int... offsets) {
        return java.util.Arrays.stream(offsets).mapToObj(MONDAY::plusDays).toList();
    }

    @Test
    void given_no_days_then_there_is_no_run() {
        assertEquals(0, Streaks.longestRun(Set.of()));
    }

    @Test
    void given_consecutive_days_then_they_are_one_run() {
        assertEquals(1, Streaks.longestRun(days(0)));
        assertEquals(3, Streaks.longestRun(days(0, 1, 2)));
        assertEquals(7, Streaks.longestRun(days(0, 1, 2, 3, 4, 5, 6)));
    }

    @Test
    void given_a_gap_then_the_run_starts_again_and_the_longest_stands() {
        // Three, then a day off, then four: four is what the player has ever managed.
        assertEquals(4, Streaks.longestRun(days(0, 1, 2, 4, 5, 6, 7)));
        // The longest counts even when it is not the most recent.
        assertEquals(5, Streaks.longestRun(days(0, 1, 2, 3, 4, 9, 10)));
    }

    @Test
    void given_several_quizzes_on_one_day_then_it_is_still_one_day() {
        assertEquals(2, Streaks.longestRun(List.of(MONDAY, MONDAY, MONDAY.plusDays(1), MONDAY)));
    }

    @Test
    void given_days_out_of_order_then_the_run_is_found_all_the_same() {
        assertEquals(3, Streaks.longestRun(days(2, 0, 1)));
    }
}
