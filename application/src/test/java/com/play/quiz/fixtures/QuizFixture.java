package com.play.quiz.fixtures;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.play.quiz.dto.QuizDto;
import com.play.quiz.domain.Question;
import com.play.quiz.domain.Quiz;

public class QuizFixture {
    public static QuizDto getQuizNoQuestionDto() {
        return QuizDto.builder()
                .quizId(1L)
                .quizType(QuizTypeFixture.getDefaultQuizType())
                .category(CategoryFixture.getCategoryDto())
                .questionsCount(5)
                .build();
    }

    public static Quiz getQuiz() {
        return Quiz.builder()
                .quizId(1L)
                .category(CategoryFixture.getCategory())
                .questionsCount(5)
                .questionIds(Collections.singleton(1L))
                .questionList(getQuestionList())
                .build();
    }

    private static List<Question> getQuestionList() {
        return LongStream.rangeClosed(1, 5)
                .mapToObj(i -> QuestionFixture.getGeneratedQuestion(i, "Life"))
                .collect(Collectors.toList());
    }
}
