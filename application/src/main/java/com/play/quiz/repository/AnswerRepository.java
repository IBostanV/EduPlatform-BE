package com.play.quiz.repository;

import java.util.List;

import com.play.quiz.domain.Answer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // Answers built from a glossary term: Q_ANSWER.TERM_ID blocks deleting the term.
    @Query("SELECT COUNT(a) FROM Answer a WHERE a.glossary.termId = :termId")
    long countUsingGlossary(Long termId);
}
