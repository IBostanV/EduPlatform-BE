package com.play.quiz.service;

import java.util.List;

import com.play.quiz.domain.Quiz;
import com.play.quiz.dto.CustomQuizDto;
import com.play.quiz.dto.CustomQuizPlayDto;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.dto.wrapper.HistoryAnswer;
import com.play.quiz.record.CustomQuizSummary;
import com.play.quiz.record.QuizInvitation;

/** Quizzes players build from questions they write, kept in Q_CUSTOM_QUESTION / Q_CUSTOM_ANSWER. */
public interface CustomQuizService {

    QuizDto create(final CustomQuizDto customQuizDto);

    CustomQuizPlayDto getForPlay(final Long quizId);

    /** The custom quizzes the signed-in player was invited to, newest first. */
    List<QuizInvitation> getMyInvitations();

    /** The custom quizzes the signed-in player made, newest first. */
    List<CustomQuizSummary> getMyQuizzes();

    /** Every custom quiz, newest first: for the admins, who moderate what players write. */
    List<CustomQuizSummary> getAllCustomQuizzes();

    /**
     * Removes a custom quiz with its questions, its invitations and every run of it. Its creator
     * and the admins may do this.
     */
    void delete(final Long quizId);

    /** A played run's answers (the saved answersJson) judged question by question. */
    List<HistoryAnswer> score(final Quiz quiz, final String answersJson);
}
