package com.play.quiz.daily;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.play.quiz.domain.Quiz;
import com.play.quiz.domain.UserQuizHistory;
import org.junit.jupiter.api.Test;

/**
 * What a day's runs add up to. This is the whole of the daily tasks' arithmetic: everything else
 * is reading the rows and writing the payment down.
 */
class DailyTaskTest {

    @Test
    void given_a_day_of_runs_then_each_task_counts_what_it_is_about() {
        List<UserQuizHistory> runs = List.of(run(5, 10), run(10, 10), run(3, 4));

        assertEquals(3, DailyTask.PLAY_QUIZZES.progressFrom(runs));
        assertEquals(18, DailyTask.RIGHT_ANSWERS.progressFrom(runs));
        assertEquals(1, DailyTask.FLAWLESS_RUN.progressFrom(runs));
    }

    @Test
    void given_a_custom_quiz_then_it_counts_for_nothing() {
        // Players write those themselves, so a day's goals would be worth whatever the easiest
        // self-made quiz is worth — the same reason they pay no experience.
        List<UserQuizHistory> runs = List.of(custom(10, 10), run(2, 5));

        assertEquals(1, DailyTask.PLAY_QUIZZES.progressFrom(runs));
        assertEquals(2, DailyTask.RIGHT_ANSWERS.progressFrom(runs));
        assertEquals(0, DailyTask.FLAWLESS_RUN.progressFrom(runs));
    }

    @Test
    void given_an_unmarked_run_then_it_counts_for_nothing() {
        // A run that could not be marked has no score to count, so it is not a quiz played either.
        assertEquals(0, DailyTask.PLAY_QUIZZES.progressFrom(List.of(unmarked())));
        assertEquals(0, DailyTask.RIGHT_ANSWERS.progressFrom(List.of(unmarked())));
        assertEquals(0, DailyTask.FLAWLESS_RUN.progressFrom(List.of(unmarked())));
    }

    @Test
    void given_no_runs_then_nothing_has_been_got_with() {
        assertEquals(0, DailyTask.PLAY_QUIZZES.progressFrom(List.of()));
        assertEquals(0, DailyTask.RIGHT_ANSWERS.progressFrom(List.of()));
        assertEquals(0, DailyTask.FLAWLESS_RUN.progressFrom(List.of()));
    }

    private static UserQuizHistory run(final int right, final int total) {
        return history(right, total, false);
    }

    private static UserQuizHistory custom(final int right, final int total) {
        return history(right, total, true);
    }

    private static UserQuizHistory unmarked() {
        return UserQuizHistory.builder().quiz(Quiz.builder().build()).build();
    }

    private static UserQuizHistory history(final int right, final int total, final boolean custom) {
        return UserQuizHistory.builder()
                .quiz(Quiz.builder().custom(custom).build())
                .rightAnswers(right)
                .totalAnswers(total)
                .build();
    }
}
