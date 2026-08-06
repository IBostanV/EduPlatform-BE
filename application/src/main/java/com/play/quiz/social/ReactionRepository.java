package com.play.quiz.social;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    List<Reaction> findByItemKeyIn(Collection<String> itemKeys);

    Optional<Reaction> findByAccountIdAndItemKeyAndKind(Long accountId, String itemKey, Reaction.Kind kind);
}
