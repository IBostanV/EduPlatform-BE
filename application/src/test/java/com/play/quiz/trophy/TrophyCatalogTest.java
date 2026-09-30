package com.play.quiz.trophy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import com.play.quiz.domain.Category;
import com.play.quiz.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * What each trophy takes, and what counts toward it. The awarding around it is bookkeeping; this
 * is where a trophy's meaning actually lives.
 */
@ExtendWith(MockitoExtension.class)
class TrophyCatalogTest {

    private static final Long MOLDOVA = 42L;
    private static final Long HISTORY = 7L;

    @Mock
    private CategoryRepository categoryRepository;

    private TrophyCatalog catalog;

    @BeforeEach
    void setUp() {
        catalog = new TrophyCatalog(categoryRepository);
        when(categoryRepository.findAllActive(any())).thenReturn(List.of(
                Category.builder().catId(MOLDOVA).name("Moldova").build(),
                Category.builder().catId(HISTORY).name("History").build()));
    }

    private static PlayerStanding standing(final long quizzes, final Map<Long, Long> byCategory,
                                           final long countries, final long iqTests, final int bestIq,
                                           final int streak) {
        return new PlayerStanding(quizzes, byCategory, 0, 0, 0, countries, 0, 0, iqTests, bestIq,
                streak, streak);
    }

    private TrophyDefinition trophy(final String code) {
        return catalog.all().stream()
                .filter(definition -> definition.code().equals(code))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No trophy " + code));
    }

    @Test
    void given_a_new_player_then_nothing_is_earned_yet() {
        PlayerStanding fresh = standing(0, Map.of(), 0, 0, 0, 0);

        assertTrue(catalog.all().stream().noneMatch(definition -> definition.isEarnedBy(fresh)),
                "A player who has done nothing has earned nothing");
    }

    @Test
    void given_quizzes_played_then_the_ladder_is_climbed_one_rung_at_a_time() {
        PlayerStanding ten = standing(10, Map.of(), 0, 0, 0, 0);

        assertTrue(trophy("FIRST_QUIZ").isEarnedBy(ten));
        assertTrue(trophy("TEN_QUIZZES").isEarnedBy(ten));
        assertFalse(trophy("FIFTY_QUIZZES").isEarnedBy(ten));
        // Progress is shown against the target and never past it.
        assertEquals(10, trophy("FIFTY_QUIZZES").progressOf(ten));
        assertEquals(1, trophy("FIRST_QUIZ").progressOf(ten));
    }

    @Test
    void given_a_category_then_it_has_a_trophy_of_its_own() {
        TrophyDefinition moldova = trophy(TrophyCatalog.CATEGORY_PREFIX + MOLDOVA);

        assertEquals("Moldova", moldova.title());
        assertEquals(MOLDOVA, moldova.categoryId());
        assertEquals(TrophyGroup.CATEGORIES, moldova.group());
        assertTrue(moldova.isEarnedBy(standing(1, Map.of(MOLDOVA, 1L), 0, 0, 0, 0)));
        // Quizzes elsewhere do not count toward it, however many there are.
        assertFalse(moldova.isEarnedBy(standing(80, Map.of(7L, 80L), 0, 0, 0, 0)));
    }

    @Test
    void given_a_quiz_in_every_category_then_the_capstone_is_earned() {
        TrophyDefinition all = trophy("EVERY_CATEGORY");

        assertEquals(2, all.target(), "As many categories as there are");
        assertFalse(all.isEarnedBy(standing(9, Map.of(MOLDOVA, 9L), 0, 0, 0, 0)), "One of the two");
        assertTrue(all.isEarnedBy(standing(2, Map.of(MOLDOVA, 1L, HISTORY, 1L), 0, 0, 0, 0)));
        // A category nobody plays any more still counts for nothing toward it.
        assertFalse(all.isEarnedBy(standing(50, Map.of(MOLDOVA, 25L, 99L, 25L), 0, 0, 0, 0)));
    }

