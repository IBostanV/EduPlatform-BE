package com.play.quiz.repository;

import com.play.quiz.domain.CustomQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

// A custom quiz's questions are found by the ids in its QUESTION_IDS (findAllById).
public interface CustomQuestionRepository extends JpaRepository<CustomQuestion, Long> {
}
