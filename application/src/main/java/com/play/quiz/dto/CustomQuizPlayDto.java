package com.play.quiz.dto;

import java.util.List;

/**
 * A custom quiz as its player receives it: the quiz (sent back when the run is saved) and its
 * questions in order, each with its options shuffled and nothing marking the right ones. A
 * typed-answer question carries no options at all, since they would be the answers.
 */
public record CustomQuizPlayDto(QuizDto quiz, List<Question> questions) {

    // `answers`, like QuestionDto's, so the same option components render them.
    public record Question(Long id, String content, List<Option> answers) {
    }

    public record Option(Long id, String content) {
    }
}
