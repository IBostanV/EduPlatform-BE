package com.play.quiz.trophy;

import java.time.LocalDate;
import java.util.Collection;
import java.util.TreeSet;

/** Runs of consecutive days, worked out from the days something happened on. */
final class Streaks {

    private Streaks() {
    }

    /**
     * The longest run of consecutive days in the set — the best it has ever been, not the run
     * going on now, so a trophy earned in March survives a quiet April.
     */
    static int longestRun(final Collection<LocalDate> days) {
        int longest = 0;
        int running = 0;
        LocalDate previous = null;

        // Sorted and de-duplicated: two quizzes on one day are one day.
        for (LocalDate day : new TreeSet<>(days)) {
            running = day.minusDays(1).equals(previous) ? running + 1 : 1;
            previous = day;
            longest = Math.max(longest, running);
        }

        return longest;
    }
}
