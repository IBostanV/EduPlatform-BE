package com.play.quiz.coin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.play.quiz.domain.Account;
import com.play.quiz.feed.LevelUpRepository;
import com.play.quiz.record.MiniGameResult;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.UserRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.QuestionService;
import com.play.quiz.service.UserService;
import com.play.quiz.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.User;

// The two rules with a branch in them: a streak freeze covers missed days only if there are
// enough of them, and a hint never takes away the right answer.
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CoinsTest {

    private static final long ACCOUNT_ID = 7L;
    private static final String EMAIL = "me@playquiz.io";

    @Mock private AccountRepository accountRepository;
    @Mock private AuthenticationFacade authenticationFacade;
    @Mock private UserService userService;
    @Mock private QuestionService questionService;
    @Mock private UserRepository userRepository;
    @Mock private LevelUpRepository levelUpRepository;
    @InjectMocks private UserServiceImpl userServiceImpl;

    private void lastSeen(final int daysAgo, final int streak, final int freezes) {
        Account account = Account.builder().accountId(ACCOUNT_ID).email(EMAIL)
                .lastSeenDate(LocalDate.now().minusDays(daysAgo)).loginStreak(streak).streakFreezes(freezes).build();
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(account));
        when(accountRepository.recordVisit(eq(ACCOUNT_ID), any(), anyInt())).thenReturn(1);
        when(accountRepository.findExperience(ACCOUNT_ID)).thenReturn(100);
    }

    @Test
    void given_one_missed_day_and_a_freeze_then_the_streak_goes_on() {
        lastSeen(2, 5, 1);
        userServiceImpl.recordVisit(EMAIL);
        verify(accountRepository).recordVisit(ACCOUNT_ID, LocalDate.now(), 6);
        verify(accountRepository).useStreakFreezes(ACCOUNT_ID, 1);
    }

    @Test
    void given_two_missed_days_and_one_freeze_then_the_streak_starts_over() {
        lastSeen(3, 5, 1);
        userServiceImpl.recordVisit(EMAIL);
        verify(accountRepository).recordVisit(ACCOUNT_ID, LocalDate.now(), 1);
        verify(accountRepository, never()).useStreakFreezes(anyLong(), anyInt());
    }

    @Test
    void given_yesterday_then_no_freeze_is_used() {
        lastSeen(1, 5, 2);
        userServiceImpl.recordVisit(EMAIL);
        verify(accountRepository).recordVisit(ACCOUNT_ID, LocalDate.now(), 6);
        verify(accountRepository, never()).useStreakFreezes(anyLong(), anyInt());
    }

    @Test
    void given_a_hint_then_all_wrong_options_but_one_go_and_the_right_one_stays() {
        CoinService coins = new CoinService(userService, authenticationFacade, accountRepository, questionService);
        when(authenticationFacade.getPrincipal()).thenReturn(new User(EMAIL, "x", List.of()));
        when(userService.findByEmail(EMAIL)).thenReturn(Account.builder().accountId(ACCOUNT_ID).build());
        when(questionService.checkMiniGameAnswer(eq(1L), any())).thenReturn(new MiniGameResult(false, null, 20L, "Paris"));
        when(accountRepository.spendCoins(ACCOUNT_ID, Coins.HINT_PRICE)).thenReturn(1);

        List<Long> remove = coins.buyHint(1L, List.of(10L, 20L, 30L, 40L)).remove();

        assertEquals(2, remove.size());
        assertFalse(remove.contains(20L));
    }

    @Test
    void given_too_few_coins_then_the_hint_is_refused() {
        CoinService coins = new CoinService(userService, authenticationFacade, accountRepository, questionService);
        when(authenticationFacade.getPrincipal()).thenReturn(new User(EMAIL, "x", List.of()));
        when(userService.findByEmail(EMAIL)).thenReturn(Account.builder().accountId(ACCOUNT_ID).build());
        when(questionService.checkMiniGameAnswer(eq(1L), any())).thenReturn(new MiniGameResult(false, null, 20L, "Paris"));
        when(accountRepository.spendCoins(ACCOUNT_ID, Coins.HINT_PRICE)).thenReturn(0);

        assertThrows(IllegalArgumentException.class, () -> coins.buyHint(1L, List.of(10L, 20L, 30L, 40L)));
    }
}
