package com.play.quiz.service.impl;

import com.play.quiz.domain.QuizType;
import com.play.quiz.repository.QuizTypeRepository;
import com.play.quiz.service.QuizTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QuizTypeServiceImpl implements QuizTypeService {

    private final QuizTypeRepository quizTypeRepository;

    public List<QuizType> includesType(Long typeBits) {
        if (Objects.isNull(typeBits)) {
            return List.of();
        }

        return quizTypeRepository.findAll()
                .stream()
                .filter(quizType -> (quizType.getBitValue() & typeBits) != 0)
                .toList();
    }

    @Override
    public Integer toTypeBits(List<QuizType> quizTypes) {
        if (Objects.isNull(quizTypes)) {
            return 0;
        }

        return quizTypes.stream()
                .map(QuizType::getBitValue)
                .reduce((a, b) -> a | b)
                .orElse(0);
    }

    @Override
    public Integer includeOnlyInputType() {
        return quizTypeRepository.findAll()
                .stream()
                .map(QuizType::getBitValue)
                .filter(value -> value != 8)
                .reduce((first, second) -> first | second)
                .orElse(0);
    }
}
