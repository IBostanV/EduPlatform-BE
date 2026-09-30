package com.play.quiz.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import com.play.quiz.domain.QuizType;
import com.play.quiz.fixtures.QuizTypeFixture;
import com.play.quiz.repository.QuizTypeRepository;
import com.play.quiz.service.impl.QuizTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuizTypeServiceImplTest {

    private static final QuizType SINGLE_CHOICE = QuizTypeFixture.getQuizType(1L, "SINGLE_CHOICE", 1);
    private static final QuizType MULTIPLE_CHOICE = QuizTypeFixture.getQuizType(2L, "MULTIPLE_CHOICE", 2);
    private static final QuizType INPUT = QuizTypeFixture.getQuizType(4L, "INPUT", 8);

    @Mock
    private QuizTypeRepository quizTypeRepository;

    @InjectMocks
    private QuizTypeServiceImpl quizTypeService;

    @Test
    void given_type_bits_when_includesType_then_return_matching_types() {
        when(quizTypeRepository.findAll()).thenReturn(List.of(SINGLE_CHOICE, MULTIPLE_CHOICE, INPUT));

        assertEquals(List.of(SINGLE_CHOICE, INPUT), quizTypeService.includesType(9L));
    }

    @Test
    void given_null_type_bits_when_includesType_then_return_empty_list() {
        assertEquals(List.of(), quizTypeService.includesType(null));
    }

    @Test
    void given_quiz_types_when_toTypeBits_then_return_combined_bits() {
        assertEquals(9, quizTypeService.toTypeBits(List.of(SINGLE_CHOICE, INPUT)));
        assertEquals(0, quizTypeService.toTypeBits(null));
    }

    @Test
    void given_all_types_when_includeOnlyInputType_then_return_bits_without_input() {
        when(quizTypeRepository.findAll()).thenReturn(List.of(SINGLE_CHOICE, MULTIPLE_CHOICE, INPUT));

        assertEquals(3, quizTypeService.includeOnlyInputType());
    }
}
