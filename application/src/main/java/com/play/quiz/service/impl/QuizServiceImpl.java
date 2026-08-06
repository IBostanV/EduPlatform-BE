package com.play.quiz.service.impl;

import com.play.quiz.domain.Category;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.domain.Property;
import com.play.quiz.domain.Question;
import com.play.quiz.domain.Quiz;
import com.play.quiz.domain.QuizType;
import com.play.quiz.dto.CategoryDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.dto.UserQuizParams;
import com.play.quiz.mapper.QuestionMapper;
import com.play.quiz.mapper.QuizMapper;
import com.play.quiz.repository.PropertyRepository;
import com.play.quiz.repository.QuizRepository;
import com.play.quiz.repository.QuizTypeRepository;
import com.play.quiz.service.CategoryService;
import com.play.quiz.service.QuestionService;
import com.play.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.play.quiz.util.Constant.DEFAULT_EXPRESS_QUESTIONS_COUNT;
import static com.play.quiz.util.Constant.DEFAULT_QUIZ_QUESTIONS_COUNT;
import static com.play.quiz.util.Constant.EXPRESS_QUIZ_DEFAULT_TIME_SECONDS;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private static final Long EXPRESS_CATEGORY_ID = 1L;
    // However long a player asks for, one quiz never runs past this.
    private static final int MAX_QUIZ_LENGTH = 50;

    private final CategoryService categoryService;
    private final PropertyRepository propertyRepository;
    private final QuestionMapper questionMapper;
    private final QuestionService questionService;
    private final QuizMapper quizMapper;
    private final QuizRepository quizRepository;
    private final QuizTypeRepository quizTypeRepository;

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
    // Read-only, and it has to be: filling a question's answer options adds the drawn wrong
    // options to the question's own list in memory, which a writable transaction would flush.
    @Transactional(readOnly = true)
    public QuizDto getExpressQuiz() {
        Property questionsCount = propertyRepository.findByName(DEFAULT_EXPRESS_QUESTIONS_COUNT);
        int quizTime = propertyRepository.findByName(EXPRESS_QUIZ_DEFAULT_TIME_SECONDS).getIntValue();
        CategoryDto category = categoryService.getById(EXPRESS_CATEGORY_ID, null);
        List<Question> questions = questionService.getGeneralKnowledgeQuestions(questionsCount.getIntValue());

        return createQuiz(quizTime, questionsCount.getIntValue(), null, category, questions);
    }

    @Override
    // Read-only for the same reason as getExpressQuiz above.
    @Transactional(readOnly = true)
    public QuizDto replay(final Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RecordNotFoundException("No quiz with id: " + quizId));
        if (quiz.isCustom()) {
            throw new IllegalArgumentException("A custom quiz is played from its own page");
        }
        List<Question> questions = quiz.getQuestionIds().stream()
                .<Question>map(id -> Question.builder().questionId(id).build())
                .toList();
        Long categoryId = Objects.nonNull(quiz.getCategory()) ? quiz.getCategory().getCatId() : EXPRESS_CATEGORY_ID;

        return createQuiz(0, questions.size(), quiz.getType(), categoryService.getById(categoryId, null), questions)
                .toBuilder()
                .quizId(quizId)
                .build();
    }

    @Override
    @Transactional
    public Long storeGeneralKnowledgeQuiz(final int questionCount) {
        List<Question> questions = questionService.getGeneralKnowledgeQuestions(questionCount);
        Quiz quiz = quizRepository.save(Quiz.builder()
                .category(Category.builder().catId(EXPRESS_CATEGORY_ID).build())
                // Q_QUIZ.TYPE is NOT NULL: the platform's own type, as a quiz played with no filter gets.
                .type(quizTypeRepository.findById(1L).orElse(null))
                .questionIds(getQuestionIds(questions))
                .questionsCount(questions.size())
                .createdDate(LocalDateTime.now())
                .build());
        return quiz.getQuizId();
    }

    @Override
    public List<QuizType> getQuizTypes() {
        return quizTypeRepository.findAll();
    }

    @Override
    // Read-only for the same reason as getExpressQuiz above.
    @Transactional(readOnly = true)
    public QuizDto getQuizByCategoryAndParams(Long catId, UserQuizParams userQuizParams) {
        int questionsCount = askedLength(userQuizParams);
        List<Question> questionList = questionService.getByCategoryIdAndParams(catId, questionsCount, userQuizParams);
        CategoryDto categoryDto = categoryService.getById(catId, null);

        // The quiz is as long as what was actually found: a filter can leave fewer questions than
        // the length asked for.
        return createQuiz(0, questionList.size(), getQuizType(userQuizParams), categoryDto, questionList);
    }

    /** The length the player picked, capped; without one, the system's default. */
    private int askedLength(final UserQuizParams userQuizParams) {
        return Optional.ofNullable(userQuizParams.getQuestionCount())
                .filter(count -> count > 0)
                .map(count -> Math.min(count, MAX_QUIZ_LENGTH))
                .orElseGet(() -> propertyRepository.findByName(DEFAULT_QUIZ_QUESTIONS_COUNT).getIntValue());
    }

    private QuizType getQuizType(final UserQuizParams userQuizParams) {
        return Optional.ofNullable(userQuizParams.getQuizType())
                .flatMap(value -> quizTypeRepository.findByBitValue(value.intValue()))
                .orElse(null);
    }

    /**
     * The quiz as its player receives it: the questions travel with it, each with its answer
     * options, the way a custom quiz already arrives. The browser used to ask for them one at a
     * time, which is a request per question per run — the site's largest source of traffic by far.
     */
    private QuizDto createQuiz(int quizTime,
                               int questionCount,
                               final QuizType quizType,
                               final CategoryDto category,
                               final List<Question> questionList) {
        return QuizDto.builder()
                .category(category)
                .quizTime(quizTime)
                .quizType(quizType)
                .questionsCount(questionCount)
                .createdDate(LocalDateTime.now())
                .questionIds(getQuestionIds(questionList))
                // ponytail: the options are filled one question at a time, which looks up the
                // options-count property once per question; fold it out if quizzes get long.
                .questionList(questionList.stream()
                        .map(question -> questionService.getQuestionWithAnswerOptions(question.getQuestionId()))
                        .toList())
                .build();
    }
}
