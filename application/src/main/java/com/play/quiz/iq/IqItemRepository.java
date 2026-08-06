package com.play.quiz.iq;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IqItemRepository extends JpaRepository<IqItem, Long> {

    // The bank is seventy-odd items: read once per answer and picked over in memory, which is
    // cheaper and simpler than asking the database for the most informative one.
    List<IqItem> findAll();
}
