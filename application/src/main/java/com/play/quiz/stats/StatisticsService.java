package com.play.quiz.stats;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Category;
import com.play.quiz.domain.UserQuizHistory;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import com.play.quiz.stats.PeriodStatistics.CategoryStat;
import com.play.quiz.stats.PeriodStatistics.DayStat;
import com.play.quiz.stats.PeriodStatistics.Trend;
import com.play.quiz.trophy.EarnedTrophyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A player's own statistics over a day, a week or a month.
 *
 * <p>The runs for the window are read once and everything is worked out from them in memory. That
 * is deliberate: a month of one player's quizzes is a handful of rows, and five group-by queries
 * would be five chances for the counts on one screen to disagree with each other.
 *
 * <p>The one number here that is not a count is the comparison with the period before, which is
 * what makes the rest of them mean anything.
 */
@Service
@RequiredArgsConstructor
public class StatisticsService {

    /**
     * How many questions a category needs before its accuracy is called a strength or a weakness.
     * Below it, one unlucky quiz would name somebody's weakest subject.
     */
    private static final int ENOUGH_TO_JUDGE = 10;

    /** How many subjects to name on each side; more than this is a list, not an observation. */
    private static final int NAMED = 3;

    /**
     * Which subject was played most: quizzes first, then questions answered, then the name.
     * Two subjects with one quiz each are a tie, and a tie has to break the same way every time
     * or the page says something different on each reload.
     */
    private static final Comparator<CategoryStat> MOST_PLAYED =
            Comparator.comparingLong(CategoryStat::quizzes)
                    .thenComparingLong(CategoryStat::totalAnswers)
                    .thenComparing(Comparator.comparing(CategoryStat::name).reversed());

    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final UserQuizHistoryRepository historyRepository;
    private final EarnedTrophyRepository trophyRepository;

    @Transactional(readOnly = true)
    public PeriodStatistics of(final StatisticsPeriod period) {
        return of(userService.findByEmail(authenticationFacade.getPrincipal().getUsername()), period);
    }

    /** Another player's statistics, for their profile page: the same numbers, no runs themselves. */
    @Transactional(readOnly = true)
    public PeriodStatistics ofAccount(final Long accountId, final StatisticsPeriod period) {
        return of(userService.getProfileAccount(accountId), period);
    }

    private PeriodStatistics of(final Account player, final StatisticsPeriod period) {
        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(period.days() - 1L);

        List<UserQuizHistory> runs = runsBetween(player, from, today.plusDays(1));
        // The same stretch again, immediately before it: "43 quizzes" says little without it.
        List<UserQuizHistory> before = runsBetween(player, from.minusDays(period.days()), from);

        long right = sum(runs, UserQuizHistory::getRightAnswers);
        long asked = sum(runs, UserQuizHistory::getTotalAnswers);
        double seconds = runs.stream()
                .map(UserQuizHistory::getSpentTime)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
        long timedRuns = runs.stream().filter(run -> Objects.nonNull(run.getSpentTime())).count();

        List<CategoryStat> categories = byCategory(runs);

        return new PeriodStatistics(period, from, today,
                runs.size(), right, Math.max(0, asked - right), percent(right, asked),
                round(asked == 0 ? 0 : seconds / asked),
                round(timedRuns == 0 ? 0 : seconds / timedRuns),
                round(seconds / 60),
                trophyRepository.countByAccountIdAndEarnedDateBetween(player.getAccountId(),
                        from.atStartOfDay(), today.plusDays(1).atStartOfDay()),
                runs.stream().map(run -> run.getCompletedDate().toLocalDate()).distinct().count(),
                player.getLoginStreak(),
                categories.stream().max(MOST_PLAYED).orElse(null),
                judged(categories, Comparator.comparingInt(CategoryStat::accuracy).reversed()),
                judged(categories, Comparator.comparingInt(CategoryStat::accuracy)),
                byDay(runs, from, period.days()),
                trendOf(runs, before));
    }

    private List<UserQuizHistory> runsBetween(final Account player, final LocalDate from, final LocalDate to) {
        return historyRepository.findRunsBetween(player.getAccountId(), from.atStartOfDay(), to.atStartOfDay())
                .stream()
                // A run that could not be marked has no score to count; it is still a quiz played.
                .filter(run -> Objects.nonNull(run.getCompletedDate()))
                .toList();
    }