    @Test
    void given_a_conquest_or_a_test_then_those_trophies_follow() {
        assertTrue(trophy("CONQUEROR").isEarnedBy(standing(0, Map.of(), 1, 0, 0, 0)));
        assertFalse(trophy("WARLORD").isEarnedBy(standing(0, Map.of(), 4, 0, 0, 0)));
        assertTrue(trophy("WARLORD").isEarnedBy(standing(0, Map.of(), 5, 0, 0, 0)));
        assertFalse(trophy("EMPEROR").isEarnedBy(standing(0, Map.of(), 9, 0, 0, 0)));
        assertTrue(trophy("EMPEROR").isEarnedBy(standing(0, Map.of(), 10, 0, 0, 0)));
        assertTrue(trophy("MEASURED").isEarnedBy(standing(0, Map.of(), 0, 1, 104, 0)));
    }

    @Test
    void given_a_day_with_every_task_done_then_the_sweep_is_earned() {
        PlayerStanding swept = new PlayerStanding(3, Map.of(), 0, 0, 0, 0, 1, 0, 0, 0, 1, 1);

        assertTrue(trophy("CLEAN_SWEEP").isEarnedBy(swept));
        assertFalse(trophy("CLEAN_SWEEP").isEarnedBy(standing(3, Map.of(), 0, 0, 0, 1)));
    }

    @Test
    void given_a_quiz_finished_inside_a_minute_then_that_is_a_trophy() {
        PlayerStanding quick = new PlayerStanding(4, Map.of(), 0, 1, 0, 0, 0, 0, 0, 0, 0, 0);

        assertTrue(trophy("QUICK_OFF_THE_MARK").isEarnedBy(quick));
        assertFalse(trophy("QUICK_OFF_THE_MARK").isEarnedBy(standing(4, Map.of(), 0, 0, 0, 0)));
    }

    @Test
    void given_a_week_of_playing_then_that_is_its_own_trophy() {
        // Visiting for a week is one trophy; playing every one of those days is another.
        PlayerStanding visited = new PlayerStanding(2, Map.of(), 0, 0, 0, 0, 0, 0, 0, 0, 7, 7);
        PlayerStanding played = new PlayerStanding(9, Map.of(), 0, 0, 0, 0, 0, 7, 0, 0, 7, 7);

        assertFalse(trophy("PLAY_STREAK_7").isEarnedBy(visited));
        assertTrue(trophy("STREAK_7").isEarnedBy(visited));
        assertTrue(trophy("PLAY_STREAK_7").isEarnedBy(played));
    }

    @Test
    void given_a_run_of_days_then_the_best_run_is_what_counts() {
        // The run that was, not the run that is: a trophy earned in March survives a quiet April.
        PlayerStanding lapsed = new PlayerStanding(0, Map.of(), 0, 0, 0, 0, 0, 0, 0, 0, 1, 30);

        assertTrue(trophy("STREAK_3").isEarnedBy(lapsed));
        assertTrue(trophy("STREAK_7").isEarnedBy(lapsed));
        assertTrue(trophy("STREAK_30").isEarnedBy(lapsed));
        assertFalse(trophy("STREAK_100").isEarnedBy(lapsed));
    }

    @Test
    void given_the_secret_ones_then_they_are_marked_secret_and_the_rest_are_not() {
        assertTrue(trophy("SHARP").secret());
        assertTrue(trophy("NIGHT_OWL").secret());
        assertTrue(trophy("FLAWLESS").secret());
        assertFalse(trophy("FIRST_QUIZ").secret());
        assertFalse(trophy("QUICK_OFF_THE_MARK").secret());
        assertFalse(trophy(TrophyCatalog.CATEGORY_PREFIX + MOLDOVA).secret());

        // Secret ones are still earned by doing the thing, not by being told about it.
        assertTrue(trophy("SHARP").isEarnedBy(standing(0, Map.of(), 0, 1, 131, 0)));
        assertFalse(trophy("SHARP").isEarnedBy(standing(0, Map.of(), 0, 1, 129, 0)));
    }

    @Test
    void given_the_catalog_then_every_code_is_its_own() {
        List<String> codes = catalog.all().stream().map(TrophyDefinition::code).toList();

        assertEquals(codes.size(), codes.stream().distinct().count(), "Codes are what earned rows key on");
    }
}
