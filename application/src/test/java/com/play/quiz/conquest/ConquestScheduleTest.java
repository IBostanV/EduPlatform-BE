package com.play.quiz.conquest;

import static com.play.quiz.conquest.ConquestSchedule.CYCLE_DAYS;
import static com.play.quiz.conquest.ConquestSchedule.ALTERNATING_FROM;
import static com.play.quiz.conquest.ConquestSchedule.EPOCH;
import static com.play.quiz.conquest.ConquestSchedule.FIRST_ALTERNATING_ROUND;
import static com.play.quiz.conquest.ConquestSchedule.ROUND_DAYS;
import static com.play.quiz.conquest.ConquestSchedule.OPEN_DAYS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;

/**
 * Rounds are worked out from the clock rather than opened by a job, so this is where the game's
 * calendar is actually pinned down.
 */
class ConquestScheduleTest {

    private static final List<String> WORLD = IntStream.range(0, 40).mapToObj(index -> "C" + index).toList();

    @Test
    void given_the_epoch_then_round_zero_is_open() {
        assertEquals(0, ConquestSchedule.roundAt(EPOCH));
        assertTrue(ConquestSchedule.isOpenAt(EPOCH));
    }

    @Test
    void given_a_cycle_then_one_day_takes_attempts_and_seven_do_not() {
        LocalDateTime start = EPOCH;

        assertTrue(ConquestSchedule.isOpenAt(start.plusHours(23)), "Still open on the first day");
        assertFalse(ConquestSchedule.isOpenAt(start.plusDays(OPEN_DAYS)), "Shut once the open days are up");
        assertFalse(ConquestSchedule.isOpenAt(start.plusDays(CYCLE_DAYS - 1)), "Still shut on the last locked day");
        assertTrue(ConquestSchedule.isOpenAt(start.plusDays(CYCLE_DAYS)), "The next round opens the cycle after");
    }

    @Test
    void given_the_switch_then_the_last_old_round_ends_and_the_first_alternating_one_starts() {
        assertEquals(FIRST_ALTERNATING_ROUND - 1, ConquestSchedule.roundAt(ALTERNATING_FROM.minusSeconds(1)));
        assertEquals(FIRST_ALTERNATING_ROUND, ConquestSchedule.roundAt(ALTERNATING_FROM));
        assertEquals(ALTERNATING_FROM, ConquestSchedule.roundOpensAt(FIRST_ALTERNATING_ROUND));
        // Anybody waiting on the old round is told the new one's start.
        assertEquals(ALTERNATING_FROM, ConquestSchedule.nextOpenAt(ALTERNATING_FROM.minusDays(1)));
    }

    @Test
    void given_an_alternating_round_then_it_is_open_every_other_day_starting_open() {
        for (long round = FIRST_ALTERNATING_ROUND; round < FIRST_ALTERNATING_ROUND + 5; round++) {
            LocalDateTime opens = ConquestSchedule.roundOpensAt(round);
            for (int day = 0; day < ROUND_DAYS; day++) {
                LocalDateTime noon = opens.plusDays(day).plusHours(12);
                assertEquals(day % 2 == 0, ConquestSchedule.isOpenAt(noon), "Round " + round + ", day " + day);
                assertEquals(round, ConquestSchedule.roundAt(noon));
                // Open: until tonight. Shut: from tonight, which is always an open day.
                assertEquals(opens.plusDays(day + 1),
                        day % 2 == 0 ? ConquestSchedule.openUntil(noon) : ConquestSchedule.nextOpenAt(noon));
            }
            assertEquals(ConquestSchedule.roundOpensAt(round + 1), opens.plusDays(ROUND_DAYS));
            // The winners stand once the whole round is out, not after its first open day.
            assertEquals(ConquestSchedule.roundOpensAt(round + 1), ConquestSchedule.roundClosesAt(round));
        }
    }

    @Test
    void given_a_moment_then_the_round_runs_from_its_own_opening_to_the_next() {
        for (long round = 0; round < FIRST_ALTERNATING_ROUND - 1; round++) {
            LocalDateTime opens = ConquestSchedule.roundOpensAt(round);

            assertEquals(round, ConquestSchedule.roundAt(opens));
            assertEquals(round, ConquestSchedule.roundAt(opens.plusDays(CYCLE_DAYS - 1)));
            assertEquals(round + 1, ConquestSchedule.roundAt(opens.plusDays(CYCLE_DAYS)));
            // Rounds run back to back: no gap, no overlap.
            assertEquals(ConquestSchedule.roundOpensAt(round + 1), opens.plusDays(CYCLE_DAYS));
            assertTrue(ConquestSchedule.roundClosesAt(round).isAfter(opens));
        }
    }

    @Test
    void given_a_round_then_it_draws_the_same_countries_every_time_it_is_asked() {
        // The whole point of drawing rather than storing: every instance agrees without a table.
        for (long round = 0; round < 20; round++) {
            assertEquals(ConquestSchedule.countriesFor(round, WORLD), ConquestSchedule.countriesFor(round, WORLD));
        }
    }

    @Test
    void given_a_round_then_it_opens_a_handful_of_different_countries() {
        List<String> drawn = ConquestSchedule.countriesFor(3, WORLD);

        assertEquals(ConquestSchedule.COUNTRIES_PER_ROUND, drawn.size());
        assertEquals(drawn.size(), Set.copyOf(drawn).size(), "The same country must not be drawn twice");
        assertTrue(WORLD.containsAll(drawn));
    }

    @Test
    void given_consecutive_rounds_then_they_do_not_all_open_the_same_countries() {
        // Seeds one apart must not give near-identical draws, or the game would never move on.
        long differing = LongStream.range(0, 20)
                .filter(round -> !ConquestSchedule.countriesFor(round, WORLD)
                        .equals(ConquestSchedule.countriesFor(round + 1, WORLD)))
                .count();

        assertEquals(20, differing);
    }

    @Test
    void given_fewer_countries_than_a_round_wants_then_open_what_there_is() {
        assertEquals(2, ConquestSchedule.countriesFor(1, List.of("A", "B")).size());
        assertTrue(ConquestSchedule.countriesFor(1, List.of()).isEmpty());
    }

    @Test
    void given_a_country_list_then_drawing_leaves_it_alone() {
        List<String> world = new ArrayList<>(List.of("A", "B", "C", "D", "E", "F"));
        List<String> before = List.copyOf(world);

        ConquestSchedule.countriesFor(7, world);

        assertEquals(before, world, "The caller's list must not be shuffled under it");
    }

    @Test
    void given_two_lists_then_a_bigger_world_draws_differently() {
        // Not a rule the game needs, but it catches a draw that ignores the list it was given.
        assertNotEquals(ConquestSchedule.countriesFor(5, WORLD).size(),
                ConquestSchedule.countriesFor(5, List.of("A", "B")).size());
    }
}
