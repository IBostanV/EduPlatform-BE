package com.play.quiz.conquest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Category;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.CategoryRepository;
import com.play.quiz.repository.MessageGroupRepository;
import com.play.quiz.repository.UserGroupRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/** Who took which country in which round, as the notifications and the news read it. */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ConquestChangesTest {

    // Two days into round 1, so rounds 0 and 1 have both closed.
    private static final Clock ROUND_ONE_LOCKED = Clock.fixed(
            ConquestSchedule.roundOpensAt(1).plusDays(2).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    private static final LocalDateTime SINCE = ConquestSchedule.roundOpensAt(0);

    @Mock private AuthenticationFacade authenticationFacade;
    @Mock private CategoryRepository categoryRepository;
    @Mock private ConquestAttemptRepository attemptRepository;
    @Mock private UserQuizHistoryRepository historyRepository;
    @Mock private UserService userService;
    @Mock private AccountRepository accountRepository;
    @Mock private MessageGroupRepository messageGroupRepository;
    @Mock private UserGroupRepository userGroupRepository;

    private ConquestService conquestService;
    private final Account alice = Account.builder().accountId(1L).username("alice").build();
    private final Account bob = Account.builder().accountId(2L).username("bob").build();
    // One country, so every round opens it.
    private final Category moldova = Category.builder().catId(40L).name("Moldova").naturalId("MDA").build();

    @BeforeEach
    void init() {
        conquestService = new ConquestServiceImpl(authenticationFacade, categoryRepository,
                attemptRepository, historyRepository, userService, accountRepository,
                messageGroupRepository, userGroupRepository, ROUND_ONE_LOCKED);
        when(categoryRepository.findCountries(ConquestServiceImpl.COUNTRIES_PARENT)).thenReturn(List.of(moldova));
        when(attemptRepository.findStandingAttempts(any(), eq(0L))).thenReturn(List.of());
    }

    @Test
    void given_a_country_taken_then_retaken_by_somebody_else_then_list_both_changes() {
        ConquestAttempt aliceTakes = attempt(alice, 0, 4);
        when(attemptRepository.findStandingAttempts(any(), eq(1L))).thenReturn(List.of(aliceTakes));
        when(attemptRepository.findStandingAttempts(any(), eq(2L))).thenReturn(List.of(attempt(bob, 1, 6), aliceTakes));

        List<ConquestService.ConquestChange> changes = conquestService.changesSince(SINCE);

        assertEquals(2, changes.size());
        assertEquals(alice, changes.get(0).holder());
        assertNull(changes.get(0).previous());
        assertEquals(bob, changes.get(1).holder());
        assertEquals(alice, changes.get(1).previous());
        assertEquals(ConquestSchedule.roundClosesAt(1), changes.get(1).at());
    }

    // Beating your own record is not losing the country, nor taking it again.
    @Test
    void given_a_holder_who_beats_their_own_record_then_nothing_changed_hands() {
        ConquestAttempt aliceTakes = attempt(alice, 0, 4);
        when(attemptRepository.findStandingAttempts(any(), eq(1L))).thenReturn(List.of(aliceTakes));
        when(attemptRepository.findStandingAttempts(any(), eq(2L))).thenReturn(List.of(attempt(alice, 1, 6), aliceTakes));

        List<ConquestService.ConquestChange> changes = conquestService.changesSince(SINCE);

        assertEquals(1, changes.size());
        assertEquals(0, changes.get(0).round());
    }

    @Test
    void given_rounds_opened_in_the_window_then_list_each_with_its_countries() {
        List<ConquestService.ConquestRound> rounds = conquestService.roundsOpenedSince(SINCE.minusMinutes(1));

        assertEquals(List.of(0L, 1L), rounds.stream().map(ConquestService.ConquestRound::round).toList());
        assertEquals(List.of(moldova), rounds.get(1).countries());
    }

    // The second day of a round is shut, but the round is not over: its runs do not count yet.
    @Test
    void given_a_closed_day_mid_round_then_the_rounds_own_attempts_do_not_hold_countries_yet() {
        long round = ConquestSchedule.FIRST_ALTERNATING_ROUND;
        Clock closedDay = Clock.fixed(ConquestSchedule.roundOpensAt(round).plusDays(1).plusHours(3)
                .toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        ConquestService midRound = new ConquestServiceImpl(authenticationFacade, categoryRepository,
                attemptRepository, historyRepository, userService, accountRepository,
                messageGroupRepository, userGroupRepository, closedDay);
        when(attemptRepository.findStandingAttempts(any(), eq(round))).thenReturn(List.of());

        ConquestState state = midRound.getState();

        assertEquals(false, state.open());
        assertEquals(ConquestSchedule.roundOpensAt(round).plusDays(2), state.nextOpenAt());
        verify(attemptRepository, never()).findStandingAttempts(any(), eq(round + 1));
    }

    private ConquestAttempt attempt(final Account account, final long round, final int right) {
        return ConquestAttempt.builder()
                .roundNo(round)
                .country(moldova)
                .account(account)
                .rightAnswers(right)
                .totalAnswers(6)
                .spentTime(30.0)
                .createdDate(ConquestSchedule.roundOpensAt(round).plusHours(1))
                .build();
    }
}
