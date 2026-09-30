package com.play.quiz.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.play.quiz.domain.Answer;
import com.play.quiz.domain.Glossary;
import com.play.quiz.domain.GlossaryType;
import com.play.quiz.domain.Property;
import com.play.quiz.domain.Question;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.fixtures.CategoryFixture;
import com.play.quiz.mapper.QuestionMapper;
import com.play.quiz.repository.PropertyRepository;
import com.play.quiz.repository.QuestionRepository;
import com.play.quiz.service.impl.QuestionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 * Where the wrong options of a question come from. A wrong option has to be the same kind of
 * thing as the right one — "What is the capital of France?" answered against a population and two
 * currencies gives itself away — so they are drawn from the right answer's glossary type and
 * nowhere else.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AnswerOptionSourceTest {

    private static final long QUESTION_ID = 3L;
    private static final int OPTIONS_WANTED = 4;

    @Mock private QuestionMapper questionMapper;
    @Mock private QuestionRepository questionRepository;
    @Mock private PropertyRepository propertyRepository;
    @Mock private AnswerService answerService;

    @InjectMocks
    private QuestionServiceImpl questionService;

    @BeforeEach
    void init() {
        when(propertyRepository.findByName(any()))
                .thenReturn(Property.builder().value(String.valueOf(OPTIONS_WANTED)).build());
        when(questionMapper.mapToDto(any())).thenReturn(QuestionDto.builder()
                .answers(new ArrayList<>())
                .build());
    }

    @Test
    void given_an_answer_with_a_glossary_type_when_options_are_short_then_draw_them_from_that_type() {
        GlossaryType capitals = GlossaryType.builder().id(8L).name("Capital").build();
        asked(questionWithAnswerIn(capitals));

        questionService.getQuestionWithAnswerOptions(QUESTION_ID);

        verify(answerService).getWrongOptionsByGlossaryTypeWithLimit(
                eqType(capitals), anyList(), anyInt(), anyBoolean());
    }

    // Nothing plausible to draw from, so nothing is drawn: the question goes out short rather
    // than with options of a different kind, which would hand the player the answer.
    @Test
    void given_an_answer_with_no_glossary_type_when_options_are_short_then_invent_none() {
        Question question = questionWithAnswerIn(null);
        asked(question);

        questionService.getQuestionWithAnswerOptions(QUESTION_ID);

        verifyNoInteractions(answerService);
        assertEquals(1, question.getAnswers().size(), "The answer it already had, and no others");
    }

    @Test
    void given_a_question_that_already_has_enough_options_then_draw_nothing() {
        GlossaryType capitals = GlossaryType.builder().id(8L).name("Capital").build();
        Question question = questionWithAnswerIn(capitals);
        while (question.getAnswers().size() < OPTIONS_WANTED) {
            question.getAnswers().add(Answer.builder().content("Another").build());
        }
        asked(question);

        questionService.getQuestionWithAnswerOptions(QUESTION_ID);

        verify(answerService, never()).getWrongOptionsByGlossaryTypeWithLimit(any(), anyList(), anyInt(), anyBoolean());
    }

    private void asked(final Question question) {
        when(questionRepository.getReferenceById(QUESTION_ID)).thenReturn(question);
        when(answerService.getWrongOptionsByGlossaryTypeWithLimit(any(), anyList(), anyInt(), anyBoolean()))
                .thenReturn(Collections.emptyList());
    }

    // One right answer, built from a glossary term that may or may not have been given a type.
    private static Question questionWithAnswerIn(final GlossaryType type) {
        Glossary term = Glossary.builder()
                .termId(1L)
                .key("France")
                .value("Paris")
                .type(type)
                .category(CategoryFixture.getCategory())
                .build();

        Question question = Question.builder()
                .questionId(QUESTION_ID)
                .content("What is the capital of France?")
                .category(CategoryFixture.getCategory())
                .attributes(Collections.emptyList())
                .answers(new ArrayList<>(List.of(Answer.builder().ansId(1L).content("Paris").glossary(term).build())))
                .build();

        question.getAnswers().forEach(answer -> answer.setQuestion(question));
        return question;
    }

    private static GlossaryType eqType(final GlossaryType type) {
        return org.mockito.ArgumentMatchers.argThat(given -> given != null && given.getId().equals(type.getId()));
    }
}
