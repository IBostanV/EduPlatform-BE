package com.play.quiz.daily;

import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;

import com.play.quiz.domain.UserQuizHistory;
import lombok.Getter;

/**
 * The goals a player is given each day, and what each is worth.
 *
 * <p>Progress is read off the runs the player finished today rather than counted up as they play:
 * a quiz already writes its score to its history row, so there is nothing to keep in step and
 * nothing to lose when a run is recorded twice or not at all.
 */
@Getter
public enum DailyTask {

    /** Turning up at all. */
    PLAY_QUIZZES(3, 60, List::size),

    /** Playing well, over however many runs it takes. */
    RIGHT_ANSWERS(25, 80, runs -> runs.stream().mapToInt(UserQuizHistory::getRightAnswers).sum()),

    /** Playing perfectly, once. */
    FLAWLESS_RUN(1, 120, runs -> (int) runs.stream().filter(DailyTask::flawless).count());

    private final int target;
    private final int experience;
    private final ToIntFunction<List<UserQuizHistory>> measure;

    DailyTask(final int target, final int experience, final ToIntFunction<List<UserQuizHistory>> measure) {
        this.target = target;
        this.experience = experience;
        this.measure = measure;
    }

    /**
     * How far this task has been got with, from a day's runs.
     *
     * <p>A run that could not be marked has no score to count, and a custom quiz counts for
     * nothing here for the same reason it pays no experience: players write those themselves, so
     * a day's goals would be worth whatever the easiest self-made quiz is worth.
     */
    public int progressFrom(final List<UserQuizHistory> runs) {
        return measure.applyAsInt(runs.stream().filter(DailyTask::counts).toList());
    }

    private static boolean counts(final UserQuizHistory run) {
        return Objects.nonNull(run.getRightAnswers()) && Objects.nonNull(run.getTotalAnswers())
                && !run.getQuiz().isCustom();
    }

    private static boolean flawless(final UserQuizHistory run) {
        return run.getTotalAnswers() > 0 && Objects.equals(run.getRightAnswers(), run.getTotalAnswers());
    }
}
