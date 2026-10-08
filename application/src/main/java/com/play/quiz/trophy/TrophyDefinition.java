package com.play.quiz.trophy;

import java.util.Locale;
import java.util.Objects;
import java.util.function.ToLongFunction;

import com.play.quiz.util.ServerText;

/**
 * One trophy: what it is called, what it takes, and how far along a player is.
 *
 * <p>Definitions live in code rather than in a table because the interesting half of a trophy is
 * the {@code measure} — "how many quizzes has this player finished", "have they ever held a
 * country" — and a row in a table cannot hold that without something to interpret it. What the
 * database keeps is the other half: who has earned what, and when.
 *
 * @param group   what the trophy belongs with on screen
 * @param secret  hidden until it is earned: neither name nor condition is sent before that
 * @param target  how much of {@code measure} it takes; progress is shown against it
 * @param categoryId the category a category trophy belongs to, else null — the browser draws that
 *                   category's own artwork on it
 */
public record TrophyDefinition(String code,
                               TrophyGroup group,
                               String title,
                               String description,
                               String icon,
                               boolean secret,
                               long target,
                               Long categoryId,
                               ToLongFunction<PlayerStanding> measure) {

    // Name and condition in the player's language, keyed by code (trophy_first_quiz_title, ...).
    // A category trophy is named after its category, which is data; only its condition is text.
    @Override
    public String title() {
        return Objects.nonNull(categoryId) ? title : ServerText.t(key("title"), title);
    }

    @Override
    public String description() {
        return Objects.nonNull(categoryId)
                ? ServerText.t("trophy_category_desc", "Finish a quiz in {{category}}", "category", title)
                : ServerText.t(key("desc"), description);
    }

    private String key(final String part) {
        return "trophy_" + code.toLowerCase(Locale.ROOT) + "_" + part;
    }

    /** Progress toward it, never past the target. */
    public long progressOf(final PlayerStanding standing) {
        return Math.min(target, measure.applyAsLong(standing));
    }

    public boolean isEarnedBy(final PlayerStanding standing) {
        return measure.applyAsLong(standing) >= target;
    }
}
