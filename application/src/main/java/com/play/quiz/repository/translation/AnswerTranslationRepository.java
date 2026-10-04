package com.play.quiz.repository.translation;

import java.util.Collection;
import java.util.List;

import com.play.quiz.domain.translation.AnswerTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerTranslationRepository extends JpaRepository<AnswerTranslation, Long> {

    List<AnswerTranslation> findAllByAnswer_AnsIdIn(Collection<Long> answerIds);
}
