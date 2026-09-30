package com.play.quiz.conquest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConquestAttemptRepository extends JpaRepository<ConquestAttempt, Long> {

    /**
     * The best attempts on the given countries from rounds that have closed: most right, then
     * fastest, then whoever got there first. One row per attempt — the caller keeps the first of
     * each country, which is that country's standing conqueror.
     *
     * <p>Ordering by country first so the caller can walk it once. A run that is still in the
     * open round is left out by {@code beforeRound}: its result is not shown until the round shuts.
     */
    @Query("SELECT a FROM ConquestAttempt a JOIN FETCH a.account JOIN FETCH a.country"
            + " WHERE a.country.catId IN :countryIds AND a.roundNo < :beforeRound"
            + " ORDER BY a.country.catId, a.rightAnswers DESC, a.spentTime ASC, a.createdDate ASC")
    List<ConquestAttempt> findStandingAttempts(List<Long> countryIds, long beforeRound);

    /** This player's last go at this country, whatever round it was in, for the cooldown. */
    @Query("SELECT a FROM ConquestAttempt a WHERE a.country.catId = :countryId"
            + " AND a.account.accountId = :accountId ORDER BY a.createdDate DESC")
    List<ConquestAttempt> findLatestAttempts(Long countryId, Long accountId, Pageable pageable);

    Optional<ConquestAttempt> findByHistoryId(Long historyId);

    /** Countries conquered: an attempt is paid the bonus exactly once, for holding one. */
    long countByAccount_AccountIdAndBonusPaidTrue(Long accountId);

    /**
     * Claims the conquest bonus for an attempt, returning 1 to whoever got there first and 0 to
     * everyone else. The map settles itself as it is read, so two readers can land on the same
     * unpaid winner at once; this is what keeps them from both paying it.
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ConquestAttempt a SET a.bonusPaid = TRUE"
            + " WHERE a.attemptId = :attemptId AND a.bonusPaid = FALSE")
    int claimBonus(Long attemptId);

    /** Everything a player has taken part in, for the standings shown beside the map. */
    @Query("SELECT COUNT(a) FROM ConquestAttempt a WHERE a.account.accountId = :accountId"
            + " AND a.createdDate >= :since")
    long countAttemptsSince(Long accountId, LocalDateTime since);
}
