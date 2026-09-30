package com.play.quiz.daily;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.UserQuizHistory;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Today's goals for the signed-in player, paid as they are read.
 *
 * <p>Reading is also when a finished task is paid, the way the conquest map settles itself when
 * it is read: nothing is counted up as a player plays, so this is the only moment there is, and
 * it needs no job that could miss a day or fire twice.
 *
 * <p>The day is the server's own day (the history rows it compares against are written with the
 * same clock), and it starts over at midnight with nothing to reset.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class DailyTaskService {

    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final UserQuizHistoryRepository historyRepository;
    private final DailyTaskClaimRepository claimRepository;

    // Not read-only: what it reads is what pays.
    @Transactional
    public List<DailyTaskProgress> getToday() {
        Account player = userService.findByEmail(authenticationFacade.getPrincipal().getUsername());
        LocalDate today = LocalDate.now();
        List<UserQuizHistory> runs = historyRepository.findRunsBetween(
                player.getAccountId(), today.atStartOfDay(), today.plusDays(1).atStartOfDay());
        Set<String> paid = Set.copyOf(claimRepository.findClaimedCodes(player.getAccountId(), today));

        return Arrays.stream(DailyTask.values())
                .map(task -> progressOf(task, player.getAccountId(), today, runs, paid))
                .toList();
    }

    private DailyTaskProgress progressOf(final DailyTask task, final Long accountId, final LocalDate today,
                                         final List<UserQuizHistory> runs, final Set<String> paid) {
        int progress = task.progressFrom(runs);
        boolean completed = progress >= task.getTarget();
        boolean awarded = completed && !paid.contains(task.name()) && pay(task, accountId, today);

        // Capped, so a bar that is over full reads as finished rather than as 31/25.
        return new DailyTaskProgress(task.name(), Math.min(progress, task.getTarget()),
                task.getTarget(), task.getExperience(), completed, awarded);
    }

    /** Pays a task once. The claim is the conditional insert: whoever writes the row pays. */
    private boolean pay(final DailyTask task, final Long accountId, final LocalDate today) {
        if (claimRepository.claim(accountId, task.name(), today, task.getExperience()) != 1) {
            // Another request wrote the claim first and paid it.
            log.debug("Account {} daily task {} already claimed for {}", accountId, task, today);
            return false;
        }
        userService.addExperience(accountId, task.getExperience());
        log.info("Account {} finished daily task {}: {} experience", accountId, task, task.getExperience());

        return true;
    }
}
