package com.play.quiz.record;

import java.util.Objects;

/**
 * A player's level, worked out from the experience they have collected.
 *
 * <p>Climbing costs more every time: level 2 asks for 100 experience, level 3 for another 200,
 * level 4 for another 300, and so on — {@code STEP * level} to leave level {@code level}. Adding
 * that up, the total needed to <em>reach</em> a level is {@code STEP / 2 * level * (level - 1)}:
 * 0, 100, 300, 600, 1000, 1500 …
 *
 * <p>{@code intoLevel} and {@code forNextLevel} are what a progress bar needs: how far into the
 * current level the player is, and how wide that level is.
 */
public record PlayerLevel(int experience, int level, int intoLevel, int forNextLevel) {

    /** Experience to leave level 1. Every level after it costs this much more than the last. */
    private static final int STEP = 100;

    public static PlayerLevel of(final Integer experience) {
        // No row has been written yet for a player who has not finished a quiz.
        int collected = Math.max(0, Objects.isNull(experience) ? 0 : experience);
        int level = levelFor(collected);
        int reached = totalForLevel(level);

        return new PlayerLevel(collected, level, collected - reached, totalForLevel(level + 1) - reached);
    }

    /** The highest level this much experience pays for; everyone starts at level 1. */
    public static int levelFor(final int experience) {
        // Counted up rather than solved with a square root: at these sizes the loop is nothing,
        // and it cannot land a player one level short when the total is exactly on a boundary.
        int level = 1;
        while (experience >= totalForLevel(level + 1)) {
            level++;
        }
        return level;
    }

    /** Total experience needed to reach a level, starting from 0 at level 1. */
    public static int totalForLevel(final int level) {
        return STEP / 2 * level * (level - 1);
    }
}
