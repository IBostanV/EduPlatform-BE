package com.play.quiz.mapper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.play.quiz.domain.Quiz;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.dto.UserQuizHistoryDto;
import com.play.quiz.fixtures.QuizFixture;
import org.junit.jupiter.api.Test;

// Q_QUIZ.IS_CUSTOM goes out in QuizDto, but only the server sets it: a quiz a client sends back
// (with a played run) never carries it into an entity.
class QuizCustomFlagMappingTest {

    @Test
    void given_custom_quiz_when_mapped_to_dto_then_expose_the_flag() {
        Quiz quiz = QuizFixture.getQuiz().toBuilder().custom(true).build();

        assertTrue(new QuizMapperImpl().toDto(quiz).isCustom());
    }

    @Test
    void given_dto_claiming_custom_when_mapped_to_entity_then_ignore_it() {
        QuizDto claimed = QuizFixture.getQuizNoQuestionDto().toBuilder().custom(true).build();

        assertFalse(new QuizMapperImpl().toEntity(claimed).isCustom());
    }

    @Test
    void given_played_run_claiming_a_custom_quiz_when_mapped_to_entity_then_ignore_it() {
        UserQuizHistoryDto run = UserQuizHistoryDto.builder()
                .quiz(QuizFixture.getQuizNoQuestionDto().toBuilder().custom(true).build())
                .build();

        assertFalse(new UserQuizHistoryMapperImpl().toEntity(run).getQuiz().isCustom());
    }
}
