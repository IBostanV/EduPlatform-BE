package com.play.quiz.conquest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * When the conquest game is open and which countries it opens, worked out from the clock alone.
 *
 * <p>There is no scheduler behind this and deliberately so: a job that opens rounds is a job that
 * can miss one while the server is down, and fire twice where there are two of them. A round is a
 * function of the time instead, so every instance agrees and nothing has to be caught up.
 *
 * <p>A round is {@link #ROUND_DAYS} days with the same handful of countries up for the taking,
 * open one day and shut the next, and its winners stand on the map once it ends. (Rounds before
 * {@link #FIRST_ALTERNATING_ROUND} were one open day and seven shut; they keep that calendar.) The
 * countries a round opens are drawn from the list by a generator seeded with the round's own
 * number, so every instance draws the same ones without anything being written down.
 */
public final class ConquestSchedule {

    /** Round 0 began here. Moving this shifts every round, so it does not move. */
    public static final LocalDateTime EPOCH = LocalDateTime.of(2026, 1, 5, 0, 0);

    // The first calendar, rounds 0 to 32: one open day, then seven locked.
    static final int OPEN_DAYS = 1;
    static final int CYCLE_DAYS = 8;

    /**
     * From here on a round is {@link #ROUND_DAYS} long and open every other day, starting open:
     * days 1, 3, 5 and 7. The earlier rounds keep their own calendar, because the attempts made in
     * them carry their round numbers and must still add up to the same holders.
     */
    public static final LocalDateTime ALTERNATING_FROM = LocalDateTime.of(2026, 9, 24, 0, 0);
    public static final long FIRST_ALTERNATING_ROUND = 33;
    public static final int ROUND_DAYS = 7;

    /** How many countries a round opens. */
    public static final int COUNTRIES_PER_ROUND = 4;

    /** A player may try a country again this long after their last go at it. */
    public static final Duration ATTEMPT_COOLDOWN = Duration.ofHours(12);

    private ConquestSchedule() {
    }

    /** Which round the given moment falls in. Rounds run back to back and are numbered from 0. */
    public static long roundAt(final LocalDateTime moment) {
        if (moment.isBefore(ALTERNATING_FROM)) {
            return Duration.between(EPOCH, moment).toDays() / CYCLE_DAYS;
        }
        return FIRST_ALTERNATING_ROUND + Duration.between(ALTERNATING_FROM, moment).toDays() / ROUND_DAYS;
    }

    public static LocalDateTime roundOpensAt(final long round) {
        return round < FIRST_ALTERNATING_ROUND
                ? EPOCH.plusDays(round * CYCLE_DAYS)
                : ALTERNATING_FROM.plusDays((round - FIRST_ALTERNATING_ROUND) * ROUND_DAYS);
    }

    /**
     * When the round stops taking attempts for good; its winners stand from here. In the
     * alternating rounds that is the end of the round, not its first closed day: the countries
     * are still being fought over until the last open day is out.
     */
    public static LocalDateTime roundClosesAt(final long round) {
        return round < FIRST_ALTERNATING_ROUND
                ? roundOpensAt(round).plusDays(OPEN_DAYS)
                : roundOpensAt(round + 1);
    }

    /** Whether attempts are being taken at the given moment. */
    public static boolean isOpenAt(final LocalDateTime moment) {
        long round = roundAt(moment);
        if (round < FIRST_ALTERNATING_ROUND) {
            return moment.isBefore(roundClosesAt(round));
        }
        return dayOfRound(round, moment) % 2 == 0;
    }

    /** When the open spell the moment falls in ends; only meaningful while {@link #isOpenAt} holds. */
    public static LocalDateTime openUntil(final LocalDateTime moment) {
        long round = roundAt(moment);
        return round < FIRST_ALTERNATING_ROUND
                ? roundClosesAt(round)
                : roundOpensAt(round).plusDays(dayOfRound(round, moment) + 1);
    }

    /**
     * When attempts are next taken after the moment. In the alternating rounds a closed day is
     * always followed by an open one — the last day of a round is open — so it is the next midnight.
     */
    public static LocalDateTime nextOpenAt(final LocalDateTime moment) {
        long round = roundAt(moment);
        return round < FIRST_ALTERNATING_ROUND
                ? roundOpensAt(round + 1)
                : roundOpensAt(round).plusDays(dayOfRound(round, moment) + 1);
    }

    private static long dayOfRound(final long round, final LocalDateTime moment) {
        return Duration.between(roundOpensAt(round), moment).toDays();
    }

    /**
     * The countries a round opens, drawn from {@code all} in an order settled by the round number.
     *
     * <p>Drawn rather than stored: the same round gives the same countries on every instance and
     * after every restart. The list itself is the one moving part — adding a country changes what
     * later rounds would have drawn, which matters to nothing, since only the round in hand is
     * ever shown and a conquest is held per country rather than per round.
     */
    public static <T> List<T> countriesFor(final long round, final List<T> all) {
        if (all.isEmpty()) {
            return List.of();
        }
        List<T> shuffled = new ArrayList<>(all);
        Collections.shuffle(shuffled, new Random(seedFor(round)));

        return List.copyOf(shuffled.subList(0, Math.min(COUNTRIES_PER_ROUND, shuffled.size())));
    }

    // Spread out, so consecutive rounds do not draw near-identical orders from near-identical seeds.
    private static long seedFor(final long round) {
        return round * 6364136223846793005L + 1442695040888963407L;
    }
}
