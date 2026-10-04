package com.play.quiz.controller;

import java.time.DateTimeException;
import java.time.ZoneId;

/**
 * The player's time zone, as the browser sends it on every request ({@code X-Time-Zone}), so
 * "today" (the visit streak, the daily tasks) is the player's own day. Without one, or with
 * nonsense, it is the server's.
 */
public final class PlayerZone {

    public static final String HEADER = "X-Time-Zone";

    private PlayerZone() {
    }

    public static ZoneId of(final String timeZone) {
        if (timeZone == null || timeZone.isBlank()) return ZoneId.systemDefault();
        try {
            return ZoneId.of(timeZone);
        } catch (DateTimeException e) {
            return ZoneId.systemDefault();
        }
    }
}
