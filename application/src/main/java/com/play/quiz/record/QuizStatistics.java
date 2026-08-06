package com.play.quiz.record;

import com.play.quiz.repository.UserQuizHistoryRepository.HistoryTotals;

/**
 * What a player's whole quiz history adds up to, for the statistics above it on their profile.
 *
 * <p>Summed in the database over every run, not over the page on screen: a page of ten would
 * otherwise report an accuracy that changed as the player clicked through.
 *
 * <p>Runs recorded before the score was kept have no score to add, so they count towards
 * {@code played} and nothing else.
 */
public record QuizStatistics(long played,
                             long answered,
                             long rightAnswers,
                             int accuracy,
                             int best,
                             long seconds) {

    public static QuizStatistics of(final HistoryTotals totals) {
        long answered = totals.getTotalAnswers();
        long right = totals.getRightAnswers();

        return new QuizStatistics(
                totals.getPlayed(),
                answered,
                right,
                answered > 0 ? (int) Math.round(right * 100.0 / answered) : 0,
                (int) Math.round(totals.getBest()),
                Math.round(totals.getSeconds()));
    }
}
