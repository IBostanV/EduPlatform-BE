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

    /** For winning a game of pairs that was played to the end; a draw pays everyone at the top. */
    public static final int PAIRS_WIN = 15;
    /** Wins a player is paid for a day, so two friends cannot farm coins off quick games. */
    public static final int PAIRS_PAID_WINS_PER_DAY = 5;

    /**
     * What a pairs win pays when it is the {@code gameTogether}-th game today between the same two
     * players: half the one before each time (15, 7, 3, 1, then nothing), so a rematch is still
     * worth something and farming one friend is not.
     */
    public static int pairsWin(final int gameTogether) {
        return gameTogether < 1 ? 0 : PAIRS_WIN >> Math.min(gameTogether - 1, 30);
    }

    public static int forExperience(final int experience) {
        return Math.max(experience, 0) / EXPERIENCE_PER_COIN;
    }
}
