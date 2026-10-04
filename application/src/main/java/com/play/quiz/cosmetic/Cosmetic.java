package com.play.quiz.cosmetic;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;

/**
 * Everything a player can wear, in code: what it is, what it costs, and how it looks. A frame's
 * look is drawn by the browser from its code (styles/_engage.scss); a name colour is the colour
 * itself, so the server can send it ready to use. Only what a player owns and wears is stored.
 *
 * <p>A null price is a season-pass reward: it cannot be bought.
 */
@Getter
public enum Cosmetic {

    FRAME_SKY(Type.FRAME, 120, null),
    FRAME_BRONZE(Type.FRAME, 200, null),
    FRAME_SILVER(Type.FRAME, 350, null),
    FRAME_NEON(Type.FRAME, 450, null),
    FRAME_GOLD(Type.FRAME, 600, null),
    FRAME_RAINBOW(Type.FRAME, 900, null),
    FRAME_CHAMPION(Type.FRAME, null, null),

    COLOR_SKY(Type.NAME_COLOR, 120, "#7fd8ff"),
    COLOR_MINT(Type.NAME_COLOR, 200, "#5fe3b3"),
    COLOR_CORAL(Type.NAME_COLOR, 200, "#ff8a7a"),
    COLOR_VIOLET(Type.NAME_COLOR, 250, "#b69cff"),
    COLOR_GOLD(Type.NAME_COLOR, 300, "#f5c95a"),
    COLOR_SEASON(Type.NAME_COLOR, null, "#ff9ff3");

    public enum Type { FRAME, NAME_COLOR }

    private final Type type;
    private final Integer price;
    /** The name colour, as {@code #rrggbb}; null for a frame. */
    private final String color;

    Cosmetic(final Type type, final Integer price, final String color) {
        this.type = type;
        this.price = price;
        this.color = color;
    }

    public boolean seasonal() {
        return price == null;
    }

    public static Optional<Cosmetic> of(final String code) {
        return Arrays.stream(values()).filter(item -> item.name().equals(code)).findFirst();
    }

    /** The colour a name is drawn in, from the code a player wears; null for none or an unknown one. */
    public static String colorOf(final String code) {
        return of(code).map(Cosmetic::getColor).orElse(null);
    }
}
