package com.play.quiz.repository;

import java.util.List;

import com.play.quiz.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Open ones first, each group newest first.
    List<Feedback> findAllByOrderByResolvedAscCreatedDateDesc();

    long countByResolvedFalse();
}