    /** How each category went, for the subject that was played most and for the two lists. */
    private List<CategoryStat> byCategory(final List<UserQuizHistory> runs) {
        Map<Category, List<UserQuizHistory>> grouped = runs.stream()
                .filter(run -> Objects.nonNull(run.getQuiz().getCategory()))
                .collect(Collectors.groupingBy(run -> run.getQuiz().getCategory()));

        return grouped.entrySet().stream()
                .map(entry -> {
                    long right = sum(entry.getValue(), UserQuizHistory::getRightAnswers);
                    long asked = sum(entry.getValue(), UserQuizHistory::getTotalAnswers);
                    return new CategoryStat(entry.getKey().getCatId(), entry.getKey().getName(),
                            entry.getValue().size(), right, asked, percent(right, asked));
                })
                .toList();
    }

    /**
     * The categories worth naming, best or worst first.
     *
     * <p>Only those answered enough times to mean anything, and only when there is more than one
     * of them: with a single subject there is nothing to be strong or weak <em>at</em>.
     */
    private List<CategoryStat> judged(final List<CategoryStat> categories, final Comparator<CategoryStat> order) {
        List<CategoryStat> enough = categories.stream()
                .filter(category -> category.totalAnswers() >= ENOUGH_TO_JUDGE)
                .sorted(order)
                .toList();

        return enough.size() < 2 ? List.of() : enough.stream().limit(NAMED).toList();
    }

    /** Every day of the period, empty ones included, so the strip reads as a calendar. */
    private List<DayStat> byDay(final List<UserQuizHistory> runs, final LocalDate from, final int days) {
        Map<LocalDate, List<UserQuizHistory>> grouped = runs.stream()
                .collect(Collectors.groupingBy(run -> run.getCompletedDate().toLocalDate()));

        return LongStream.range(0, days)
                .mapToObj(from::plusDays)
                .map(day -> {
                    List<UserQuizHistory> onDay = grouped.getOrDefault(day, List.of());
                    return new DayStat(day, onDay.size(), sum(onDay, UserQuizHistory::getRightAnswers),
                            sum(onDay, UserQuizHistory::getTotalAnswers));
                })
                .toList();
    }

    private Trend trendOf(final List<UserQuizHistory> runs, final List<UserQuizHistory> before) {
        long wasQuizzes = before.size();
        int wasAccuracy = percent(sum(before, UserQuizHistory::getRightAnswers),
                sum(before, UserQuizHistory::getTotalAnswers));
        int accuracy = percent(sum(runs, UserQuizHistory::getRightAnswers),
                sum(runs, UserQuizHistory::getTotalAnswers));

        // Nothing to compare against reads as no change at all, rather than as an infinite rise.
        Integer quizzesChange = wasQuizzes == 0 ? null
                : (int) Math.round((runs.size() - wasQuizzes) * 100.0 / wasQuizzes);
        Integer accuracyChange = before.isEmpty() ? null : accuracy - wasAccuracy;

        return new Trend(wasQuizzes, wasAccuracy, quizzesChange, accuracyChange);
    }

    private static long sum(final List<UserQuizHistory> runs,
                            final java.util.function.Function<UserQuizHistory, Integer> field) {
        return runs.stream()
                .map(field)
                .filter(Objects::nonNull)
                .mapToLong(Integer::longValue)
                .sum();
    }

    private static int percent(final long part, final long whole) {
        return whole == 0 ? 0 : (int) Math.round(part * 100.0 / whole);
    }

    private static double round(final double value) {
        return Math.round(value * 10) / 10.0;
    }

    /** Guards against a period asked for by a name nobody defined. */
    public static StatisticsPeriod periodOf(final String name) {
        return Optional.ofNullable(name)
                .map(String::toUpperCase)
                .filter(value -> java.util.Arrays.stream(StatisticsPeriod.values())
                        .anyMatch(period -> period.name().equals(value)))
                .map(StatisticsPeriod::valueOf)
                .orElse(StatisticsPeriod.WEEK);
    }
}
