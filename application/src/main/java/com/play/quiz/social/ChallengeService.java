package com.play.quiz.social;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.UserQuizHistory;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.feed.FeedItem;
import com.play.quiz.record.UserSummary;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.QuizService;
import com.play.quiz.service.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * "Beat my score". A challenge is a finished quiz sent to friends; they play the same questions
 * (the same quiz id), and the two runs are set side by side. Nothing is settled or stored about the
 * outcome: whoever has played, their first run of that quiz is their score.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class ChallengeService {

    /** How far back the lists and the notifications reach. */
    static final int WINDOW_DAYS = 30;

    private final ChallengeRepository challengeRepository;
    private final UserQuizHistoryRepository historyRepository;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final QuizService quizService;
    private final AuthenticationFacade authenticationFacade;

    public record Score(int rightAnswers, int totalAnswers, Double spentTime, LocalDateTime at) {
        static Score of(final UserQuizHistory run) {
            return new Score(run.getRightAnswers(), run.getTotalAnswers(), run.getSpentTime(), run.getCompletedDate());
        }
    }

    /** One challenge as either side sees it; opponentScore is null until the opponent has played. */
    public record ChallengeView(Long id, Long quizId, String category, UserSummary challenger,
                                UserSummary opponent, Score challengerScore, Score opponentScore,
                                LocalDateTime createdDate) {

        /** WON, LOST or DRAW for the challenger once both have played (right first, then faster); null before. */
        @JsonProperty
        public String outcome() {
            if (Objects.isNull(opponentScore)) return null;
            int byRight = Integer.compare(challengerScore.rightAnswers(), opponentScore.rightAnswers());
            int bySpeed = Double.compare(Optional.ofNullable(opponentScore.spentTime()).orElse(0.0),
                    Optional.ofNullable(challengerScore.spentTime()).orElse(0.0));
            int result = byRight != 0 ? byRight : bySpeed;
            return result > 0 ? "WON" : result < 0 ? "LOST" : "DRAW";
        }
    }

    public record Challenges(List<ChallengeView> received, List<ChallengeView> sent) {}

    /**
     * Sends one of the signed-in player's finished runs to some of their friends. The run has to be
     * theirs, marked, and not a custom quiz (those have invitations of their own). A friend already
     * challenged on this quiz is skipped rather than refused.
     */
    @Transactional
    public List<ChallengeView> send(final Long historyId, final Collection<Long> friendIds) {
        Account me = currentAccount();
        UserQuizHistory run = historyRepository.findById(historyId)
                .orElseThrow(() -> new RecordNotFoundException("No quiz run with id: " + historyId));
        if (!Objects.equals(run.getAccount().getAccountId(), me.getAccountId())) {
            throw new IllegalArgumentException("That quiz run is not yours");
        }
        if (Objects.isNull(run.getTotalAnswers())) {
            throw new IllegalArgumentException("That quiz run has no score to beat");
        }
        if (run.getQuiz().isCustom()) {
            throw new IllegalArgumentException("A custom quiz is shared by inviting friends to it");
        }

        Set<Long> friends = accountRepository.findFriends(me.getAccountId()).stream()
                .map(Account::getAccountId)
                .collect(Collectors.toSet());
        Long quizId = run.getQuiz().getQuizId();
        List<Challenge> sent = new ArrayList<>();
        for (Long friendId : new HashSet<>(friendIds)) {
            if (!friends.contains(friendId)) {
                throw new IllegalArgumentException("You can only challenge your friends");
            }
            if (challengeRepository.existsByQuizIdAndOpponentId(quizId, friendId)) {
                continue;
            }
            sent.add(challengeRepository.save(Challenge.builder()
                    .quizId(quizId)
                    .challengerId(me.getAccountId())
                    .challengerHistoryId(historyId)
                    .opponentId(friendId)
                    .createdDate(LocalDateTime.now())
                    .build()));
        }
        log.info("Account {} challenged {} to quiz {}", me.getAccountId(), friendIds, quizId);
        return views(sent);
    }

    /** What the signed-in player was challenged to, and what they sent, in the last month. */
    @Transactional(readOnly = true)
    public Challenges mine() {
        Long me = currentAccount().getAccountId();
        List<ChallengeView> all = views(challengeRepository.findInvolving(me, LocalDateTime.now().minusDays(WINDOW_DAYS)));
        return new Challenges(
                all.stream().filter(view -> me.equals(view.opponent().id())).toList(),
                all.stream().filter(view -> me.equals(view.challenger().id())).toList());
    }

    /** The challenge's quiz, to play: only for the two players in it. */
    @Transactional(readOnly = true)
    public QuizDto quiz(final Long challengeId) {
        Long me = currentAccount().getAccountId();
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RecordNotFoundException("No challenge with id: " + challengeId));
        if (!me.equals(challenge.getOpponentId()) && !me.equals(challenge.getChallengerId())) {
            throw new IllegalArgumentException("That challenge is not yours");
        }
        return quizService.replay(challenge.getQuizId());
    }

    /**
     * The bell's side of it: a challenge received, and, for the one who sent it, the moment the
     * opponent has played. Derived on read like the rest of the notifications.
     */
    @Transactional(readOnly = true)
    public List<FeedItem> notificationsFor(final Long accountId, final LocalDateTime since) {
        List<FeedItem> items = new ArrayList<>();
        for (ChallengeView view : views(challengeRepository.findInvolving(accountId, since))) {
            if (accountId.equals(view.opponent().id())) {
                items.add(FeedItem.builder()
                        .key("CHALLENGE-" + view.id())
                        .type(FeedItem.Type.CHALLENGE)
                        .at(view.createdDate())
                        .refId(view.id())
                        .name(view.category())
                        .user(view.challenger())
                        .build());
            } else if (Objects.nonNull(view.opponentScore())) {
                items.add(FeedItem.builder()
                        .key("CHALLENGE_DONE-" + view.id())
                        .type(FeedItem.Type.CHALLENGE_DONE)
                        .at(view.opponentScore().at())
                        .refId(view.id())
                        .name(view.category())
                        .user(view.opponent())
                        .title(view.outcome())
                        .build());
            }
        }
        return items;
    }

    private List<ChallengeView> views(final List<Challenge> challenges) {
        if (challenges.isEmpty()) return List.of();
        Set<Long> accountIds = challenges.stream()
                .flatMap(challenge -> Stream.of(challenge.getChallengerId(), challenge.getOpponentId()))
                .collect(Collectors.toSet());
        Map<Long, UserSummary> people = accountRepository.findAllById(accountIds).stream()
                .collect(Collectors.toMap(Account::getAccountId, UserSummary::of, (first, second) -> first));

        // ponytail: two history reads per challenge; fine for a month of one player's challenges,
        // batch them by quiz id if the lists ever get long.
        return challenges.stream().map(challenge -> {
            Optional<UserQuizHistory> theirs = historyRepository.findById(challenge.getChallengerHistoryId())
                    .filter(run -> Objects.nonNull(run.getTotalAnswers()));
            Optional<UserQuizHistory> answer = historyRepository
                    .findFirstByQuiz_QuizIdAndAccount_AccountIdOrderByHistoryIdAsc(challenge.getQuizId(), challenge.getOpponentId())
                    .filter(run -> Objects.nonNull(run.getTotalAnswers()));
            String category = theirs.map(UserQuizHistory::getQuiz)
                    .map(quiz -> Objects.nonNull(quiz.getCategory()) ? quiz.getCategory().getName() : null)
                    .orElse(null);
            return new ChallengeView(challenge.getChallengeId(), challenge.getQuizId(), category,
                    people.get(challenge.getChallengerId()), people.get(challenge.getOpponentId()),
                    theirs.map(Score::of).orElse(null),
                    answer.map(Score::of).orElse(null),
                    challenge.getCreatedDate());
        }).filter(view -> Objects.nonNull(view.challengerScore())).toList();
    }

    private Account currentAccount() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername());
    }
}
