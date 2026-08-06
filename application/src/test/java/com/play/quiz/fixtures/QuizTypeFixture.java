package com.play.quiz.fixtures;

import com.play.quiz.domain.QuizType;

public class QuizTypeFixture {

    public static QuizType getDefaultQuizType() {
        return getQuizType(1L, "SINGLE_CHOICE", 1);
    }

    public static QuizType getQuizType(final Long id, final String name, final Integer value) {
        return QuizType.builder()
                .id(id)
                .name(name)
                .bitValue(value)
                .build();
    }
}
