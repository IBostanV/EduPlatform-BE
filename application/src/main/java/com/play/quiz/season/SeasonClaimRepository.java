package com.play.quiz.season;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonClaimRepository extends JpaRepository<SeasonClaim, SeasonClaim.Key> {

    @Query("SELECT c.code FROM SeasonClaim c WHERE c.accountId = :accountId AND c.periodStart = :periodStart")
    List<String> findClaimedCodes(Long accountId, LocalDate periodStart);

    /** Points from weekly quests whose week began in [from, to): a season's quest points. */
    @Query("SELECT COALESCE(SUM(c.points), 0) FROM SeasonClaim c WHERE c.accountId = :accountId"
            + " AND c.code LIKE 'QUEST:%' AND c.periodStart >= :from AND c.periodStart < :to")
    long sumQuestPoints(Long accountId, LocalDate from, LocalDate to);

    /**
     * Claims a quest or a tier, returning 1 to whoever got there first and 0 to everyone else: the
     * list pays as it is read, as the daily tasks do, so two tabs can land on it at once.
     */
    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO Q_SEASON_CLAIM (ACCOUNT_ID, CODE, PERIOD_START, POINTS, CLAIMED_DATE)
            SELECT :accountId, :code, :periodStart, :points, LOCALTIMESTAMP
            WHERE NOT EXISTS (SELECT 1 FROM Q_SEASON_CLAIM
                WHERE ACCOUNT_ID = :accountId AND CODE = :code AND PERIOD_START = :periodStart)
            """)
    int claim(Long accountId, String code, LocalDate periodStart, int points);
}
