package com.play.quiz.appearance;

import com.play.quiz.util.ServerText;

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
 * @param background   the picture behind the site: one of {@link #BACKGROUNDS} (files the site
 *                     serves itself), {@link #CUSTOM} for the player's own upload, or null for the
 *                     site's logo
 * @param customBackground the address part of the player's uploaded picture, if they have one.
 *                     Filled in by the server; whatever a browser sends for it is ignored
 * @param hideTourLink no site-tour compass in the navbar
 */
public record Appearance(String accent,
                         Integer textSize,
                         Motion motion,
                         Boolean compactNav,
                         Side friendsDock,
                         HomeFriends homeFriends,
                         String accentText,
                         String background,
                         String customBackground,
                         Boolean hideTourLink) {

    public static final Set<Integer> TEXT_SIZES = Set.of(90, 100, 110, 125);
    private static final Pattern COLOUR = Pattern.compile("^#[0-9a-fA-F]{6}$");
    /** The pictures the site ships, by name: public/backgrounds/<name>.svg on the front end. */
    public static final Set<String> BACKGROUNDS = Set.of("aurora", "constellation", "topography", "waves",
            "hexagons", "dunes");
    public static final String CUSTOM = "custom";

    public enum Motion { ON, OFF }

    public enum Side { LEFT, RIGHT }

    public enum HomeFriends { RIGHT, LEFT, BELOW }

    /** The site as it comes. */
    public static Appearance defaults() {
        return new Appearance(null, null, null, null, null, null, null, null, null, null);
    }

    public Appearance withBackground(final String picture) {
        return new Appearance(accent, textSize, motion, compactNav, friendsDock, homeFriends, accentText,
                picture, customBackground, hideTourLink);
    }

    public Appearance withCustomBackground(final String publicId) {
        return new Appearance(accent, textSize, motion, compactNav, friendsDock, homeFriends, accentText,
                background, publicId, hideTourLink);
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
            throw new IllegalArgumentException(ServerText.t("err_accent_colour", "The accent must be a colour like #00a8e8"));
        }
        if (Objects.nonNull(accentText) && !COLOUR.matcher(accentText).matches()) {
            throw new IllegalArgumentException(ServerText.t("err_accent_text_colour", "The text on the accent must be a colour like #ffffff"));
        }
        if (Objects.nonNull(background) && !BACKGROUNDS.contains(background) && !CUSTOM.equals(background)) {
            throw new IllegalArgumentException(ServerText.t("err_background", "That background is not one of the site's"));
        }
        if (Objects.nonNull(textSize) && !TEXT_SIZES.contains(textSize)) {
            throw new IllegalArgumentException(ServerText.t("err_text_size", "The text size must be one of {{sizes}}", "sizes", TEXT_SIZES));
        }
        return this;
    }
}
