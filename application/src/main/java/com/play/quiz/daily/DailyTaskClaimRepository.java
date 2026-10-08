package com.play.quiz.daily;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyTaskClaimRepository extends JpaRepository<DailyTaskClaim, Long> {

    /** Which of today's tasks this player has already been paid for. */
    @Query("SELECT c.taskCode FROM DailyTaskClaim c WHERE c.accountId = :accountId AND c.taskDay = :day")
    List<String> findClaimedCodes(Long accountId, LocalDate day);

    /** How many daily tasks this player finished between two days, both included: season points. */
    long countByAccountIdAndTaskDayBetween(Long accountId, LocalDate from, LocalDate to);

    /**
     * The days on which this player was paid for every one of that day's tasks — a clean sweep,
     * which is a trophy. One row per such day, counted by the caller.
     */
    @Query("SELECT c.taskDay FROM DailyTaskClaim c WHERE c.accountId = :accountId"
            + " GROUP BY c.taskDay HAVING COUNT(c) >= :tasks")
    List<LocalDate> findCompleteDays(Long accountId, long tasks);

    /**
     * Claims a finished task, returning 1 to whoever got there first and 0 to everyone else.
     *
     * <p>Native and conditional for the same reason the conquest bonus is: the list settles
     * itself as it is read, so two tabs can land on the same finished task at once. NOT EXISTS
     * turns the second one into a no-op rather than a unique-key violation that would take the
     * whole read down with it.
     */
    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO Q_DAILY_TASK (TASK_ID, ACCOUNT_ID, TASK_CODE, TASK_DAY, EXPERIENCE, CREATED_BY, CREATED_DATE)
            SELECT daily_task_seq.NEXTVAL, :accountId, :code, :day, :experience, :accountId, SYSDATE FROM dual
            WHERE NOT EXISTS (SELECT 1 FROM Q_DAILY_TASK
                WHERE ACCOUNT_ID = :accountId AND TASK_CODE = :code AND TASK_DAY = :day)
            """)
    int claim(Long accountId, String code, LocalDate day, int experience);
}
