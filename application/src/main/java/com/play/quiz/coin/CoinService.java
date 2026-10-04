package com.play.quiz.coin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.play.quiz.dto.AnswerDto;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.QuestionService;
import com.play.quiz.service.UserService;
import com.play.quiz.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The shop: everything coins are spent on. Each purchase takes the coins first, in one conditional
 * statement, and throws if it cannot be delivered, so the transaction hands them back.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class CoinService {

    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final AccountRepository accountRepository;
    private final QuestionService questionService;

    @Transactional
    public Purchase buyStreakFreeze() {
        Long accountId = currentAccountId();
        pay(accountId, Coins.STREAK_FREEZE_PRICE);
        if (accountRepository.addStreakFreeze(accountId, Coins.STREAK_FREEZE_MAX) != 1) {
            throw new IllegalArgumentException("You already hold " + Coins.STREAK_FREEZE_MAX + " streak freezes");
        }
        int coins = accountRepository.findCoins(accountId);
        log.info("Account {} bought a streak freeze, {} coins left", accountId, coins);
        return new Purchase(coins, null);
    }

    /**
     * A 50/50 on a glossary question: of the options on screen, which to take away — every wrong
     * one but one. The right answer never leaves the server; only what to hide does.
     */
    @Transactional
    public Purchase buyHint(final Long questionId, final List<Long> shownTermIds) {
        Long rightTermId = questionService.checkMiniGameAnswer(questionId, new AnswerDto()).termId();
        if (Objects.isNull(rightTermId) || Objects.isNull(shownTermIds)) {
            throw new IllegalArgumentException("This question has no hint");
        }
        List<Long> wrong = new ArrayList<>(shownTermIds.stream().distinct()
                .filter(termId -> !rightTermId.equals(termId)).toList());
        if (wrong.size() < 2) {
            throw new IllegalArgumentException("Nothing left to take away");
        }

        Long accountId = currentAccountId();
        pay(accountId, Coins.HINT_PRICE);
        Collections.shuffle(wrong);
        int coins = accountRepository.findCoins(accountId);
        log.info("Account {} bought a hint on question {}: {} options removed, {} coins left",
                accountId, questionId, wrong.size() - 1, coins);
        return new Purchase(coins, wrong.subList(1, wrong.size()));
    }

    /** Paid for here, added by the browser: the quiz clock runs there. */
    @Transactional
    public Purchase buyExtraTime() {
        Long accountId = currentAccountId();
        pay(accountId, Coins.EXTRA_TIME_PRICE);
        int coins = accountRepository.findCoins(accountId);
        log.info("Account {} bought extra time, {} coins left", accountId, coins);
        return new Purchase(coins, null);
    }

    private void pay(final Long accountId, final int price) {
        if (accountRepository.spendCoins(accountId, price) != 1) {
            throw new IllegalArgumentException("Not enough coins: this costs " + price);
        }
        log.info("Account {} spent {} coins", accountId, price);
    }

    private Long currentAccountId() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
    }

    /** The balance after a purchase, and for a hint the options to take away (termIds). */
    public record Purchase(int coins, List<Long> remove) {}
}
