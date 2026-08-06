package com.play.quiz.feed;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelUpRepository extends JpaRepository<LevelUp, Long> {

    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO Q_LEVEL_UP (LEVEL_UP_ID, ACCOUNT_ID, LEVEL_NO, REACHED_DATE)
            VALUES (level_up_seq.NEXTVAL, :accountId, :level, SYSDATE)
            """)
    void record(Long accountId, int level);

    /** Levels this player's friends reached after {@code since}. */
    @Query("SELECT l FROM LevelUp l WHERE l.reachedDate > :since AND l.accountId IN"
            + " (SELECT f.accountId FROM Account a JOIN a.friends f WHERE a.accountId = :accountId)")
    List<LevelUp> findFriendsSince(Long accountId, LocalDateTime since);
}
