package com.play.quiz.trophy;

import java.util.Map;

/**
 * Everything the trophies are judged on, read once per look at the shelf.
 *
 * <p>Read rather than kept: a count of quizzes is already in the history table, and a second copy
 * of it in a trophy row is a second copy to get wrong. The only thing stored about a trophy is
 * that it was earned, which is the one fact the tables cannot answer later (a trophy earned at
 * ten quizzes stays earned at nine, if a run is ever deleted).
 *
 * @param quizzesByCategory how many runs in each category, for the category trophies
 * @param countriesHeld     countries conquered — a round won, not merely played
 * @param fullTaskDays      days on which every one of that day's tasks was finished
 * @param playStreak        the longest run of consecutive days with a quiz finished on each
 * @param swiftRuns         quizzes of ten questions or more finished inside a minute
 * @param bestIq            the best IQ score, or 0 if the test has never been finished
 * @param nightRuns         quizzes finished between two and five in the morning
 */
public record PlayerStanding(long quizzes,
                             Map<Long, Long> quizzesByCategory,
                             long flawlessRuns,
                             long swiftRuns,
                             long nightRuns,
                             long countriesHeld,
                             long fullTaskDays,
                             int playStreak,
                             long iqTests,
                             int bestIq,
                             int loginStreak,
                             int bestStreak) {

    public long inCategory(final Long categoryId) {
        return quizzesByCategory.getOrDefault(categoryId, 0L);
    }
}
