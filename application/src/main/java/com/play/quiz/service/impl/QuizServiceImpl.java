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
import com.play.quiz.util.ServerText;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.play.quiz.util.Constant.DEFAULT_EXPRESS_QUESTIONS_COUNT;
import static com.play.quiz.util.Constant.DEFAULT_QUIZ_QUESTIONS_COUNT;
import static com.play.quiz.util.Constant.EXPRESS_QUIZ_DEFAULT_TIME_SECONDS;

@Log4j2
@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

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
        log.info("Created quiz id: {} with {} questions", quiz.getQuizId(), quiz.getQuestionsCount());

        return quizMapper.toDto(quiz);
    }

    private static void handleQuestionDiscrepancy(final QuizDto quizDto, final List<Question> questions) {
        // Nothing to repeat: without this getFirst() throws and the player sees a bare 500.
        if (questions.isEmpty() && quizDto.getQuestionsCount() > 0) {
            throw new IllegalArgumentException(ServerText.t("err_category_no_questions", "This category has no questions yet"));
        }
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
    public QuizDto getExpressQuiz(final String email) {
        int count = propertyRepository.findByName(DEFAULT_EXPRESS_QUESTIONS_COUNT).getIntValue();
        int quizTime = propertyRepository.findByName(EXPRESS_QUIZ_DEFAULT_TIME_SECONDS).getIntValue();

        // Half at most, so the quiz stays general knowledge with a lean, not a quiz on one topic.
        List<Question> questions = new ArrayList<>(Objects.isNull(email)
                ? List.of() : questionService.getOccupationQuestions(email, count / 2));
        Set<Long> taken = getQuestionIds(questions);
        questionService.getGeneralKnowledgeQuestions(count).stream()
                .filter(question -> !taken.contains(question.getQuestionId()))
                .limit(count - questions.size())
                .forEach(questions::add);
        log.debug("Express quiz: {} of {} questions, {} from occupation", questions.size(), count, taken.size());

        // No category: it is general knowledge, and is stored that way (see the CAT_ID migration).
        return createQuiz(quizTime, questions.size(), null, null, questions);
    }

    @Override
    // Read-only for the same reason as getExpressQuiz above.
    @Transactional(readOnly = true)
    public QuizDto replay(final Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RecordNotFoundException("No quiz with id: " + quizId));
        if (quiz.isCustom()) {
            throw new IllegalArgumentException(ServerText.t("err_custom_quiz_own_page", "A custom quiz is played from its own page"));
        }
        // The ids are stored as a set, so a fixed order: shuffle, or every replay asks them the same way.
        List<Question> questions = quiz.getQuestionIds().stream()
                .<Question>map(id -> Question.builder().questionId(id).build())
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(questions);
        CategoryDto category = Objects.nonNull(quiz.getCategory())
                ? categoryService.getById(quiz.getCategory().getCatId(), null) : null;

        return createQuiz(0, questions.size(), quiz.getType(), category, questions)
                .toBuilder()
                .quizId(quizId)
                .build();
    }

    @Override
    @Transactional
    public Long storeGeneralKnowledgeQuiz(final int questionCount) {
        List<Question> questions = questionService.getGeneralKnowledgeQuestions(questionCount);
        Quiz quiz = quizRepository.save(Quiz.builder()
                // Q_QUIZ.TYPE is NOT NULL: the platform's own type, as a quiz played with no filter gets.
                .type(quizTypeRepository.findById(1L).orElse(null))
                .questionIds(getQuestionIds(questions))
                .questionsCount(questions.size())
                .createdDate(LocalDateTime.now())
                .build());
        log.info("Stored general knowledge quiz id: {} with {} questions", quiz.getQuizId(), questions.size());
        return quiz.getQuizId();
    }

    @Override
    @Transactional
    public Long storeQuestionsQuiz(final Set<Long> questionIds) {
        Quiz quiz = quizRepository.save(Quiz.builder()
                // Q_QUIZ.TYPE is NOT NULL: the platform's own type, as a quiz played with no filter gets.
                .type(quizTypeRepository.findById(1L).orElse(null))
                .questionIds(questionIds)
                .questionsCount(questionIds.size())
                .createdDate(LocalDateTime.now())
                .build());
        log.info("Stored quiz id: {} of {} given questions", quiz.getQuizId(), questionIds.size());
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
        Long bits = userQuizParams.getQuizType();
        Optional<QuizType> quizType = Optional.ofNullable(bits)
                .flatMap(value -> quizTypeRepository.findByBitValue(value.intValue()));
        if (Objects.nonNull(bits) && quizType.isEmpty()) {
            log.debug("No single quiz type has bit value: {}, quiz goes without a type", bits);
        }
        return quizType.orElse(null);
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
                        .map(question -> questionService.getQuestionWithAnswerOptions(question.getQuestionId(),
                                Objects.isNull(quizType) ? null : quizType.getName()))
                        .toList())
                .build();
    }
}
