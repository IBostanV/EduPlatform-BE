package com.play.quiz.trophy;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EarnedTrophyRepository extends JpaRepository<EarnedTrophy, Long> {

    List<EarnedTrophy> findByAccountId(Long accountId);

    /** How many trophies were won inside a window, for the statistics. */
    long countByAccountIdAndEarnedDateBetween(Long accountId, LocalDateTime from, LocalDateTime to);

    /**
     * Records a trophy the first time it is earned, and does nothing every time after.
     *
     * <p>Conditional rather than a save that might hit the unique key: the shelf works itself out
     * as it is read, so two tabs can find the same new trophy at the same moment.
     */
    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO Q_USER_TROPHY (USER_TROPHY_ID, ACCOUNT_ID, CODE, EARNED_DATE, CREATED_BY, CREATED_DATE)
            SELECT nextval('user_trophy_seq'), :accountId, :code, LOCALTIMESTAMP, :accountId, LOCALTIMESTAMP
            WHERE NOT EXISTS (SELECT 1 FROM Q_USER_TROPHY WHERE ACCOUNT_ID = :accountId AND CODE = :code)
            """)
    int award(Long accountId, String code);
}
