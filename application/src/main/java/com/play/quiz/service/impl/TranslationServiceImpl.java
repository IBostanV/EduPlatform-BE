package com.play.quiz.service.impl;

import com.play.quiz.domain.Translation;
import com.play.quiz.repository.translation.TranslationRepository;
import com.play.quiz.service.TranslationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRepository translationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> translate(String langCode) {
        // The code names a column (EN, RU, ...) and goes into the SQL as text, so nothing but a
        // two-letter code may get that far: the URL it comes from is open to anyone.
        if (langCode == null || !langCode.matches("[A-Za-z]{2}")) {
            throw new IllegalArgumentException("Unknown language code");
        }
        String queryString = "SELECT trsl.*, trsl.%s as value FROM Q_TRANSLATION trsl ".formatted(langCode);

        Query nativeQuery = entityManager.createNativeQuery(queryString, Translation.class);
        List<Translation> resultList = nativeQuery.getResultList();

        return resultList.stream()
                .collect(Collectors.toMap(Translation::getKey, Translation::getValue));
    }
}
