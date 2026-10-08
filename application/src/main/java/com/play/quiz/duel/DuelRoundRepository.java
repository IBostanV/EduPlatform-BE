package com.play.quiz.duel;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DuelRoundRepository extends JpaRepository<DuelRound, DuelRound.Key> {

    List<DuelRound> findByDuelIdInOrderByRoundNo(Collection<Long> duelIds);
}
