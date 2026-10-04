package com.play.quiz.review;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReviewLadderTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 10, 6);

    @Test
    void given_answers_when_filing_then_questions_climb_1_3_7_and_leave_once_learnt() {
        // Wrong, new or not: back to the bottom, due tomorrow.
        assertEquals(Optional.of(new ReviewService.Step(0, TODAY.plusDays(1))), ReviewService.next(null, TODAY, false, TODAY));
        assertEquals(Optional.of(new ReviewService.Step(0, TODAY.plusDays(1))), ReviewService.next(2, TODAY, false, TODAY));

        // Right when due: up a rung.
        assertEquals(Optional.of(new ReviewService.Step(1, TODAY.plusDays(3))), ReviewService.next(0, TODAY, true, TODAY));
        assertEquals(Optional.of(new ReviewService.Step(2, TODAY.plusDays(7))), ReviewService.next(1, TODAY.minusDays(1), true, TODAY));

        // Right on the last rung: learnt.
        assertTrue(ReviewService.next(2, TODAY, true, TODAY).isEmpty());

        // Right before it is due: stays where it is. Right and never wrong: nothing to file.
        assertEquals(Optional.of(new ReviewService.Step(1, TODAY.plusDays(2))), ReviewService.next(1, TODAY.plusDays(2), true, TODAY));
        assertTrue(ReviewService.next(null, TODAY, true, TODAY).isEmpty());
    }
}
