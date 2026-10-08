package com.play.quiz.trophy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.play.quiz.conquest.ConquestAttemptRepository;
import com.play.quiz.daily.DailyTask;
import com.play.quiz.daily.DailyTaskClaimRepository;
import com.play.quiz.domain.Account;
import com.play.quiz.iq.IqSessionRepository;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import com.play.quiz.util.ServerText;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The trophy shelf: what a player has earned, what they have not, and what the locked ones take.
 *
 * <p>Trophies are worked out from what the player has done rather than handed out as it happens.
 * Nothing has to fire at the right moment, nothing is missed while a server is down, and a
 * trophy cannot be awarded twice — the shelf settles itself when it is read, the way the conquest
 * map and the daily tasks do. What is written down is only that a trophy was earned and when,
 * because that is the one thing the counts cannot answer later.
 *
 * <p>A secret trophy that has not been earned is sent with no title and no description: they are
 * meant to be come across, and a list of things to do is not a secret.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class TrophyService {

    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final TrophyCatalog catalog;
    private final EarnedTrophyRepository earnedRepository;
    private final AccountRepository accountRepository;
    private final UserQuizHistoryRepository historyRepository;
    private final ConquestAttemptRepository conquestRepository;
    private final DailyTaskClaimRepository dailyTaskRepository;
    private final IqSessionRepository iqRepository;

    /** Every trophy, as this player stands — and anything newly earned is recorded on the way. */
    @Transactional
    public List<TrophyProgress> shelf() {
        Account player = currentAccount();
        PlayerStanding standing = standingOf(player);
        Map<String, LocalDateTime> earned = earnedRepository.findByAccountId(player.getAccountId()).stream()
                .collect(Collectors.toMap(EarnedTrophy::getCode, EarnedTrophy::getEarnedDate,
                        (first, second) -> first));

        return catalog.all().stream()
                .map(definition -> toProgress(definition, standing, earned, player))
                .toList();
    }

    /**
     * Chooses the trophy shown beside this player's name, or clears it with a blank code.
     *
     * @throws IllegalArgumentException if they have not earned it — the shelf is not a dressing-up box
     */
    @Transactional
    public List<TrophyProgress> prefer(final String code) {
        Account player = currentAccount();
        // Reading the shelf also records anything just earned, so a trophy can be chosen the
        // moment it is won rather than after the next look at the list.
        List<TrophyProgress> shelf = shelf();

        boolean clearing = Objects.isNull(code) || code.isBlank();
        if (!clearing && shelf.stream().noneMatch(trophy -> trophy.earned() && trophy.code().equals(code))) {
            throw new IllegalArgumentException(ServerText.t("err_trophy_not_earned", "That trophy has not been earned"));
        }

        accountRepository.setPreferredTrophy(player.getAccountId(), clearing ? null : code);
        log.info("Account {} preferred trophy {} -> {}", player.getAccountId(), player.getPreferredTrophy(),
                clearing ? null : code);

        return shelf();
    }

    private TrophyProgress toProgress(final TrophyDefinition definition, final PlayerStanding standing,
                                      final Map<String, LocalDateTime> earned, final Account player) {
        LocalDateTime earnedDate = earned.get(definition.code());
        boolean has = Objects.nonNull(earnedDate) || definition.isEarnedBy(standing);
        if (has && Objects.isNull(earnedDate)) {
            earnedDate = award(player, definition);
        }

        // Secret and not earned: the code is all that goes out, so the browser can draw a blank
        // and count how many there are left to find.
        boolean hidden = definition.secret() && !has;

        return new TrophyProgress(definition.code(), definition.group(),
                hidden ? null : definition.title(),
                hidden ? null : definition.description(),
                hidden ? null : definition.icon(),
                definition.secret(), definition.categoryId(),
                hidden ? 0 : definition.progressOf(standing), hidden ? 0 : definition.target(),
                has, earnedDate, definition.code().equals(player.getPreferredTrophy()));
    }

    /** Writes a newly earned trophy down. The insert is conditional, so two readers cannot race. */
    private LocalDateTime award(final Account player, final TrophyDefinition definition) {
        if (earnedRepository.award(player.getAccountId(), definition.code()) == 1) {
            log.info("Account {} earned trophy {}", player.getAccountId(), definition.code());
        }
        return LocalDateTime.now();
    }

    private PlayerStanding standingOf(final Account player) {
        Long accountId = player.getAccountId();
        Map<Long, Long> byCategory = historyRepository.countByCategory(accountId).stream()
                .collect(Collectors.toMap(UserQuizHistoryRepository.CategoryRuns::getCategoryId,
                        UserQuizHistoryRepository.CategoryRuns::getRuns));

        return new PlayerStanding(
                historyRepository.countByAccount_AccountId(accountId),
                byCategory,
                historyRepository.countFlawless(accountId),
                historyRepository.countSwiftRuns(accountId),
                historyRepository.countNightRuns(accountId),
                conquestRepository.countByAccount_AccountIdAndBonusPaidTrue(accountId),
                // A clean sweep is a day with a payment for every task there is.
                dailyTaskRepository.findCompleteDays(accountId, DailyTask.values().length).size(),
                Streaks.longestRun(historyRepository.findCompletedDates(accountId).stream()
                        .map(LocalDateTime::toLocalDate)
                        .toList()),
                iqRepository.countByAccount_AccountIdAndFinishedDateIsNotNull(accountId),
                iqRepository.bestIq(accountId),
                player.getLoginStreak(),
                player.getBestStreak());
    }

    private Account currentAccount() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername());
    }

}
