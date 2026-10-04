package com.play.quiz.duel;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DuelRulesTest {

    private static final LocalDateTime START = LocalDateTime.of(2026, 10, 6, 10, 0);

    private static DuelRules.Score score(final int right, final int hour) {
        return new DuelRules.Score(right, 5, 30.0, START.plusHours(hour));
    }

    @Test
    void given_a_new_duel_then_the_challenger_starts_and_turns_alternate_by_round() {
        Map<Integer, DuelRules.Round> rounds = new HashMap<>();
        assertEquals(DuelRules.Side.CHALLENGER, DuelRules.of(START, rounds, START).turn());

        rounds.put(1, new DuelRules.Round(score(4, 1), null));
        DuelRules.State state = DuelRules.of(START, rounds, START.plusHours(2));
        assertEquals(DuelRules.Side.OPPONENT, state.turn());
        assertEquals(1, state.currentRound());

        // Round 2 is the opponent's to start.
        rounds.put(1, new DuelRules.Round(score(4, 1), score(3, 2)));
        state = DuelRules.of(START, rounds, START.plusHours(3));
        assertEquals(2, state.currentRound());
        assertEquals(DuelRules.Side.OPPONENT, state.turn());
        assertEquals(1, state.challengerWins());
        // A day from the last move.
        assertEquals(START.plusHours(2).plus(DuelRules.TURN), state.deadline());
    }

    @Test
    void given_three_round_wins_then_the_duel_ends_early() {
        Map<Integer, DuelRules.Round> rounds = new HashMap<>();
        rounds.put(1, new DuelRules.Round(score(5, 1), score(1, 2)));
        rounds.put(2, new DuelRules.Round(score(5, 4), score(2, 3)));
        rounds.put(3, new DuelRules.Round(score(4, 5), score(0, 6)));

        DuelRules.State state = DuelRules.of(START, rounds, START.plusHours(7));
        assertTrue(state.over());
        assertFalse(state.forfeit());
        assertEquals(DuelRules.Side.CHALLENGER, state.winner());
    }

    @Test
    void given_a_turn_left_for_over_a_day_then_whoever_had_it_loses() {
        Map<Integer, DuelRules.Round> rounds = new HashMap<>();
        rounds.put(1, new DuelRules.Round(score(3, 1), null));

        DuelRules.State state = DuelRules.of(START, rounds, START.plusHours(1).plus(DuelRules.TURN).plusMinutes(1));
        assertTrue(state.over());
        assertTrue(state.forfeit());
        assertEquals(DuelRules.Side.CHALLENGER, state.winner());
    }

    @Test
    void given_the_same_right_answers_then_the_quicker_run_takes_the_round() {
        DuelRules.Score slow = new DuelRules.Score(3, 5, 60.0, START);
        DuelRules.Score quick = new DuelRules.Score(3, 5, 40.0, START);
        assertTrue(DuelRules.compare(quick, slow) > 0);
        assertTrue(DuelRules.compare(slow, quick) < 0);
    }
}
