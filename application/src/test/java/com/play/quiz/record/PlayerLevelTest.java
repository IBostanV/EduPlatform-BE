package com.play.quiz.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PlayerLevelTest {

    @Test
    void given_no_experience_then_level_one_at_the_start_of_the_bar() {
        PlayerLevel fresh = PlayerLevel.of(null);

        assertEquals(0, fresh.experience());
        assertEquals(1, fresh.level());
        assertEquals(0, fresh.intoLevel());
        assertEquals(100, fresh.forNextLevel());
        // Same as an account that has a row but has never finished a quiz.
        assertEquals(fresh, PlayerLevel.of(0));
    }

    @Test
    void given_experience_then_each_level_costs_more_than_the_last() {
        assertEquals(0, PlayerLevel.totalForLevel(1));
        assertEquals(100, PlayerLevel.totalForLevel(2));
        assertEquals(300, PlayerLevel.totalForLevel(3));
        assertEquals(600, PlayerLevel.totalForLevel(4));
        assertEquals(1000, PlayerLevel.totalForLevel(5));

        int previousStep = 0;
        for (int level = 2; level <= 50; level++) {
            int step = PlayerLevel.totalForLevel(level) - PlayerLevel.totalForLevel(level - 1);
            assertTrue(step > previousStep, "Level " + level + " must cost more than the one before");
            previousStep = step;
        }
    }

    @Test
    void given_a_total_exactly_on_a_boundary_then_the_level_is_reached_not_missed() {
        for (int level = 1; level <= 50; level++) {
            int exact = PlayerLevel.totalForLevel(level);
            assertEquals(level, PlayerLevel.levelFor(exact), "Exactly enough must reach level " + level);
            if (level > 1) {
                assertEquals(level - 1, PlayerLevel.levelFor(exact - 1), "One short must stay below " + level);
            }
        }
    }

    @Test
    void given_any_total_then_the_bar_never_runs_past_its_end() {
        for (int experience = 0; experience <= 5000; experience++) {
            PlayerLevel progress = PlayerLevel.of(experience);

            assertTrue(progress.intoLevel() >= 0, "Negative progress at " + experience);
            assertTrue(progress.intoLevel() < progress.forNextLevel(), "Full bar not levelled at " + experience);
            // The bar starts where the level was reached.
            assertEquals(experience, PlayerLevel.totalForLevel(progress.level()) + progress.intoLevel());
        }
    }

    @Test
    void given_a_negative_total_then_it_is_read_as_none() {
        assertEquals(PlayerLevel.of(0), PlayerLevel.of(-50));
    }
}
