package com.play.quiz.service.impl;

import com.play.quiz.domain.Category;
import com.play.quiz.domain.Property;
import com.play.quiz.domain.Question;
import com.play.quiz.domain.Quiz;
import com.play.quiz.dto.CategoryDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.mapper.QuestionMapper;
import com.play.quiz.mapper.QuizMapper;
import com.play.quiz.repository.PropertyRepository;
import com.play.quiz.repository.QuizRepository;
import com.play.quiz.service.CategoryService;
import com.play.quiz.service.QuestionService;
import com.play.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.play.quiz.util.Constant.DEFAULT_EXPRESS_QUESTIONS_COUNT;
import static com.play.quiz.util.Constant.DEFAULT_QUIZ_QUESTIONS_COUNT;
import static com.play.quiz.util.Constant.EXPRESS_QUIZ_DEFAULT_TIME_SECONDS;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private static final Long EXPRESS_CATEGORY_ID = 1L;

    private final QuizMapper quizMapper;
    private final QuizRepository quizRepository;
    private final QuestionMapper questionMapper;
    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final PropertyRepository propertyRepository;

    @Override
    public QuizDto create(final QuizDto quizDto) {
        List<QuestionDto> questionsByCategory = questionService.findByCategory(getCategory(quizDto));
        List<Question> questions = questionMapper.mapToEntityList(questionsByCategory);
        handleQuestionDiscrepancy(quizDto, questions);
        Quiz quiz = quizRepository.save(buildQuiz(questions, quizDto));

        return quizMapper.toDto(quiz);
    }

    private static void handleQuestionDiscrepancy(final QuizDto quizDto, final List<Question> questions) {
        while (questions.size() < quizDto.getQuestionsCount()) {
            Collections.shuffle(questions);
            questions.add(questions.getFirst());
        }
    }

    private Quiz buildQuiz(final List<Question> questionList, final QuizDto quizDto) {
        Collections.shuffle(questionList);
        List<Question> quizQuestions = questionList.subList(0, quizDto.getQuestionsCount());

        return Quiz.builder()
                .questionList(questionList)
                .category(getCategory(quizDto))
                .questionsCount(quizQuestions.size())
                .createdDate(quizDto.getCreatedDate())
                .questionIds(getQuestionIds(quizQuestions))
                .build();
    }

    private static Category getCategory(final QuizDto quizDto) {
        return Category.builder()
                .catId(quizDto.getCategory().getCatId())
                .name(quizDto.getCategory().getName())
                .naturalId(quizDto.getCategory().getNaturalId())
                .build();
    }

    private static Set<Long> getQuestionIds(final List<Question> quizQuestions) {
        return quizQuestions.stream()
                .map(Question::getQuestionId)
                .collect(Collectors.toSet());
    }

    @Override
    public QuizDto getById(final Long quizId) {
        Quiz quiz = quizRepository.getReferenceById(quizId);
        return quizMapper.toDto(quiz);
    }

    @Override
    @Transactional
    public QuizDto getExpressQuiz() {
        Property questionsCount = propertyRepository.findByName(DEFAULT_EXPRESS_QUESTIONS_COUNT);
        int quizTime = propertyRepository.findByName(EXPRESS_QUIZ_DEFAULT_TIME_SECONDS).getIntValue();
        CategoryDto category = categoryService.getById(EXPRESS_CATEGORY_ID, null);
        List<Question> questions = questionService.getGeneralKnowledgeQuestions(questionsCount.getIntValue());

        return createQuiz(quizTime, questionsCount.getIntValue(), category, questions);
    }

    @Override
    public QuizDto getQuizByCategory(Long catId) {
        int questionsCount = propertyRepository.findByName(DEFAULT_QUIZ_QUESTIONS_COUNT).getIntValue();
        List<Question> questionList = questionService.getByCategoryId(catId, questionsCount);
        CategoryDto categoryDto = categoryService.getById(catId, null);

        return createQuiz(0, questionsCount, categoryDto, questionList);
    }

    private QuizDto createQuiz(int quizTime,
                               int questionCount,
                               final CategoryDto category,
                               final List<Question> questionList) {
        return QuizDto.builder()
                .category(category)
                .quizTime(quizTime)
                .questionsCount(questionCount)
                .createdDate(LocalDateTime.now())
                .questionIds(getQuestionIds(questionList))
                .build();
    }
}
