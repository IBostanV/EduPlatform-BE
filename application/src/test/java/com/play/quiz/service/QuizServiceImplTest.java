package com.play.quiz.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.play.quiz.dto.QuestionDto;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.dto.UserQuizParams;
import com.play.quiz.fixtures.CategoryFixture;
import com.play.quiz.fixtures.QuestionFixture;
import com.play.quiz.fixtures.QuizFixture;
import com.play.quiz.mapper.QuestionMapper;
import com.play.quiz.mapper.QuizMapper;
import com.play.quiz.mapper.QuizMapperImpl;
import com.play.quiz.domain.Question;
import com.play.quiz.domain.Quiz;
import com.play.quiz.domain.Property;
import com.play.quiz.repository.PropertyRepository;
import static com.play.quiz.util.Constant.DEFAULT_EXPRESS_QUESTIONS_COUNT;
import static com.play.quiz.util.Constant.DEFAULT_QUIZ_QUESTIONS_COUNT;
import static com.play.quiz.util.Constant.EXPRESS_QUIZ_DEFAULT_TIME_SECONDS;
import com.play.quiz.repository.QuizRepository;
import com.play.quiz.repository.QuizTypeRepository;
import com.play.quiz.service.impl.QuizServiceImpl;
import com.play.quiz.util.EncryptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionService questionService;

    @Mock
    private QuestionMapper questionMapper;

    @Mock
    private CategoryService categoryService;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private QuizTypeRepository quizTypeRepository;

    @Mock
    private GlossaryService glossaryService;

    private QuizService quizService;

    @BeforeEach
    void init() {
        QuizMapper quizMapper = new QuizMapperImpl();
        EncryptionUtils encryptionUtils = new EncryptionUtils(propertyRepository);
        quizService = new QuizServiceImpl(categoryService, propertyRepository, questionMapper, questionService,
                quizMapper, quizRepository, quizTypeRepository);
    }

    @Test
    void given_quizDto_when_create_then_return_quiz_with_questions() {
        Quiz quiz = QuizFixture.getQuiz();
        QuizDto quizDto = QuizFixture.getQuizNoQuestionDto();
        QuestionDto questionDto = QuestionFixture.getQuestionDto();
        List<QuestionDto> questionDtos = Collections.singletonList(questionDto);
        Question question = QuestionFixture.getNoAnswerQuestion(1L, "Life");

        when(questionService.findByCategory(CategoryFixture.getSimpleCategory())).thenReturn(questionDtos);
        when(questionMapper.mapToEntityList(questionDtos)).thenReturn(new ArrayList<>(Collections.singletonList(question)));
        when(quizRepository.save(any())).thenReturn(quiz);

        QuizDto result = quizService.create(quizDto);

        assertEquals(1L, result.getQuizId());
        assertEquals(5, result.getQuestionsCount());
        assertEquals(Collections.singleton(1L), result.getQuestionIds());
        assertEquals(CategoryFixture.getCategoryDto(), result.getCategory());
        assertEquals(5, result.getQuestionList().size());
        assertTrue(result.getQuestionList().contains(QuestionFixture.getQuestionDto()));

        verify(questionService, only()).findByCategory(CategoryFixture.getSimpleCategory());
        verify(quizRepository, only()).save(any());
    }

    @Test
    void given_an_express_quiz_when_it_is_built_then_its_questions_travel_with_it() {
        when(propertyRepository.findByName(DEFAULT_EXPRESS_QUESTIONS_COUNT))
                .thenReturn(Property.builder().value("1").build());
        when(propertyRepository.findByName(EXPRESS_QUIZ_DEFAULT_TIME_SECONDS))
                .thenReturn(Property.builder().value("60").build());
        when(categoryService.getById(1L, null)).thenReturn(CategoryFixture.getCategoryDto());
        when(questionService.getGeneralKnowledgeQuestions(1))
                .thenReturn(List.of(QuestionFixture.getNoAnswerQuestion(1L, "Life")));
        when(questionService.getQuestionWithAnswerOptions(1L)).thenReturn(QuestionFixture.getQuestionDto());

        QuizDto result = quizService.getExpressQuiz();

        // Everything the browser needs to ask the whole quiz: no request per question.
        assertEquals(Set.of(1L), result.getQuestionIds());
        assertEquals(1, result.getQuestionList().size());
        assertEquals(QuestionFixture.getQuestionDto().getAnswers(), result.getQuestionList().getFirst().getAnswers());
    }

    @Test
    void given_a_length_in_the_filters_when_building_a_categorized_quiz_then_ask_for_that_many() {
        when(questionService.getByCategoryIdAndParams(eq(7L), eq(5), any())).thenReturn(List.of());
        when(categoryService.getById(7L, null)).thenReturn(CategoryFixture.getCategoryDto());

        QuizDto result = quizService.getQuizByCategoryAndParams(7L,
                UserQuizParams.builder().questionCount(5).build());

        // The system's default length is not even looked up when the player picked one.
        verify(propertyRepository, never()).findByName(any());
        assertEquals(0, result.getQuestionsCount());
    }

    @Test
    void given_no_length_in_the_filters_when_building_a_categorized_quiz_then_use_the_default() {
        when(propertyRepository.findByName(DEFAULT_QUIZ_QUESTIONS_COUNT))
                .thenReturn(Property.builder().value("20").build());
        when(questionService.getByCategoryIdAndParams(eq(7L), eq(20), any())).thenReturn(List.of());
        when(categoryService.getById(7L, null)).thenReturn(CategoryFixture.getCategoryDto());

        quizService.getQuizByCategoryAndParams(7L, UserQuizParams.builder().build());

        verify(questionService).getByCategoryIdAndParams(eq(7L), eq(20), any());
    }
}
