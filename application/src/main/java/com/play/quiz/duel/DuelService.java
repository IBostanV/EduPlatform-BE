package com.play.quiz.duel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.play.quiz.domain.Account;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.feed.FeedItem;
import com.play.quiz.record.UserSummary;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.QuizService;
import com.play.quiz.service.UserService;
import com.play.quiz.util.ServerText;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Turn-based duels between friends (rules in {@link DuelRules}). A round is played on the quiz page
 * like a challenge: the round's stored quiz, replayed, and a player's score in it is their first
 * run of that quiz. So saving a run needs to know nothing about duels.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class DuelService {

    /** How long a finished duel stays on the list. */
    static final int SHOWN_DAYS = 30;

    private final DuelRepository duelRepository;
    private final DuelRoundRepository roundRepository;
    private final DuelRounds duelRounds;
    private final UserQuizHistoryRepository historyRepository;
    private final AccountRepository accountRepository;
    private final QuizService quizService;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    public record RoundView(int no, DuelRules.Score challenger, DuelRules.Score opponent) {}

    /**
     * A duel as one of its players sees it. {@code me} says which side the reader is on, and
     * {@code outcome} is from their side: YOUR_TURN, THEIR_TURN, WON, LOST or DRAW.
     */
    public record DuelView(Long id, UserSummary challenger, UserSummary opponent, DuelRules.Side me,
                           List<RoundView> rounds, DuelRules.State state, String outcome,
                           long deadlineMillis, LocalDateTime createdDate) {}

    @Transactional(readOnly = true)
    public List<DuelView> mine() {
        Long me = currentAccountId();
        LocalDateTime shownSince = LocalDateTime.now().minusDays(SHOWN_DAYS);
        return views(duelRepository.findInvolving(me), me).stream()
                .filter(view -> !view.state().over() || view.state().endedAt().isAfter(shownSince))
                .sorted(Comparator.comparing((DuelView view) -> !"YOUR_TURN".equals(view.outcome()))
                        .thenComparing(view -> view.state().over()))
                .toList();
    }

    /** Starts a duel with a friend, or hands back the one already going between the two. */
    @Transactional
    public DuelView start(final Long opponentId) {
        Long me = currentAccountId();
        boolean friends = accountRepository.findFriends(me).stream()
                .anyMatch(friend -> Objects.equals(friend.getAccountId(), opponentId));
        if (!friends) {
            throw new IllegalArgumentException(ServerText.t("err_duel_only_friends", "You can only duel your friends"));
        }
        List<DuelView> going = views(duelRepository.findInvolving(me), me).stream()
                .filter(view -> !view.state().over())
                .filter(view -> Objects.equals(view.challenger().id(), opponentId) || Objects.equals(view.opponent().id(), opponentId))
                .toList();
        if (!going.isEmpty()) return going.getFirst();

        Duel duel = duelRepository.save(Duel.builder()
                .challengerId(me)
                .opponentId(opponentId)
                .createdDate(LocalDateTime.now())
                .build());
        log.info("Account {} started duel {} with {}", me, duel.getDuelId(), opponentId);
        return views(List.of(duel), me).getFirst();
    }

    /** The current round's questions, for the player whose turn it is. */
    @Transactional(readOnly = true)
    public QuizDto quiz(final Long duelId) {
        Long me = currentAccountId();
        Duel duel = duelRepository.findById(duelId)
                .orElseThrow(() -> new RecordNotFoundException("No duel with id: " + duelId));
        DuelView view = views(List.of(duel), me).getFirst();
        if (Objects.isNull(view.me())) {
            throw new IllegalArgumentException(ServerText.t("err_duel_not_yours", "That duel is not yours"));
        }
        if (!"YOUR_TURN".equals(view.outcome())) {
            throw new IllegalArgumentException(ServerText.t("err_duel_not_your_turn", "It is not your turn in this duel"));
        }
        return quizService.replay(duelRounds.quizIdFor(duelId, view.state().currentRound()));
    }

    /**
     * The bell's side: a duel waiting on the reader (timed from the move that made it their turn),
     * and one that has been decided.
     */
    @Transactional(readOnly = true)
    public List<FeedItem> notificationsFor(final Long accountId, final LocalDateTime since) {
        List<FeedItem> items = new ArrayList<>();
        for (DuelView view : views(duelRepository.findInvolving(accountId), accountId)) {
            UserSummary other = view.me() == DuelRules.Side.CHALLENGER ? view.opponent() : view.challenger();
            if ("YOUR_TURN".equals(view.outcome())) {
                LocalDateTime at = view.state().deadline().minus(DuelRules.TURN);
                if (at.isAfter(since)) {
                    items.add(FeedItem.builder()
                            .key("DUEL_TURN-" + view.id() + "-" + view.state().currentRound())
                            .type(FeedItem.Type.DUEL_TURN)
                            .at(at)
                            .refId(view.id())
                            .count((long) view.state().currentRound())
                            .user(other)
                            .build());
                }
            } else if (view.state().over() && view.state().endedAt().isAfter(since)) {
                items.add(FeedItem.builder()
                        .key("DUEL_DONE-" + view.id())
                        .type(FeedItem.Type.DUEL_DONE)
                        .at(view.state().endedAt())
                        .refId(view.id())
                        .title(view.outcome())
                        .user(other)
                        .build());
            }
        }
        return items;
    }

    /** How many duel rounds this player played between two moments: a weekly quest counts them. */
    @Transactional(readOnly = true)
    public int roundsPlayedBetween(final Long accountId, final LocalDateTime from, final LocalDateTime to) {
        List<Duel> duels = duelRepository.findInvolving(accountId);
        if (duels.isEmpty()) return 0;
        return (int) roundRepository.findByDuelIdInOrderByRoundNo(duels.stream().map(Duel::getDuelId).toList()).stream()
                .map(round -> scoreOf(round.getQuizId(), accountId))
                .filter(Objects::nonNull)
                .filter(score -> !score.at().isBefore(from) && score.at().isBefore(to))
                .count();
    }

    private List<DuelView> views(final List<Duel> duels, final Long me) {
        if (duels.isEmpty()) return List.of();
        Map<Long, List<DuelRound>> rounds = roundRepository.findByDuelIdInOrderByRoundNo(
                        duels.stream().map(Duel::getDuelId).toList()).stream()
                .collect(Collectors.groupingBy(DuelRound::getDuelId));
        Set<Long> people = duels.stream().flatMap(duel -> Stream.of(duel.getChallengerId(), duel.getOpponentId()))
                .collect(Collectors.toSet());
        Map<Long, UserSummary> summaries = accountRepository.findAllById(people).stream()
                .collect(Collectors.toMap(Account::getAccountId, UserSummary::of));
        LocalDateTime now = LocalDateTime.now();

        // ponytail: two history reads per played round; fine for a player's handful of duels,
        // batch them by quiz id if the lists grow.
        return duels.stream()
                .filter(duel -> summaries.containsKey(duel.getChallengerId()) && summaries.containsKey(duel.getOpponentId()))
                .map(duel -> {
                    Map<Integer, DuelRules.Round> scored = new HashMap<>();
                    rounds.getOrDefault(duel.getDuelId(), List.of()).forEach(round -> scored.put(round.getRoundNo(),
                            new DuelRules.Round(scoreOf(round.getQuizId(), duel.getChallengerId()),
                                    scoreOf(round.getQuizId(), duel.getOpponentId()))));
                    DuelRules.State state = DuelRules.of(duel.getCreatedDate(), scored, now);
                    DuelRules.Side side = Objects.equals(me, duel.getChallengerId()) ? DuelRules.Side.CHALLENGER
                            : Objects.equals(me, duel.getOpponentId()) ? DuelRules.Side.OPPONENT : null;
                    List<RoundView> roundViews = IntStream.rangeClosed(1, DuelRules.ROUNDS)
                            .filter(scored::containsKey)
                            .mapToObj(no -> new RoundView(no, scored.get(no).challenger(), scored.get(no).opponent()))
                            .toList();
                    return new DuelView(duel.getDuelId(), summaries.get(duel.getChallengerId()),
                            summaries.get(duel.getOpponentId()), side, roundViews, state, outcome(state, side),
                            state.over() ? 0 : state.deadline().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                            duel.getCreatedDate());
                })
                .toList();
    }

    static String outcome(final DuelRules.State state, final DuelRules.Side me) {
        if (Objects.isNull(me)) return null;
        if (!state.over()) return state.turn() == me ? "YOUR_TURN" : "THEIR_TURN";
        if (Objects.isNull(state.winner())) return "DRAW";
        return state.winner() == me ? "WON" : "LOST";
    }

    private DuelRules.Score scoreOf(final Long quizId, final Long accountId) {
        return historyRepository.findFirstByQuiz_QuizIdAndAccount_AccountIdOrderByHistoryIdAsc(quizId, accountId)
                .filter(run -> Objects.nonNull(run.getTotalAnswers()))
                .map(run -> new DuelRules.Score(run.getRightAnswers(), run.getTotalAnswers(), run.getSpentTime(),
                        run.getCompletedDate()))
                .orElse(null);
    }

    private Long currentAccountId() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
    }
}
