package com.play.quiz.season;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import com.play.quiz.cosmetic.Cosmetic;

/**
 * The season pass's calendar and track, in code. Seasons run 30 days back to back from
 * {@link #FIRST_DAY}; weeks run Monday to Sunday. Ten tiers, one every {@link #TIER_POINTS}
 * points; each pays coins, and two pay a cosmetic only the pass gives.
 */
public record Season(int number, LocalDate start, LocalDate end) {

    static final LocalDate FIRST_DAY = LocalDate.of(2026, 10, 1);
    static final int LENGTH_DAYS = 30;
    static final int TIERS = 10;
    static final int TIER_POINTS = 200;
    /** What finishing one of the day's tasks adds to the season. */
    static final int DAILY_TASK_POINTS = 10;
    /** A cosmetic reward already owned pays these coins instead. */
    static final int OWNED_REWARD_COINS = 300;

    /** The season a day falls in; {@code end} is the first day after it. */
    public static Season of(final LocalDate day) {
        long days = Math.max(0, ChronoUnit.DAYS.between(FIRST_DAY, day));
        int number = (int) (days / LENGTH_DAYS) + 1;
        LocalDate start = FIRST_DAY.plusDays((long) (number - 1) * LENGTH_DAYS);
        return new Season(number, start, start.plusDays(LENGTH_DAYS));
    }

    public static LocalDate weekStart(final LocalDate day) {
        return day.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /** A tier's reward: a cosmetic for tiers 5 and 10, coins for the rest. */
    public static Optional<Cosmetic> itemFor(final int tier) {
        return switch (tier) {
            case 5 -> Optional.of(Cosmetic.COLOR_SEASON);
            case 10 -> Optional.of(Cosmetic.FRAME_CHAMPION);
            default -> Optional.empty();
        };
    }

    public static int coinsFor(final int tier) {
        return 50 * tier;
    }
}
