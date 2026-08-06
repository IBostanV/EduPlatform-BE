package com.play.quiz.repository;

import java.util.List;

import com.play.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    Quiz findFirstByCategory_CatId(final Long categoryId);

    // The custom quizzes one player made, newest first.
    List<Quiz> findByCustomTrueAndCreatedBy_EmailOrderByCreatedDateDesc(final String email);

    // Every custom quiz, newest first: the admins' list.
    List<Quiz> findByCustomTrueOrderByCreatedDateDesc();
}
