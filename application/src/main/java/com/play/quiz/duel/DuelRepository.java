package com.play.quiz.duel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DuelRepository extends JpaRepository<Duel, Long> {

    @Query("SELECT d FROM Duel d WHERE d.challengerId = :accountId OR d.opponentId = :accountId ORDER BY d.createdDate DESC")
    List<Duel> findInvolving(Long accountId);
}
