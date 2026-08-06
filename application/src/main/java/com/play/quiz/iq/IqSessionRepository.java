package com.play.quiz.iq;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IqSessionRepository extends JpaRepository<IqSession, Long> {

    /** The test this player has on the go, if any. */
    Optional<IqSession> findFirstByAccount_AccountIdAndFinishedDateIsNullOrderBySessionIdDesc(Long accountId);

    /** What they scored last time, for the profile and for counting their attempts. */
    List<IqSession> findByAccount_AccountIdAndFinishedDateIsNotNullOrderByFinishedDateDesc(Long accountId);

    long countByFinishedDateIsNotNull();

    long countByAccount_AccountIdAndFinishedDateIsNotNull(Long accountId);

    /** The best score this player has ever had, or nought if they have never finished a test. */
    @Query("SELECT COALESCE(MAX(s.iq), 0) FROM IqSession s WHERE s.account.accountId = :accountId"
            + " AND s.finishedDate IS NOT NULL")
    int bestIq(Long accountId);

    /**
     * How many finished tests came out below this ability — the raw material of a percentile.
     *
     * <p>Only the first test of each player counts: later goes are inflated by knowing the
     * questions, and a norm built on them would drag everyone else's score down.
     */
    @Query("SELECT COUNT(s) FROM IqSession s WHERE s.finishedDate IS NOT NULL AND s.attemptNo = 0"
            + " AND s.theta < :theta")
    long countFirstAttemptsBelow(double theta);

    @Query("SELECT COUNT(s) FROM IqSession s WHERE s.finishedDate IS NOT NULL AND s.attemptNo = 0")
    long countFirstAttempts();
}
