package com.play.quiz.service.impl;

import java.util.List;

import com.play.quiz.domain.Answer;
import com.play.quiz.domain.GlossaryType;
import com.play.quiz.repository.GlossaryRepository;
import com.play.quiz.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final GlossaryRepository glossaryRepository;

    @Override
    public List<Answer> getWrongOptionsByGlossaryTypeWithLimit(final GlossaryType glossaryType, final List<Long> termIdList,
                                                              int amount, boolean answerByKey) {
        Pageable pageable = PageRequest.of(0, amount);
        return glossaryRepository.findWrongOptions(glossaryType, termIdList, pageable).stream()
                .<Answer>map(glossary -> Answer.builder()
                        .glossary(glossary)
                        .content(answerByKey ? glossary.getKey() : glossary.getValue())
                        .build())
                .toList();
    }
}
