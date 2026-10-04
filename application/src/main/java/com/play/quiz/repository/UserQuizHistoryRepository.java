package com.play.quiz.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.play.quiz.domain.UserQuizHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserQuizHistoryRepository extends JpaRepository<UserQuizHistory, Long> {

    boolean existsByQuiz_QuizIdAndAccount_Email(Long quizId, String email);

    // Has this player finished this quiz before? The run being paid for is already written by the
    // time experience is worked out, so it is the one left out by id.
    boolean existsByQuiz_QuizIdAndAccount_AccountIdAndHistoryIdNot(Long quizId, Long accountId, Long historyId);

    // Was there a run of this quiz by this player before this one? Only the first one paid.
    boolean existsByQuiz_QuizIdAndAccount_AccountIdAndHistoryIdLessThan(Long quizId, Long accountId, Long historyId);

    // One page of the signed-in player's own runs, newest first, for the history on their
    // profile. The quiz, its category and its type come along: the list names every row by them.
    // Its own countQuery: the derived one would carry the fetch joins and refuse to count.
    @Query(value = "SELECT h FROM UserQuizHistory h JOIN FETCH h.quiz q LEFT JOIN FETCH q.category"
            + " LEFT JOIN FETCH q.type WHERE h.account.email = :email",
            countQuery = "SELECT COUNT(h) FROM UserQuizHistory h WHERE h.account.email = :email")
    Page<UserQuizHistory> findOwnHistory(String email, Pageable pageable);

    // The same, for another player's profile.
    @Query(value = "SELECT h FROM UserQuizHistory h JOIN FETCH h.quiz q LEFT JOIN FETCH q.category"
            + " LEFT JOIN FETCH q.type WHERE h.account.accountId = :accountId",
            countQuery = "SELECT COUNT(h) FROM UserQuizHistory h WHERE h.account.accountId = :accountId")
    Page<UserQuizHistory> findHistoryOf(Long accountId, Pageable pageable);

    /**
     * What every one of a player's runs adds up to — the whole history, not the page on screen.
     *
     * <p>SUM skips the runs recorded before the score was kept, so they weigh nothing, while
     * COUNT still counts them as quizzes played, which they were.
     */
    @Query("SELECT COUNT(h) AS played"
            + ", COALESCE(SUM(h.rightAnswers), 0) AS rightAnswers"
            + ", COALESCE(SUM(h.totalAnswers), 0) AS totalAnswers"
            + ", COALESCE(SUM(h.spentTime), 0) AS seconds"
            + ", COALESCE(MAX(CASE WHEN h.totalAnswers > 0"
            + "     THEN h.rightAnswers * 100.0 / h.totalAnswers ELSE 0 END), 0) AS best"
            + " FROM UserQuizHistory h WHERE h.account.email = :email")
    HistoryTotals sumOwnHistory(String email);

    /** The row {@link #sumOwnHistory} returns; {@code QuizStatistics.of} works the rest out. */
    interface HistoryTotals {
        long getPlayed();
        long getRightAnswers();
        long getTotalAnswers();
        double getSeconds();
        double getBest();
    }

    /**
     * A player's runs finished within a window, for the daily tasks. The quiz comes along: a
     * custom one counts for nothing there, the same as it pays no experience.
     */
    @Query("SELECT h FROM UserQuizHistory h JOIN FETCH h.quiz q LEFT JOIN FETCH q.category"
            + " WHERE h.account.accountId = :accountId"
            + " AND h.completedDate >= :from AND h.completedDate < :to")
    List<UserQuizHistory> findRunsBetween(Long accountId, LocalDateTime from, LocalDateTime to);

    long countByQuiz_QuizId(Long quizId);

    /** A player's first run of a quiz: the one a challenge or the daily challenge counts. */
    Optional<UserQuizHistory> findFirstByQuiz_QuizIdAndAccount_AccountIdOrderByHistoryIdAsc(Long quizId, Long accountId);

    /**
     * Everyone's first run of one quiz, best first: most right, then fastest, then earliest. The
     * daily challenge's table — a second go at the same quiz does not count.
     */
    @Query(nativeQuery = true, value = """
            SELECT ACCOUNT_ID AS accountId, RIGHT_ANSWERS AS rightAnswers, TOTAL_ANSWERS AS totalAnswers,
                   SPENT_TIME AS spentTime
            FROM (SELECT h.*, ROW_NUMBER() OVER (PARTITION BY h.ACCOUNT_ID ORDER BY h.HISTORY_ID) AS nth
                  FROM Q_USER_HISTORY h WHERE h.QUIZ_ID = :quizId AND h.TOTAL_ANSWERS IS NOT NULL) runs
            WHERE nth = 1
            ORDER BY RIGHT_ANSWERS DESC, COALESCE(SPENT_TIME, 0) ASC, HISTORY_ID ASC
            """)
    List<FirstRun> findFirstRuns(Long quizId);

    /**
     * Every active player's totals since a moment: marked runs of quizzes that pay (custom ones do
     * not count, as for experience), blocked accounts left out. The leaderboard's raw material.
     */
    @Query(nativeQuery = true, value = """
            SELECT h.ACCOUNT_ID AS accountId, COUNT(*) AS quizzes,
                   SUM(h.RIGHT_ANSWERS) AS rightAnswers, SUM(h.TOTAL_ANSWERS) AS totalAnswers
            FROM Q_USER_HISTORY h
            JOIN Q_QUIZ q ON q.QUIZ_ID = h.QUIZ_ID
            JOIN Q_USER u ON u.ACCOUNT_ID = h.ACCOUNT_ID
            WHERE h.TOTAL_ANSWERS IS NOT NULL AND q.IS_CUSTOM = FALSE AND NOT COALESCE(u.IS_BLOCKED, FALSE)
              AND COALESCE(h.COMPLETED_DATE, h.CREATED_DATE) >= :since
            GROUP BY h.ACCOUNT_ID
            """)
    List<PlayerTotals> findPlayerTotalsSince(LocalDateTime since);

    interface PlayerTotals {
        Long getAccountId();
        Long getQuizzes();
        Long getRightAnswers();
        Long getTotalAnswers();
    }

    interface FirstRun {
        Long getAccountId();
        Integer getRightAnswers();
        Integer getTotalAnswers();
        Double getSpentTime();
    }

    // ---- What the trophies are judged on -------------------------------------------------------

    long countByAccount_AccountId(Long accountId);

    /** Runs per category, for the category trophies: one row each rather than one query each. */
    @Query("SELECT h.quiz.category.catId AS categoryId, COUNT(h) AS runs FROM UserQuizHistory h"
            + " WHERE h.account.accountId = :accountId AND h.quiz.category IS NOT NULL"
            + " GROUP BY h.quiz.category.catId")
    List<CategoryRuns> countByCategory(Long accountId);

    interface CategoryRuns {
        Long getCategoryId();
        long getRuns();
    }

    /**
     * When this player finished each of their runs, for the run-of-days trophies. The days are
     * worked out from these rather than in the query: a date function is the database's dialect,
     * and there is nothing here worth writing twice.
     */
    @Query("SELECT h.completedDate FROM UserQuizHistory h WHERE h.account.accountId = :accountId"
            + " AND h.completedDate IS NOT NULL")
    List<LocalDateTime> findCompletedDates(Long accountId);

    /** Runs with nothing wrong in them. Unmarked runs have no score and cannot be flawless. */
    @Query("SELECT COUNT(h) FROM UserQuizHistory h WHERE h.account.accountId = :accountId"
            + " AND h.totalAnswers > 0 AND h.rightAnswers = h.totalAnswers")
    long countFlawless(Long accountId);

    /**
     * Runs of a proper length finished inside a minute. SPENT_TIME is in seconds, and a run with
     * no time on it is one the clock never reached rather than an instant one. Ten questions at
     * least: three questions answered quickly is not the same feat, and the trophy should mean
     * the harder one.
     */
    @Query("SELECT COUNT(h) FROM UserQuizHistory h WHERE h.account.accountId = :accountId"
            + " AND h.totalAnswers >= 10 AND h.spentTime > 0 AND h.spentTime < 60")
    long countSwiftRuns(Long accountId);

    /** Runs finished in the small hours, which is a trophy of its own. */
    @Query("SELECT COUNT(h) FROM UserQuizHistory h WHERE h.account.accountId = :accountId"
            + " AND EXTRACT(HOUR FROM h.completedDate) BETWEEN 2 AND 4")
    long countNightRuns(Long accountId);

    // Loads them first, so each row's trophies go with it.
    void deleteByQuiz_QuizId(Long quizId);
}
