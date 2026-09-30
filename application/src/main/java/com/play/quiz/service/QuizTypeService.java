package com.play.quiz.service;

import com.play.quiz.domain.QuizType;

import java.util.List;

public interface QuizTypeService {

    List<QuizType> includesType(Long typeBits);

    Integer toTypeBits(List<QuizType> quizTypes);

    Integer includeOnlyInputType();
}
