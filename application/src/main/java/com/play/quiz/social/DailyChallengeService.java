package com.play.quiz.social;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.play.quiz.domain.Account;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.record.UserSummary;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.repository.UserQuizHistoryRepository.FirstRun;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.QuizService;
import com.play.quiz.service.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The same ten questions for everyone, one set a day (UTC), and a table of the day's best. The
 * day's quiz is stored on the first read of the day rather than by a job, the project having no
 * working scheduler; after that every run is ordinary quiz history under that quiz id, and only a
 * player's first run of it counts.
 */
@Service
@RequiredArgsConstructor
public class DailyChallengeService {

    static final int QUESTIONS = 10;
    /** How many of the day's best the table shows. */
    static final int TOP = 10;

    private final DailyChallengeDays days;
    private final UserQuizHistoryRepository historyRepository;
    private final AccountRepository accountRepository;
    private final QuizService quizService;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final Clock clock;

    public record Entry(int rank, UserSummary user, int rightAnswers, int totalAnswers, Double spentTime) {}

    /**
     * @param you     the reader's own place, or null before they have played
     * @param closesAt when the day ends and a new set of questions takes over, as epoch ms: the
     *                 day is UTC's, which a local date-time would not say
     */
    public record Status(LocalDate day, int questions, int players, Entry you, List<Entry> top,
                         long closesAt) {

        @JsonProperty
        public boolean played() {
            return you != null;
        }
    }

    @Transactional(readOnly = true)
    public Status status() {
        LocalDate day = LocalDate.now(clock);
        Long me = currentAccountId();
        List<FirstRun> runs = historyRepository.findFirstRuns(days.quizIdFor(day));

        List<Long> shown = IntStream.range(0, runs.size())
                .filter(index -> index < TOP || me.equals(runs.get(index).getAccountId()))
                .mapToObj(index -> runs.get(index).getAccountId())
                .toList();
        Map<Long, UserSummary> people = accountRepository.findAllById(shown).stream()
                .collect(Collectors.toMap(Account::getAccountId, UserSummary::withPhoto, (first, second) -> first));

        List<Entry> entries = IntStream.range(0, runs.size())
                .filter(index -> people.containsKey(runs.get(index).getAccountId()))
                .mapToObj(index -> entry(index + 1, runs.get(index), people))
                .toList();

        return new Status(day, QUESTIONS, runs.size(),
                entries.stream().filter(entry -> me.equals(entry.user().id())).findFirst().orElse(null),
                entries.stream().filter(entry -> entry.rank() <= TOP).toList(),
                day.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
    }

    /** Today's questions, once: a second go would only be practice, and it would not count. */
    @Transactional(readOnly = true)
    public QuizDto quiz() {
        Long quizId = days.quizIdFor(LocalDate.now(clock));
        if (historyRepository.findFirstByQuiz_QuizIdAndAccount_AccountIdOrderByHistoryIdAsc(quizId, currentAccountId()).isPresent()) {
            throw new IllegalArgumentException("You have played today's challenge; a new one starts tomorrow");
        }
        return quizService.replay(quizId);
    }

    private static Entry entry(final int rank, final FirstRun run, final Map<Long, UserSummary> people) {
        return new Entry(rank, people.get(run.getAccountId()), run.getRightAnswers(), run.getTotalAnswers(),
                run.getSpentTime());
    }

    private Long currentAccountId() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
    }
}
