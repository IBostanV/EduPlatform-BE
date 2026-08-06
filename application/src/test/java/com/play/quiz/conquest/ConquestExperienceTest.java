package com.play.quiz.conquest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Category;
import com.play.quiz.domain.UserQuizHistory;
import com.play.quiz.fixtures.AccountFixture;
import com.play.quiz.fixtures.UserDetailsFixture;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.CategoryRepository;
import com.play.quiz.repository.MessageGroupRepository;
import com.play.quiz.repository.UserGroupRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import com.play.quiz.util.ExperiencePayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.User;

/**
 * What the conquest game pays: a multiple of the ordinary run for going at a country, and a bonus
 * to whoever is holding it once the round shuts.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ConquestExperienceTest {

    private static final long COUNTRY_ID = 40L;
    private static final long HISTORY_ID = 7L;
    private static final long ATTEMPT_ID = 3L;

    @Mock private AuthenticationFacade authenticationFacade;
    @Mock private CategoryRepository categoryRepository;
    @Mock private ConquestAttemptRepository attemptRepository;
    @Mock private UserQuizHistoryRepository historyRepository;
    @Mock private UserService userService;
    @Mock private AccountRepository accountRepository;
    @Mock private MessageGroupRepository messageGroupRepository;
    @Mock private UserGroupRepository userGroupRepository;

    private ConquestService conquestService;
    private Account player;
    private Category country;

    // Pinned to the first day of round 0, so these read the same whatever day they are run.
    private static final Clock OPEN_ROUND = Clock.fixed(
            ConquestSchedule.roundOpensAt(0).plusHours(2).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

    @BeforeEach
    void init() {
        conquestService = new ConquestServiceImpl(authenticationFacade, categoryRepository,
                attemptRepository, historyRepository, userService, accountRepository,
                messageGroupRepository, userGroupRepository, OPEN_ROUND);

        player = AccountFixture.getAdminAccount();
        country = Category.builder()
                .catId(COUNTRY_ID)
                .name("Moldova")
                .naturalId("MDA")
                .visible(true)
                .parent(Category.builder().naturalId(ConquestServiceImpl.COUNTRIES_PARENT).build())
                .build();

        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        when(userService.findByEmail(player.getEmail())).thenReturn(player);
        when(categoryRepository.findCountries(ConquestServiceImpl.COUNTRIES_PARENT)).thenReturn(List.of(country));
        when(categoryRepository.findById(COUNTRY_ID)).thenReturn(Optional.of(country));
        when(attemptRepository.findByHistoryId(HISTORY_ID)).thenReturn(Optional.empty());
        when(attemptRepository.findLatestAttempts(any(), any(), any())).thenReturn(List.of());
        when(attemptRepository.findStandingAttempts(any(), anyLong())).thenReturn(List.of());
    }

    @Test
    void given_a_conquest_attempt_then_top_the_run_up_to_the_conquest_rate() {
        // 5 right and 1 wrong is 52 at the ordinary rate, which the run has already been paid.
        when(historyRepository.findById(HISTORY_ID)).thenReturn(Optional.of(run(5, 6)));

        conquestService.recordAttempt(COUNTRY_ID, HISTORY_ID);

        verify(userService).addExperience(player.getAccountId(), ExperiencePayout.conquestTopUp(5, 6));
        // What the run and the top-up come to together is the multiple, and nothing else.
        org.junit.jupiter.api.Assertions.assertEquals(
                ExperiencePayout.forRun(5, 6) * ExperiencePayout.CONQUEST_MULTIPLIER,
                ExperiencePayout.forRun(5, 6) + ExperiencePayout.conquestTopUp(5, 6));
    }

    @Test
    void given_a_holder_who_has_not_been_paid_then_pay_the_conquest_bonus_once() {
        ConquestAttempt holder = holding(false);
        when(attemptRepository.findStandingAttempts(any(), anyLong())).thenReturn(List.of(holder));
        when(attemptRepository.claimBonus(ATTEMPT_ID)).thenReturn(1);

        conquestService.getState();

        verify(userService).addExperience(player.getAccountId(), ExperiencePayout.CONQUEST_BONUS);
    }

    // Holding a country is not conquering it again: the bonus is for taking one.
    @Test
    void given_a_holder_already_paid_then_pay_nothing_however_often_the_map_is_read() {
        when(attemptRepository.findStandingAttempts(any(), anyLong())).thenReturn(List.of(holding(true)));

        conquestService.getState();
        conquestService.getState();

        verify(userService, never()).addExperience(anyLong(), anyInt());
        verify(attemptRepository, never()).claimBonus(anyLong());
    }

    // Two readers can land on the same unpaid winner; only the one that claims it pays.
    @Test
    void given_another_reader_got_there_first_then_do_not_pay_it_twice() {
        when(attemptRepository.findStandingAttempts(any(), anyLong())).thenReturn(List.of(holding(false)));
        when(attemptRepository.claimBonus(ATTEMPT_ID)).thenReturn(0);

        conquestService.getState();

        verify(userService, never()).addExperience(anyLong(), eq(ExperiencePayout.CONQUEST_BONUS));
    }

    private ConquestAttempt holding(final boolean paid) {
        return ConquestAttempt.builder()
                .attemptId(ATTEMPT_ID)
                .roundNo(0)
                .country(country)
                .account(player)
                .historyId(HISTORY_ID)
                .rightAnswers(6)
                .totalAnswers(6)
                .spentTime(30.0)
                .bonusPaid(paid)
                .createdDate(LocalDateTime.now())
                .build();
    }

    private UserQuizHistory run(final int right, final int total) {
        return UserQuizHistory.builder()
                .historyId(HISTORY_ID)
                .account(player)
                .rightAnswers(right)
                .totalAnswers(total)
                .spentTime(42.0)
                .build();
    }
}
