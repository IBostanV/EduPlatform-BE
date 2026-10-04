package com.play.quiz.dto.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryAnswer {
    private double time;
    private String content;
    private String userAnswer;
    private String rightAnswer;
    // Set once marked (UserQuizHistoryServiceImpl.getById): the mistakes deck files the answer
    // under its question, and the result page finds an article for its category. Null for a
    // custom quiz, whose questions are not the site's.
    private Long questionId;
    private Long categoryId;

    public HistoryAnswer(final double time, final String content, final String userAnswer, final String rightAnswer) {
        this(time, content, userAnswer, rightAnswer, null, null);
    }
}
