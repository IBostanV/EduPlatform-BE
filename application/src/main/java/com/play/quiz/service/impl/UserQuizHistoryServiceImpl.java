package com.play.quiz.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.play.quiz.domain.Question;
import com.play.quiz.domain.Quiz;
import com.play.quiz.domain.UserQuizHistory;
import com.play.quiz.dto.AnswerDto;
import com.play.quiz.dto.GlossaryDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.dto.UserQuizHistoryDto;
import com.play.quiz.dto.wrapper.HistoryAnswer;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.mapper.UserQuizHistoryMapper;
import com.play.quiz.record.PageResponse;
import com.play.quiz.record.QuizHistoryEntry;
import com.play.quiz.record.QuizStatistics;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.repository.QuizRepository;
import com.play.quiz.repository.QuizTypeRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.CustomQuizService;
import com.play.quiz.service.GlossaryService;
import com.play.quiz.service.QuestionService;
import com.play.quiz.service.UserQuizHistoryService;
import com.play.quiz.service.UserService;
import com.play.quiz.util.ExperiencePayout;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserQuizHistoryServiceImpl implements UserQuizHistoryService {

    // Q_QUIZ.TYPE's own database default: SINGLE_CHOICE.
    private static final Long DEFAULT_QUIZ_TYPE_ID = 1L;

    private final UserService userService;
    private final QuestionService questionService;
    private final GlossaryService glossaryService;
    private final UserQuizHistoryMapper userQuizHistoryMapper;
    private final AuthenticationFacade authenticationFacade;
    private final UserQuizHistoryRepository userQuizHistoryRepository;
    private final QuizRepository quizRepository;
    private final QuizTypeRepository quizTypeRepository;
    private final CustomQuizService customQuizService;

    @Override
    // One persistence context for the load and the save: the quiz would be detached otherwise,
    // and the cascade below refuses a detached quiz just as it refuses a proxy.
    @Transactional
    public UserQuizHistoryDto save(final UserQuizHistoryDto userQuizHistoryDto) {
        UserQuizHistory userQuizHistory = userQuizHistoryMapper.toEntity(userQuizHistoryDto);
        UserQuizHistory saved = saveUserHistory(userQuizHistory);
        // Marked once, here: the score is kept on the row so the profile's history and statistics
        // are a read, and the experience below is paid from the same marking.
        storeScore(saved);
        awardExperience(saved);

        return userQuizHistoryMapper.toDto(saved);
    }

    /**
     * Marks the run just recorded and keeps the score on it. The marking comes from
     * {@link #getById}, the same call the result page makes: marking the answers a second way here
     * would be one set of rules written twice, and the two would drift.
     *
     * <p>Nothing here is worth a player's history, so a run that cannot be marked — a question
     * left with no answers, a glossary deleted since — is logged and left unscored instead of
     * failing the save.
     */
    private void storeScore(final UserQuizHistory history) {
        try {
            List<HistoryAnswer> answers = getById(history.getHistoryId()).getAnswers();
            // A right answer comes back with no right answer to show: there was nothing to correct.
            long right = answers.stream().filter(answer -> Objects.isNull(answer.getRightAnswer())).count();

            history.setRightAnswers((int) right);
            history.setTotalAnswers(answers.size());
            userQuizHistoryRepository.save(history);
        } catch (RuntimeException exception) {
            log.warn("History {} could not be marked: {}", history.getHistoryId(), exception.getMessage());
        }
    }

    /**
     * Pays for the run, from the score {@link #storeScore} just worked out.
     *
     * <p>A quiz pays once. Custom quizzes never pay: their questions and answers are written by
     * players, so a level would be worth whatever the easiest self-made quiz is worth. A run that
     * could not be marked pays nothing, since there is no telling what it was worth.
     */
    private void awardExperience(final UserQuizHistory history) {
        if (Objects.isNull(history.getTotalAnswers())) {
            return;
        }
        if (history.getQuiz().isCustom()) {
            log.debug("History {} is a custom quiz, no experience awarded", history.getHistoryId());
            return;
        }
        if (alreadyPlayed(history)) {
            log.debug("Quiz {} was already played by account {}, no experience awarded",
                    history.getQuiz().getQuizId(), history.getAccount().getAccountId());
            return;
        }

        userService.addExperience(history.getAccount().getAccountId(),
                ExperiencePayout.forRun(history.getRightAnswers(), history.getTotalAnswers()));
    }

    /**
     * Whether this player has a run of this quiz other than the one just recorded, which is what
     * stops a quiz being replayed for the experience.
     *
     * <p>An express quiz is built fresh for every run and saved under its own id, so it is never
     * the same quiz twice and always pays.
     */
    private boolean alreadyPlayed(final UserQuizHistory history) {
        return userQuizHistoryRepository.existsByQuiz_QuizIdAndAccount_AccountIdAndHistoryIdNot(
                history.getQuiz().getQuizId(), history.getAccount().getAccountId(), history.getHistoryId());
    }

    /**
     * Q_QUIZ.TYPE is NOT NULL. A quiz played with no type filter ("All") has none, and Hibernate
     * writes the column even where the database has a default, so the platform's own type stands
     * in for it.
     */
    private Quiz withType(final Quiz quiz) {
        if (Objects.nonNull(quiz.getType())) {
            return quiz;
        }
        return quiz.toBuilder()
                .type(quizTypeRepository.findById(DEFAULT_QUIZ_TYPE_ID).orElse(null))
                .build();
    }

    private UserQuizHistory saveUserHistory(final UserQuizHistory userQuizHistory) {
        String username = authenticationFacade.getPrincipal().getUsername();
        // A quiz played from the database (a custom one) is referenced: cascading PERSIST onto it
        // would fail as a detached entity. One built on the fly (express) is still saved with it.
        Quiz quiz = userQuizHistory.getQuiz();
        // Loaded, not referenced: the cascade would hand persist() an uninitialized proxy.
        Quiz playedQuiz = Objects.isNull(quiz.getQuizId())
                ? quiz
                : quizRepository.findById(quiz.getQuizId())
                        .orElseThrow(() -> new RecordNotFoundException("No quiz with id: " + quiz.getQuizId()));

        return userQuizHistoryRepository.save(userQuizHistory.toBuilder()
                .quiz(withType(playedQuiz))
                .completedDate(LocalDateTime.now())
                .account(userService.findByEmail(username))
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<QuizHistoryEntry> getOwnHistory(final int page, final int size) {
        String username = authenticationFacade.getPrincipal().getUsername();
        Page<UserQuizHistory> runs = userQuizHistoryRepository.findOwnHistory(username,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "completedDate")));

        return PageResponse.of(runs, runs.getContent().stream().map(QuizHistoryEntry::of).toList());
    }

    @Override
    @Transactional(readOnly = true)
    public QuizStatistics getOwnStatistics() {
        return QuizStatistics.of(
                userQuizHistoryRepository.sumOwnHistory(authenticationFacade.getPrincipal().getUsername()));
    }

    @NonNull
    @Override
    @Transactional
    public UserQuizHistoryDto getById(final Long historyId) {
        log.debug("Get UserHistory with historyId: {}", historyId);
        // A custom quiz's questions live in their own tables and are judged there.
        UserQuizHistory history = userQuizHistoryRepository.getReferenceById(historyId);
        if (history.getQuiz().isCustom()) {
            UserQuizHistoryDto customHistory = userQuizHistoryMapper.toDto(history);
            customHistory.getAnswers().addAll(customQuizService.score(history.getQuiz(),
                    JsonParser.parseString(history.getAnswersJson()).getAsJsonArray()));
            return customHistory;
        }

        UserQuizHistoryDto historyDto = buildUserHistory(historyId);
        JsonArray jsonUserAnswers = JsonParser.parseString(historyDto.getAnswersJson()).getAsJsonArray();

        historyDto.getQuiz().getQuestionList().forEach(question ->
                historyDto.getAnswers().addAll(createUserAnswersFromJson(jsonUserAnswers, question)));

        return historyDto;
    }

    private UserQuizHistoryDto buildUserHistory(final Long historyId) {
        UserQuizHistory userQuizHistory = userQuizHistoryRepository.getReferenceById(historyId);
        List<Question> questionList = questionService.getByIds(userQuizHistory.getQuiz().getQuestionIds());
        Quiz quiz = userQuizHistory.getQuiz().toBuilder().questionList(questionList).build();

        return userQuizHistoryMapper.toDto(userQuizHistory.toBuilder().quiz(quiz).build());
    }

    private List<HistoryAnswer> createUserAnswersFromJson(final JsonArray userAnswers, final QuestionDto question) {
        if (!userAnswers.isEmpty()) {
            List<HistoryAnswer> singleAnswerAsList = getAnswers(userAnswers, question);
            if (!singleAnswerAsList.isEmpty()) return singleAnswerAsList;
        }

        return Collections.singletonList(new HistoryAnswer(
                0, question.getContent(), null, getFirstAnswer(question).getContent()));
    }

    private List<HistoryAnswer> getAnswers(JsonArray userAnswers, QuestionDto question) {
        for (JsonElement userAnswer : userAnswers) {
            final Set<String> questionIds = userAnswer.getAsJsonObject().keySet();
            final Function<Map.Entry<String, JsonElement>, HistoryAnswer> createAnswer =
                    keyValue -> createHistoryAnswer(keyValue.getValue().getAsJsonObject(), question);

            if (questionIds.contains(question.getId().toString())) {
                log.info("User answered the question: {}", question.getId());
                return userAnswer.getAsJsonObject().entrySet()
                        .stream().map(createAnswer).toList();
            }
        }

        log.info("User did not answer the question.");
        return Collections.emptyList();
    }

    private HistoryAnswer createHistoryAnswer(final JsonObject keyValue, final QuestionDto question) {
        AnswerDto answer = getFirstAnswer(question);
        Long glossaryId = getJsonElementValue(keyValue, "answer").getAsLong();
        double time = getJsonElementValue(keyValue, "time").getAsDouble();

        if (Objects.equals(answer.getTermId(), glossaryId)) {
            log.debug("User answered right. Glossary id: {}", glossaryId);
            return new HistoryAnswer(time, question.getContent(), answer.getContent(), null);
        }

        log.debug("User answered wrong. Get user answer by glossary id: {}", glossaryId);
        GlossaryDto glossaryDto = glossaryService.getById(glossaryId);
        return new HistoryAnswer(time, question.getContent(), glossaryDto.getValue(), answer.getContent());
    }

    private static JsonElement getJsonElementValue(final JsonObject keyValue, String key) {
        return Objects.requireNonNull(keyValue.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getKey(), key))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null));
    }

    private static AnswerDto getFirstAnswer(final QuestionDto question) {
        return question.getAnswers().stream().findFirst()
                .orElseThrow(() -> new RecordNotFoundException("No answers found for question: " + question.getId()));
    }
}
