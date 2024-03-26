package com.play.quiz.repository.translation;

import com.play.quiz.domain.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationRepository extends JpaRepository<Translation, Long> {
}
