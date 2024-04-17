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
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.GlossaryService;
import com.play.quiz.service.QuestionService;
import com.play.quiz.service.UserQuizHistoryService;
import com.play.quiz.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserQuizHistoryServiceImpl implements UserQuizHistoryService {

    private final UserService userService;
    private final QuestionService questionService;
    private final GlossaryService glossaryService;
    private final UserQuizHistoryMapper userQuizHistoryMapper;
    private final AuthenticationFacade authenticationFacade;
    private final UserQuizHistoryRepository userQuizHistoryRepository;

    @Override
    public UserQuizHistoryDto save(final UserQuizHistoryDto userQuizHistoryDto) {
        UserQuizHistory userQuizHistory = userQuizHistoryMapper.toEntity(userQuizHistoryDto);
        return userQuizHistoryMapper.toDto(saveUserHistory(userQuizHistory));
    }

    private UserQuizHistory saveUserHistory(final UserQuizHistory userQuizHistory) {
        String username = authenticationFacade.getPrincipal().getUsername();

        return userQuizHistoryRepository.save(userQuizHistory.toBuilder()
                .completedDate(LocalDateTime.now())
                .account(userService.findByEmail(username))
                .build());
    }

    @NonNull
    @Override
    @Transactional
    public UserQuizHistoryDto getById(final Long historyId) {
        log.debug("Get UserHistory with historyId: {}", historyId);
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
