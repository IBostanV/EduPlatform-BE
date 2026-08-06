package com.play.quiz.social;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.stream.LongStream;

import com.play.quiz.domain.Account;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.repository.UserQuizHistoryRepository.PlayerTotals;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import com.play.quiz.social.LeaderboardService.Board;
import com.play.quiz.social.LeaderboardService.Leaderboard;
import com.play.quiz.social.LeaderboardService.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LeaderboardServiceTest {

    @Mock private UserQuizHistoryRepository historyRepository;
    @Mock private AccountRepository accountRepository;
    @Mock private UserService userService;
    @Mock private AuthenticationFacade authenticationFacade;

    private LeaderboardService service;

    private static PlayerTotals totals(final long accountId, final long quizzes, final long right, final long answers) {
        return new PlayerTotals() {
            public Long getAccountId() { return accountId; }
            public Long getQuizzes() { return quizzes; }
            public Long getRightAnswers() { return right; }
            public Long getTotalAnswers() { return answers; }
        };
    }

    private static Account account(final long id) {
        return Account.builder().accountId(id).email(id + "@quiz").username("P" + id).build();
    }

    @BeforeEach
    void init() {
        service = new LeaderboardService(historyRepository, accountRepository, userService, authenticationFacade,
                Clock.fixed(Instant.parse("2026-09-27T12:00:00Z"), ZoneOffset.UTC));
        when(accountRepository.findAllById(any())).thenAnswer(invocation ->
                ((Collection<Long>) invocation.getArgument(0)).stream().map(LeaderboardServiceTest::account).toList());
        when(authenticationFacade.getPrincipal()).thenReturn(new User("12@quiz", "secret", List.of()));
        when(userService.findByEmail(anyString())).thenReturn(account(12));
    }

    @Test
    void given_players_then_accuracy_ranks_only_those_with_enough_answers() {
        when(historyRepository.findPlayerTotalsSince(any())).thenReturn(List.of(
                totals(1, 1, 2, 2),       // 100%, but two answers are not a record
                totals(2, 5, 40, 50),     // 80%
                totals(3, 3, 27, 30)));   // 90%

        Leaderboard board = service.read(Board.ACCURACY, Period.MONTH);

        assertEquals(2, board.players());
        assertEquals(3L, board.top().get(0).user().id());
        assertEquals(90, board.top().get(0).value());
        assertEquals(30, board.minAnswers());
    }

    @Test
    void given_the_reader_outside_the_top_then_their_place_comes_separately() {
        // Twelve players, player 1 with the most quizzes and the reader, 12, with the fewest.
        when(historyRepository.findPlayerTotalsSince(any())).thenReturn(LongStream.rangeClosed(1, 12)
                .mapToObj(id -> totals(id, 20 - id, 10, 20)).toList());

        Leaderboard board = service.read(Board.QUIZZES, Period.WEEK);

        assertEquals(10, board.top().size());
        assertEquals(1L, board.top().get(0).user().id());
        assertEquals(12, board.you().rank());
    }

    @Test
    void given_a_guest_then_there_is_no_place_of_theirs() {
        when(authenticationFacade.getPrincipal()).thenThrow(new ClassCastException("anonymousUser"));
        when(historyRepository.findPlayerTotalsSince(any())).thenReturn(List.of(totals(1, 3, 10, 20)));

        assertNull(service.read(Board.RIGHT_ANSWERS, Period.ALL).you());
    }
}
