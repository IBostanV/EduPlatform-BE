package com.play.quiz.service.impl;

import static com.play.quiz.util.Constant.EXPRESS_QUIZ_DEFAULT_OPTIONS_COUNT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.play.quiz.domain.Answer;
import com.play.quiz.domain.Category;
import com.play.quiz.domain.Glossary;
import com.play.quiz.domain.GlossaryType;
import com.play.quiz.domain.Property;
import com.play.quiz.domain.Question;
import com.play.quiz.dto.AnswerDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.dto.UserQuizParams;
import com.play.quiz.engine.QuestionGenerationEngine;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.enums.QuestionAttribute;
import com.play.quiz.mapper.QuestionMapper;
import com.play.quiz.record.MiniGameResult;
import com.play.quiz.record.PageResponse;
import com.play.quiz.repository.PropertyRepository;
import com.play.quiz.repository.QuestionRepository;
import com.play.quiz.service.AnswerService;
import com.play.quiz.service.GlossaryService;
import com.play.quiz.service.QuestionService;
import com.play.quiz.util.Numbers;
import com.play.quiz.util.SystemAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Log4j2
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private static final int MAX_PAGE_SIZE = 100;

    // Columns the admin table may sort by -> entity property. Only these are accepted, so a
    // client cannot sort (and probe) by arbitrary fields such as account details.
    private static final Map<String, String> SORTABLE = Map.of(
            "topic", "topic",
            "priority", "priority",
            "type", "type",
            "complexityLevel", "complexityLevel",
            "content", "content",
            "category", "category.name",
            "isActive", "isActive",
            "createdDate", "createdDate");
    private static final Sort NEWEST_FIRST = Sort.by(Sort.Direction.DESC, "questionId");

    private final AnswerService answerService;
    private final QuestionMapper questionMapper;
    private final GlossaryService glossaryService;
    private final PropertyRepository propertyRepository;
    private final QuestionRepository questionRepository;
    private final QuestionGenerationEngine generationEngine;

    @Override
    @Transactional
    public QuestionDto save(final QuestionDto questionDto) {
        Question question = questionMapper.mapToEntity(questionDto);
        Question savedQuestion = questionRepository.save(processQuestion(question));
        log.info("Saved question id: {}, type: {}, answers: {}",
                savedQuestion.getQuestionId(), savedQuestion.getType(), savedQuestion.getAnswers().size());

        return questionMapper.mapToDto(savedQuestion);
    }

    private Question processQuestion(final Question question) {
        question.fillAnswersParent();
        question.fillTranslationsParent();
        handleAnswersContent(question);

        return question;
    }

    private void handleAnswersContent(final Question question) {
        question.getAnswers().forEach(answer -> Optional.ofNullable(answer.getGlossary())
                .ifPresent(glossary -> answer.setContent(processAnswerByGlossary(question.getAttributes(), glossary))));
    }

    private String processAnswerByGlossary(final List<QuestionAttribute> attributes, final Glossary answerGlossary) {
        Glossary glossary = glossaryService.getEntityById(answerGlossary.getTermId());
        return attributes.contains(QuestionAttribute.ANSWER_BY_KEY) ? glossary.getKey() : glossary.getValue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionDto> findAll() {
        List<Question> questions = questionRepository.findAll();
        return questionMapper.mapToDtoList(questions);
    }

    // Sorted by the requested column, else newest first. Size is capped so a client cannot ask
    // for the whole table in one "page".
    @Override
    @Transactional(readOnly = true)
    public PageResponse<QuestionDto> findPage(int page, int size, String query, String sort, String direction) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.clamp(size, 1, MAX_PAGE_SIZE), toSort(sort, direction));
        Page<Question> questions = StringUtils.hasText(query)
                ? questionRepository.search("%" + query.trim().toLowerCase(Locale.ROOT) + "%", pageable)
                : questionRepository.findAll(pageable);
        return PageResponse.of(questions, questionMapper.mapToDtoList(questions.getContent()));
    }

    /**
     * Edits the question itself: text, topic, type, complexity, priority, active flag, category
     * and attributes. Answers, translations and excluded quiz types are left untouched. The stored
     * question is copied with only these fields replaced, so its created date, author and
     * children survive (saving the DTO would null what it does not carry).
     */
    @Override
    @Transactional
    public QuestionDto update(final Long questionId, final QuestionDto changes) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RecordNotFoundException("No question with id: " + questionId));

        Question updated = question.toBuilder()
                .content(changes.getContent())
                .topic(changes.getTopic())
                .type(changes.getType())
                .complexityLevel(changes.getComplexityLevel())
                .priority(changes.getPriority())
                .isActive(changes.getIsActive())
                .category(changes.getCategoryId() == null ? question.getCategory()
                        : Category.builder().catId(changes.getCategoryId()).build())
                .attributes(changes.getAttributes() == null ? question.getAttributes() : changes.getAttributes())
                .build();

        Question saved = questionRepository.save(updated);
        log.info("Updated question id: {}, type: {}, active: {}", questionId, saved.getType(), saved.getIsActive());
        return questionMapper.mapToDto(saved);
    }

    // Answers and translations go with it (cascade on the entity), which is also what the
    // foreign keys to Q_QUESTION require.
    @Override
    @Transactional
    public void delete(final Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RecordNotFoundException("No question with id: " + questionId));
        log.info("Deleting question {}", questionId);
        questionRepository.delete(question);
    }

    // Unknown or missing column: newest first. Ties always fall back to newest first, so a
    // page boundary never splits equal values differently between requests.
    private static Sort toSort(String sort, String direction) {
        String property = sort == null ? null : SORTABLE.get(sort);
        if (property == null) {
            return NEWEST_FIRST;
        }
        Sort.Direction order = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(order, property).and(NEWEST_FIRST);
    }

    @Override
    public List<QuestionDto> findByCategory(final Category category) {
        List<Question> questions = questionRepository.findByCategory_naturalId(category.getNaturalId());
        return questionMapper.mapToDtoList(questions);
    }

    @Override
    public List<QuestionDto> generateFromTemplate(final QuestionDto questionDto) {
        SystemAssert.isTemplateQuestion(questionDto);
        Question question = questionMapper.mapToEntity(questionDto);
        return questionMapper.mapToDtoList(generationEngine.generateFromCreatedTemplate(question));
    }

    @Override
    public void deactivate(final Long questionId) {
        questionRepository.deactivate(questionId);
        log.info("Deactivated question id: {}", questionId);
    }

    @Override
    public List<Question> getGeneralKnowledgeQuestions(int questionCount) {
        return questionRepository.findRandom(questionCount);
    }

    @Override
    public List<Question> getOccupationQuestions(final String email, final int count) {
        return questionRepository.findOccupationQuestions(email, count);
    }

    @Override
    public Question getById(final Long questionId) {
        return questionRepository.getReferenceById(questionId);
    }

    @Override
    public List<Question> getByIds(final Set<Long> idList) {
        return questionRepository.findAllByQuestionIdIn(idList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnswerDto> getAnswers(final Long questionId) {
        Question question = questionRepository.getReferenceById(questionId);
        return questionMapper.mapToDto(question).getAnswers();
    }

    @Override
    public List<Question> getByCategoryIdAndParams(Long catId, int questionCount, UserQuizParams userQuizParams) {
        Long quizType = userQuizParams.getQuizType();
        List<Question> questions = questionRepository.getByCategoryAndParams(catId, quizType, userQuizParams.getComplexityFrom(),
                userQuizParams.getComplexityTo(), questionCount);
        log.debug("Found {} of {} questions for category id: {}, type bits: {}, complexity: {}-{}", questions.size(),
                questionCount, catId, quizType, userQuizParams.getComplexityFrom(), userQuizParams.getComplexityTo());
        return questions;
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionDto getQuestionWithAnswerOptions(final Long questionId) {
        return getQuestionWithAnswerOptions(questionId, null);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionDto getQuestionWithAnswerOptions(final Long questionId, final String quizType) {
        Question question = questionRepository.getReferenceById(questionId);
        handleAnswerCountDiscrepancy(question);
        QuestionDto dto = questionMapper.mapToDto(question);
        // The right answers come first and the filled-in wrong ones after, so shuffle, or the
        // first option would always be right. Wrong options drawn from other questions can
        // repeat an option ("South America" twice), so keep the first of each text.
        Set<String> seen = new HashSet<>();
        List<AnswerDto> options = new ArrayList<>(dto.getAnswers().stream()
                .filter(answer -> answer.getContent() == null || seen.add(answer.getContent().trim().toLowerCase(Locale.ROOT)))
                .toList());
        options = shapeFor(quizType, options);
        Collections.shuffle(options);
        dto.setAnswers(options);
        return dto;
    }

    /**
     * The options as a quiz type plays them. {@code options} has the right answer first, as
     * getQuestionWithAnswerOptions builds it before the shuffle.
     */
    private static List<AnswerDto> shapeFor(final String quizType, final List<AnswerDto> options) {
        if (Objects.isNull(quizType) || options.isEmpty()) return options;
        switch (quizType) {
            // The right answer against one wrong option, drawn from the rest.
            case "ONE_FROM_TWO" -> {
                if (options.size() <= 2) return options;
                AnswerDto wrong = options.get(1 + ThreadLocalRandom.current().nextInt(options.size() - 1));
                return new ArrayList<>(List.of(options.get(0), wrong));
            }
            // Typed: there is nothing to pick from, and the options would only give the answer away.
            case "INPUT" -> {
                return new ArrayList<>();
            }
            // Scaled by the options' values; one that is not a number has no place on the scale.
            case "VALUES_RANGE" -> {
                options.removeIf(option -> Objects.isNull(Numbers.parse(option.getContent())));
                return options;
            }
            // The things themselves (the glossary keys) are put in order of their values, which
            // would give the order away, so the values are not sent.
            case "IN_ORDER" -> {
                options.removeIf(option -> Objects.isNull(Numbers.parse(option.getContent())));
                options.forEach(option -> option.setContent(option.getGlossaryKey()));
                return options;
            }
            // Matching pairs: the question's own terms only (filled-in wrong options have no id),
            // each sent with its key and another one's value, so the pairs are not in what is sent.
            case "DRAG_AND_DROP" -> {
                options.removeIf(option -> Objects.isNull(option.getId()));
                List<String> values = new ArrayList<>(options.stream().map(AnswerDto::getContent).toList());
                Collections.shuffle(values);
                for (int i = 0; i < options.size(); i++) {
                    options.get(i).setContent(values.get(i));
                    options.get(i).setId(null);
                }
                return options;
            }
            default -> {
                return options;
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionDto> getMiniGameQuestion() {
        return questionRepository.findRandomMiniGameQuestionId().map(this::miniGameQuestion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionDto> getMiniGameQuestions(final int count) {
        return questionRepository.findRandomMiniGameQuestionIds(count).stream().map(this::miniGameQuestion).toList();
    }

    private QuestionDto miniGameQuestion(final Long questionId) {
        QuestionDto question = getQuestionWithAnswerOptions(questionId);
        // Glossary options are told apart by termId; only the right one would carry an
        // answer id, which would give it away.
        question.getAnswers().forEach(answer -> {
            if (answer.getTermId() != null) answer.setId(null);
        });
        return question;
    }

    @Override
    @Transactional(readOnly = true)
    public MiniGameResult checkMiniGameAnswer(Long questionId, AnswerDto choice) {
        Answer answer = questionRepository.findById(questionId)
                .flatMap(question -> question.getAnswers().stream().findFirst())
                .orElseThrow(() -> new RecordNotFoundException("Question not found: " + questionId));
        Long termId = answer.getGlossary() == null ? null : answer.getGlossary().getTermId();
        boolean correct = termId != null
                ? termId.equals(choice.getTermId())
                : answer.getAnsId().equals(choice.getId());
        return new MiniGameResult(correct, termId == null ? answer.getAnsId() : null, termId, answer.getContent());
    }

    private void handleAnswerCountDiscrepancy(final Question question) {
        Property optionsCountProperty = propertyRepository.findByName(EXPRESS_QUIZ_DEFAULT_OPTIONS_COUNT);
        int optionsCount = optionsCountProperty.getIntValue();
        List<Answer> answers = question.getAnswers();

        if (answers.size() < optionsCount) {
            boolean answerByKey = question.getAttributes().contains(QuestionAttribute.ANSWER_BY_KEY);
            List<Answer> wrongAnswerOptions = fillAnswerOptions(answers, question, optionsCount, answerByKey);
            answers.addAll(wrongAnswerOptions);
        }
    }

    /**
     * Wrong options for a question that does not carry enough of its own, drawn from the glossary
     * type the right answer belongs to — and only from there.
     *
     * <p>A wrong option has to be the same kind of thing as the right one. Anything else gives the
     * answer away: "What is the capital of France?" against a population and two currencies is not
     * a question, whatever it looks like. This used to fall back to any answer in the category,
     * which is exactly how that happened.
     *
     * <p>So an answer with no glossary type gets no options invented for it. The question goes out
     * short, which an editor can see and fix by giving the glossary a type; the alternative is a
     * question that looks fine and is worth nothing.
     */
    private List<Answer> fillAnswerOptions(final List<Answer> answers, final Question question, int optionsCount, boolean answerByKey) {
        GlossaryType answerGlossaryType = getGlossaryTypeFor(answers);

        if (Objects.isNull(answerGlossaryType)) {
            log.warn("Question {} is short of options and its answer has no glossary type to draw them from",
                    question.getQuestionId());
            return Collections.emptyList();
        }

        List<Long> termIdList = answers.stream().map(Answer::getGlossary).filter(Objects::nonNull).map(Glossary::getTermId).toList();
        return answerService.getWrongOptionsByGlossaryTypeWithLimit(
                answerGlossaryType, termIdList, optionsCount - answers.size(), answerByKey);
    }

    private static GlossaryType getGlossaryTypeFor(final List<Answer> answers) {
        return answers.stream()
                .findFirst()
                .map(Answer::getGlossary)
                .map(Glossary::getType)
                .orElse(null);
    }
}
