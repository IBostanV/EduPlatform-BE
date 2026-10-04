package com.play.quiz.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.play.quiz.dto.AnswerDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.domain.Category;
import com.play.quiz.domain.Question;
import com.play.quiz.record.MiniGameResult;
import com.play.quiz.dto.UserQuizParams;
import com.play.quiz.record.PageResponse;

public interface QuestionService {

    QuestionDto save(final QuestionDto questionDto);

    List<QuestionDto> findAll();

    PageResponse<QuestionDto> findPage(int page, int size, String query, String sort, String direction);

    QuestionDto update(final Long questionId, final QuestionDto changes);

    void delete(final Long questionId);

    List<QuestionDto> findByCategory(final Category category);

    List<QuestionDto> generateFromTemplate(final QuestionDto questionDto);

    void deactivate(final Long questionId);

    /** Random questions close to this player's occupations; none if they have none or turned it off. */
    List<Question> getOccupationQuestions(String email, int count);

    List<Question> getGeneralKnowledgeQuestions(int questionCount);

    Question getById(final Long questionId);

    List<Question> getByIds(final Set<Long> idList);

    List<AnswerDto> getAnswers(final Long questionId);

    QuestionDto getQuestionWithAnswerOptions(final Long questionId);

    /** The question with its options shaped the way a quiz type (Q_QUIZ_TYPE.NAME, null for none) plays them. */
    QuestionDto getQuestionWithAnswerOptions(final Long questionId, final String quizType);

    Optional<QuestionDto> getMiniGameQuestion();

    MiniGameResult checkMiniGameAnswer(Long questionId, AnswerDto choice);

    /** Several mini-game questions, all different, shown the way the mini game shows one. */
    List<QuestionDto> getMiniGameQuestions(int count);

    List<Question> getByCategoryIdAndParams(Long catId, int questionCount, UserQuizParams userQuizParams);
}
