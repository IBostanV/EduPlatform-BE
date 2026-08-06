package com.play.quiz.social;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.play.quiz.domain.Account;
import com.play.quiz.record.PlayerLevel;
import com.play.quiz.record.UserSummary;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.repository.UserQuizHistoryRepository.PlayerTotals;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The site-wide tables: who played the most, who is right most often, who answered the most right,
 * who has the highest level and the longest run of days. Read for everyone, guests included; a
 * signed-in reader also gets their own place when they are not in the top.
 *
 * <p>ponytail: each table is worked out over every active player and kept for a minute, so the
 * home page does not aggregate the whole history on every visit. Fine into the thousands of
 * players; past that, a table maintained as runs are recorded would replace the read.
 */
@Service
@RequiredArgsConstructor
public class LeaderboardService {

    static final int TOP = 10;
    static final Duration KEEP = Duration.ofMinutes(1);

    public enum Board { QUIZZES, ACCURACY, RIGHT_ANSWERS, LEVEL, STREAK }

    /** Level and streak are what a player has now, so they have no period: they read as ALL. */
    public enum Period {
        WEEK(7, 20), MONTH(30, 30), ALL(null, 50);

        final Integer days;
        /** Accuracy only ranks a player with at least this many answers: two out of two is not a record. */
        final int minAnswers;

        Period(final Integer days, final int minAnswers) {
            this.days = days;
            this.minAnswers = minAnswers;
        }
    }

    /**
     * @param value what the table is ranked by: quizzes, percent right, right answers, level or days
     * @param extra what goes with it: answers counted (accuracy), answers given (right answers), experience (level)
     */
    public record Entry(int rank, UserSummary user, long value, Long extra) {}

    public record Leaderboard(Board board, Period period, int players, Integer minAnswers, List<Entry> top, Entry you) {}

    private record Row(Long accountId, long value, Long extra, long tiebreak) {}

    private record Cached(Instant at, List<Row> rows) {}

    private final Map<String, Cached> cache = new ConcurrentHashMap<>();

    private final UserQuizHistoryRepository historyRepository;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final Clock clock;

    @Transactional(readOnly = true)
    public Leaderboard read(final Board board, final Period asked) {
        Period period = board == Board.LEVEL || board == Board.STREAK ? Period.ALL : asked;
        List<Row> rows = rows(board, period);
        Long me = currentAccountId();

        List<Row> top = rows.stream().limit(TOP).toList();
        int myIndex = me == null ? -1 : IntStream.range(0, rows.size())
                .filter(index -> me.equals(rows.get(index).accountId()))
                .findFirst().orElse(-1);

        List<Long> shown = Stream.concat(top.stream().map(Row::accountId),
                myIndex >= TOP ? Stream.of(me) : Stream.empty()).toList();
        Map<Long, UserSummary> people = accountRepository.findAllById(shown).stream()
                .collect(Collectors.toMap(Account::getAccountId, UserSummary::withPhoto, (first, second) -> first));

        List<Entry> entries = IntStream.range(0, top.size())
                .mapToObj(index -> entry(index, top.get(index), people))
                .filter(entry -> Objects.nonNull(entry.user()))
                .toList();
        Entry you = myIndex < 0 ? null : myIndex < TOP
                ? entries.stream().filter(entry -> me.equals(entry.user().id())).findFirst().orElse(null)
                : entry(myIndex, rows.get(myIndex), people);

        return new Leaderboard(board, period, rows.size(),
                board == Board.ACCURACY ? period.minAnswers : null, entries, you);
    }

    private List<Row> rows(final Board board, final Period period) {
        String key = board + "-" + period;
        Instant now = Instant.now(clock);
        Cached cached = cache.get(key);
        if (cached != null && cached.at().plus(KEEP).isAfter(now)) {
            return cached.rows();
        }
        List<Row> rows = compute(board, period);
        cache.put(key, new Cached(now, rows));
        return rows;
    }

    private List<Row> compute(final Board board, final Period period) {
        Comparator<Row> best = Comparator.comparingLong(Row::value).reversed()
                .thenComparing(Comparator.comparingLong(Row::tiebreak).reversed())
                .thenComparingLong(Row::accountId);

        if (board == Board.LEVEL || board == Board.STREAK) {
            return accountRepository.findStandings().stream()
                    .map(standing -> board == Board.LEVEL
                            ? new Row(standing.getAccountId(), PlayerLevel.levelFor(standing.getExperience().intValue()),
                                    standing.getExperience(), standing.getExperience())
                            : new Row(standing.getAccountId(), standing.getBestStreak(), null, standing.getExperience()))
                    .filter(row -> row.value() > 0)
                    .sorted(best)
                    .toList();
        }

        LocalDateTime since = Optional.ofNullable(period.days)
                .map(days -> LocalDateTime.now(clock).minusDays(days))
                .orElse(LocalDateTime.of(2000, 1, 1, 0, 0));
        List<PlayerTotals> totals = historyRepository.findPlayerTotalsSince(since);

        return totals.stream()
                .map(total -> {
                    long right = Optional.ofNullable(total.getRightAnswers()).orElse(0L);
                    long answers = Optional.ofNullable(total.getTotalAnswers()).orElse(0L);
                    return switch (board) {
                        case QUIZZES -> new Row(total.getAccountId(), total.getQuizzes(), null, right);
                        case RIGHT_ANSWERS -> new Row(total.getAccountId(), right, answers, total.getQuizzes());
                        default -> answers < period.minAnswers ? null
                                : new Row(total.getAccountId(), Math.round(right * 100.0 / answers), answers, answers);
                    };
                })
                .filter(Objects::nonNull)
                .filter(row -> row.value() > 0)
                .sorted(best)
                .toList();
    }

    private static Entry entry(final int index, final Row row, final Map<Long, UserSummary> people) {
        return new Entry(index + 1, people.get(row.accountId()), row.value(), row.extra());
    }

    // Read signed out too, so there may be nobody to find a place for.
    private Long currentAccountId() {
        try {
            return userService.findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
        } catch (RuntimeException exception) {
            return null;
        }
    }
}
