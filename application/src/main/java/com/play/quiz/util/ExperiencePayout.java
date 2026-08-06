package com.play.quiz.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * What a finished quiz run is worth in experience.
 *
 * <p>Here rather than in the service that pays it, because the conquest game pays on top of the
 * same run and has to agree with it to the point.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExperiencePayout {

    /** A wrong answer is still worth something for turning up. */
    public static final int PER_RIGHT_ANSWER = 10;
    public static final int PER_WRONG_ANSWER = 2;

    /**
     * What a go at conquering a country is worth, as a multiple of the ordinary run. The run has
     * already been paid for by the time the attempt is entered, so what the game owes is the
     * rest: see {@link #conquestTopUp}.
     */
    public static final int CONQUEST_MULTIPLIER = 2;

    /** On top of that, once, to whoever holds the country when the round shuts. */
    public static final int CONQUEST_BONUS = 250;

    /** Sitting the IQ test through to the end, paid on the first go only. */
    public static final int IQ_TEST = 300;

    /** Turning up at all, once a day. */
    public static final int DAILY_VISIT = 25;

    /** On top of that, for each day of the run so far — up to a week, after which it is flat. */
    public static final int STREAK_STEP = 10;
    public static final int STREAK_CAP = 7;

    /**
     * What today's visit is worth on day {@code streak} of a run. Rising, so a run is worth
     * keeping, but flat after a week: a hundred-day run should not be worth a thousand a day.
     */
    public static int forVisit(final int streak) {
        return DAILY_VISIT + STREAK_STEP * Math.min(Math.max(streak, 1), STREAK_CAP);
    }

    public static int forRun(final int rightAnswers, final int totalAnswers) {
        int wrong = Math.max(0, totalAnswers - rightAnswers);
        return rightAnswers * PER_RIGHT_ANSWER + wrong * PER_WRONG_ANSWER;
    }

    /** What is still owed on a run already paid for once, to bring it up to the conquest rate. */
    public static int conquestTopUp(final int rightAnswers, final int totalAnswers) {
        return forRun(rightAnswers, totalAnswers) * (CONQUEST_MULTIPLIER - 1);
    }
}
