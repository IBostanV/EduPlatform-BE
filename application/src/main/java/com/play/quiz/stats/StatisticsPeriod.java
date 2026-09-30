package com.play.quiz.stats;

/**
 * How far back a look at the statistics goes. Each period is also compared with the one
 * immediately before it of the same length, which is what turns a number into a direction.
 */
public enum StatisticsPeriod {

    DAY(1), WEEK(7), MONTH(30);

    private final int days;

    StatisticsPeriod(final int days) {
        this.days = days;
    }

    public int days() {
        return days;
    }
}
