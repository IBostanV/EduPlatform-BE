package com.play.quiz.service.impl;

import java.util.List;

import com.play.quiz.domain.Language;
import com.play.quiz.repository.LanguageRepository;
import com.play.quiz.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    @Override
    public List<Language> findAll() {
        return languageRepository.findAll()
                .stream()
                .peek(language -> language.setCreatedDate(null))
                .toList();
    }
}
