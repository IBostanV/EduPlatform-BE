package com.play.quiz.stats;

import java.time.LocalDate;
import java.util.List;

/**
 * What a player did over a stretch of days, and what it says about them.
 *
 * <p>Two things here are worth more than the counts. {@code trend} sets the period against the one
 * before it, because "43 quizzes" means nothing without "and 28 last week". And
 * {@code strongest}/{@code weakest} are where the accuracy actually differs by subject, which is
 * the only part of this that tells somebody what to go and read.
 *
 * @param accuracy          right answers as a percentage of those asked
 * @param secondsPerQuestion how long a question took on average, over runs that were timed
 * @param activeDays        days with at least one quiz on them
 * @param byDay             one row per day of the period, oldest first, including the empty ones
 */
public record PeriodStatistics(StatisticsPeriod period,
                               LocalDate from,
                               LocalDate to,
                               long quizzes,
                               long rightAnswers,
                               long wrongAnswers,
                               int accuracy,
                               double secondsPerQuestion,
                               double secondsPerQuiz,
                               double minutesPlayed,
                               long trophies,
                               long activeDays,
                               int loginStreak,
                               CategoryStat topCategory,
                               List<CategoryStat> strongest,
                               List<CategoryStat> weakest,
                               List<DayStat> byDay,
                               Trend trend) {

    /** How a category went: what was played in it and how much of it was right. */
    public record CategoryStat(Long categoryId, String name, long quizzes, long rightAnswers,
                               long totalAnswers, int accuracy) {
    }

    /** One day of the period, for the activity strip. */
    public record DayStat(LocalDate day, long quizzes, long rightAnswers, long totalAnswers) {
    }

    /**
     * The same period, one period ago, and the change: quizzes as a percentage of what it was,
     * accuracy in points. Null where there is nothing to compare against.
     */
    public record Trend(long quizzesBefore, int accuracyBefore, Integer quizzesChange,
                        Integer accuracyChange) {
    }
}
