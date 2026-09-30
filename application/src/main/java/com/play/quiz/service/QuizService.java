package com.play.quiz.service;

import com.play.quiz.domain.QuizType;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.dto.UserQuizParams;

import java.util.List;

public interface QuizService {

    QuizDto create(final QuizDto quizDto);

    QuizDto getById(final Long quizId);

    /** Up to half from the player's occupations when signed in (email), the rest general knowledge. */
    QuizDto getExpressQuiz(String email);

    QuizDto getQuizByCategoryAndParams(Long catId, UserQuizParams userQuizParams);

    List<QuizType> getQuizTypes();

    /** A stored quiz played again: the same questions, under the same quiz id, so every run of it
     *  can be compared with the others. What a challenge and the daily challenge are made of. */
    QuizDto replay(final Long quizId);

    /** Stores a quiz of general-knowledge questions nobody has played yet, for replays; its id. */
    Long storeGeneralKnowledgeQuiz(int questionCount);
}
