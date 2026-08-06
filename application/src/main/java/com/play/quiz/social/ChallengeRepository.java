package com.play.quiz.social;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    /** Everything this player sent or was sent since then, newest first. */
    @Query("SELECT c FROM Challenge c WHERE (c.challengerId = :accountId OR c.opponentId = :accountId)"
            + " AND c.createdDate >= :since ORDER BY c.createdDate DESC")
    List<Challenge> findInvolving(Long accountId, LocalDateTime since);

    boolean existsByQuizIdAndOpponentId(Long quizId, Long opponentId);
}
