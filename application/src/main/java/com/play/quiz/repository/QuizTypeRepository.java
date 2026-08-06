package com.play.quiz.repository;

import com.play.quiz.domain.QuizType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizTypeRepository extends JpaRepository<QuizType, Long> {

    Optional<QuizType> findByBitValue(final Integer bitValue);
}
