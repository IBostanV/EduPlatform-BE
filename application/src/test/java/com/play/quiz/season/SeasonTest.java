package com.play.quiz.season;

import java.time.LocalDate;

import com.play.quiz.cosmetic.Cosmetic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeasonTest {

    @Test
    void given_days_when_placing_them_then_seasons_run_30_days_back_to_back() {
        assertEquals(new Season(1, Season.FIRST_DAY, Season.FIRST_DAY.plusDays(30)), Season.of(Season.FIRST_DAY));
        assertEquals(1, Season.of(Season.FIRST_DAY.plusDays(29)).number());
        Season second = Season.of(Season.FIRST_DAY.plusDays(30));
        assertEquals(2, second.number());
        assertEquals(Season.FIRST_DAY.plusDays(30), second.start());
    }

    @Test
    void given_any_day_then_its_week_starts_on_the_monday() {
        assertEquals(LocalDate.of(2026, 10, 5), Season.weekStart(LocalDate.of(2026, 10, 11)));
        assertEquals(LocalDate.of(2026, 10, 5), Season.weekStart(LocalDate.of(2026, 10, 5)));
    }

    @Test
    void given_the_track_then_tiers_5_and_10_pay_the_season_cosmetics() {
        assertEquals(Cosmetic.COLOR_SEASON, Season.itemFor(5).orElseThrow());
        assertEquals(Cosmetic.FRAME_CHAMPION, Season.itemFor(10).orElseThrow());
        assertTrue(Season.itemFor(3).isEmpty());
        assertTrue(Cosmetic.FRAME_CHAMPION.seasonal());
    }
}
