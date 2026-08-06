package com.play.quiz.social;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyChallengeRepository extends JpaRepository<DailyChallenge, LocalDate> {

    /**
     * Sets the day's quiz unless somebody already has, returning 1 to whoever got there first.
     *
     * <p>The day starts on the first read, from whichever request comes in first; two at once would
     * otherwise each set a quiz of their own. NOT EXISTS makes the second a no-op, and it then reads
     * the winner's quiz back.
     */
    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO Q_DAILY_CHALLENGE (CHALLENGE_DAY, QUIZ_ID)
            SELECT :day, :quizId FROM dual
            WHERE NOT EXISTS (SELECT 1 FROM Q_DAILY_CHALLENGE WHERE CHALLENGE_DAY = :day)
            """)
    int claimDay(LocalDate day, Long quizId);
}
