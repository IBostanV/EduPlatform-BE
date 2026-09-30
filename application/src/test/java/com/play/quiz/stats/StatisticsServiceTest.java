package com.play.quiz.stats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Category;
import com.play.quiz.domain.Quiz;
import com.play.quiz.domain.UserQuizHistory;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import com.play.quiz.trophy.EarnedTrophyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.User;

/**
 * The arithmetic a player is shown about themselves: the counts, the averages, what is named as a
 * strength, and the comparison with the week before.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StatisticsServiceTest {

    private static final Long ACCOUNT = 5L;
    private static final Category HISTORY = Category.builder().catId(1L).name("History").build();
    private static final Category SCIENCE = Category.builder().catId(2L).name("Science").build();

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private UserQuizHistoryRepository historyRepository;
    @Mock
    private EarnedTrophyRepository trophyRepository;

    private StatisticsService service;

    @BeforeEach
    void setUp() {
        service = new StatisticsService(userService, authenticationFacade, historyRepository, trophyRepository);
        Account player = Account.builder().accountId(ACCOUNT).email("player@play.quiz").loginStreak(4).build();
        when(authenticationFacade.getPrincipal())
                .thenReturn(new User("player@play.quiz", "", List.of()));
        when(userService.findByEmail("player@play.quiz")).thenReturn(player);
        when(trophyRepository.countByAccountIdAndEarnedDateBetween(anyLong(), any(), any())).thenReturn(2L);
    }

    private static UserQuizHistory run(final Category category, final int right, final int total,
                                       final double seconds, final LocalDateTime when) {
        return UserQuizHistory.builder()
                .quiz(Quiz.builder().category(category).build())
                .rightAnswers(right)
                .totalAnswers(total)
                .spentTime(seconds)
                .completedDate(when)
                .build();
    }

    /** Runs land in the window asked for; anything older is the comparison period. */
    private void given(final List<UserQuizHistory> thisPeriod, final List<UserQuizHistory> before) {
        LocalDate today = LocalDate.now();
        LocalDateTime windowStart = today.minusDays(6).atStartOfDay();
        when(historyRepository.findRunsBetween(eq(ACCOUNT), eq(windowStart), any())).thenReturn(thisPeriod);
        when(historyRepository.findRunsBetween(eq(ACCOUNT), eq(today.minusDays(13).atStartOfDay()),
                eq(windowStart))).thenReturn(before);
    }

    @Test
    void given_a_week_of_quizzes_then_the_counts_and_averages_follow() {
        LocalDateTime yesterday = LocalDate.now().minusDays(1).atTime(10, 0);
        given(List.of(run(HISTORY, 8, 10, 100, yesterday), run(HISTORY, 5, 10, 200, yesterday)), List.of());

        PeriodStatistics stats = service.of(StatisticsPeriod.WEEK);

        assertEquals(2, stats.quizzes());
        assertEquals(13, stats.rightAnswers());
        assertEquals(7, stats.wrongAnswers());
        assertEquals(65, stats.accuracy());
        // 300 seconds over 20 questions, and over two runs.
        assertEquals(15.0, stats.secondsPerQuestion());
        assertEquals(150.0, stats.secondsPerQuiz());
        assertEquals(5.0, stats.minutesPlayed());
        assertEquals(2, stats.trophies());
        assertEquals(1, stats.activeDays());
        assertEquals(4, stats.loginStreak());
    }

    @Test
    void given_two_subjects_then_the_stronger_and_the_weaker_are_named() {
        LocalDateTime when = LocalDate.now().minusDays(2).atTime(9, 0);
        given(List.of(
                run(HISTORY, 18, 20, 200, when),
                run(SCIENCE, 6, 20, 200, when)), List.of());

        PeriodStatistics stats = service.of(StatisticsPeriod.WEEK);

        assertEquals("History", stats.strongest().get(0).name());
        assertEquals(90, stats.strongest().get(0).accuracy());
        assertEquals("Science", stats.weakest().get(0).name());
        assertEquals(30, stats.weakest().get(0).accuracy());
    }

    @Test
    void given_too_few_questions_in_a_subject_then_it_is_not_called_a_weakness() {
        // Four questions in one subject is bad luck, not a weak point.
        LocalDateTime when = LocalDate.now().minusDays(2).atTime(9, 0);
        given(List.of(run(HISTORY, 18, 20, 200, when), run(SCIENCE, 1, 4, 40, when)), List.of());

        PeriodStatistics stats = service.of(StatisticsPeriod.WEEK);

        assertTrue(stats.weakest().isEmpty(), "Only one subject has been answered enough to judge");
        assertTrue(stats.strongest().isEmpty());
        // It still counts as played, and History is still what was played most.
        assertEquals("History", stats.topCategory().name());
    }

    @Test
    void given_the_period_before_then_the_change_is_worked_out() {
        LocalDateTime when = LocalDate.now().minusDays(1).atTime(9, 0);
        LocalDateTime earlier = LocalDate.now().minusDays(9).atTime(9, 0);
        given(List.of(run(HISTORY, 9, 10, 100, when), run(HISTORY, 9, 10, 100, when)),
                List.of(run(HISTORY, 5, 10, 100, earlier)));

        PeriodStatistics stats = service.of(StatisticsPeriod.WEEK);

        assertEquals(1, stats.trend().quizzesBefore());
        assertEquals(50, stats.trend().accuracyBefore());
        assertEquals(100, stats.trend().quizzesChange(), "Two quizzes against one");
        assertEquals(40, stats.trend().accuracyChange(), "90% against 50%");
    }

    @Test
    void given_a_first_week_then_there_is_nothing_to_compare_against() {
        given(List.of(run(HISTORY, 9, 10, 100, LocalDate.now().atTime(9, 0))), List.of());

        PeriodStatistics stats = service.of(StatisticsPeriod.WEEK);

        assertNull(stats.trend().quizzesChange());
        assertNull(stats.trend().accuracyChange());
    }

    @Test
    void given_a_week_then_every_day_of_it_is_listed_even_the_empty_ones() {
        given(List.of(run(HISTORY, 9, 10, 100, LocalDate.now().atTime(9, 0))), List.of());

        PeriodStatistics stats = service.of(StatisticsPeriod.WEEK);

        assertEquals(7, stats.byDay().size(), "A strip with gaps in it reads as a calendar");
        assertEquals(LocalDate.now(), stats.byDay().get(6).day());
        assertEquals(1, stats.byDay().get(6).quizzes());
        assertEquals(0, stats.byDay().get(0).quizzes());
    }

    @Test
    void given_no_quizzes_then_nothing_divides_by_nought() {
        given(List.of(), List.of());

        PeriodStatistics stats = service.of(StatisticsPeriod.DAY);

        assertEquals(0, stats.quizzes());
        assertEquals(0, stats.accuracy());
        assertEquals(0.0, stats.secondsPerQuestion());
        assertEquals(0.0, stats.secondsPerQuiz());
        assertNull(stats.topCategory());
    }

    @Test
    void given_another_players_id_then_their_runs_are_counted() {
        Long other = 9L;
        when(userService.getProfileAccount(other))
                .thenReturn(Account.builder().accountId(other).loginStreak(7).build());
        LocalDateTime yesterday = LocalDate.now().minusDays(1).atTime(10, 0);
        when(historyRepository.findRunsBetween(eq(other), eq(LocalDate.now().minusDays(6).atStartOfDay()), any()))
                .thenReturn(List.of(run(HISTORY, 3, 4, 60, yesterday)));

        PeriodStatistics stats = service.ofAccount(other, StatisticsPeriod.WEEK);

        assertEquals(1, stats.quizzes());
        assertEquals(7, stats.loginStreak());
    }
}
