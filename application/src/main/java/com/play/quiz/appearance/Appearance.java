package com.play.quiz.appearance;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * How a player has changed the look of the site for themselves. Every field is optional: null is
 * the site as it comes, so resetting one setting is sending it back as null, and resetting all of
 * them is sending none.
 *
 * @param accent       the accent colour, as {@code #rrggbb}
 * @param textSize     text size as a percentage of the usual: one of {@link #TEXT_SIZES}
 * @param motion       animations always on or always off; null follows the system's
 *                     "reduce motion" setting, as the site always has
 * @param compactNav   icons only in the main navigation, at every width
 * @param friendsDock  which bottom corner the friends panel sits in
 * @param homeFriends  where friends' news sits on the home page, next to the rest of the news
 * @param accentText   the colour of text on the accent (buttons, highlights), as {@code #rrggbb};
 *                     null leaves it to the site, which turns it dark on a very light accent
 */
public record Appearance(String accent,
                         Integer textSize,
                         Motion motion,
                         Boolean compactNav,
                         Side friendsDock,
                         HomeFriends homeFriends,
                         String accentText) {

    public static final Set<Integer> TEXT_SIZES = Set.of(90, 100, 110, 125);
    private static final Pattern COLOUR = Pattern.compile("^#[0-9a-fA-F]{6}$");

    public enum Motion { ON, OFF }

    public enum Side { LEFT, RIGHT }

    public enum HomeFriends { RIGHT, LEFT, BELOW }

    /** The site as it comes. */
    public static Appearance defaults() {
        return new Appearance(null, null, null, null, null, null, null);
    }

    // Not a setting: kept out of the JSON, or it would be stored and sent as one.
    @JsonIgnore
    public boolean isDefault() {
        return equals(defaults());
    }

    /**
     * Refuses what the browser cannot use. The colour goes into a style rule as it is, so it is
     * held to one exact shape rather than trusted.
     *
     * @throws IllegalArgumentException naming the first setting that is not one
     */
    public Appearance validated() {
        if (Objects.nonNull(accent) && !COLOUR.matcher(accent).matches()) {
            throw new IllegalArgumentException("The accent must be a colour like #00a8e8");
        }
        if (Objects.nonNull(accentText) && !COLOUR.matcher(accentText).matches()) {
            throw new IllegalArgumentException("The text on the accent must be a colour like #ffffff");
        }
        if (Objects.nonNull(textSize) && !TEXT_SIZES.contains(textSize)) {
            throw new IllegalArgumentException("The text size must be one of " + TEXT_SIZES);
        }
        return this;
    }
}
