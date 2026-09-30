package com.play.quiz.coin;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * What coins are earned at and what they buy. All in one place so the prices can be tuned
 * together: an active player earns something like 30–50 a day.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Coins {

    /** Coins ride on experience, one for every ten, so everything that pays XP pays coins too. */
    public static final int EXPERIENCE_PER_COIN = 10;

    /** Covers one missed day of the visit streak. */
    public static final int STREAK_FREEZE_PRICE = 100;
    /** Freezes a player may hold at once, so a long break still ends a streak. */
    public static final int STREAK_FREEZE_MAX = 2;

    /** Takes away all but one of a question's wrong options. */
    public static final int HINT_PRICE = 15;

    /** Seconds added to a timed quiz's clock. */
    public static final int EXTRA_TIME_PRICE = 10;
    public static final int EXTRA_TIME_SECONDS = 30;

    public static int forExperience(final int experience) {
        return Math.max(experience, 0) / EXPERIENCE_PER_COIN;
    }
}
