package com.play.quiz.controller;

import com.play.quiz.domain.Language;
import com.play.quiz.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_LANGUAGE;

@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_LANGUAGE)
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping(value = "/languages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Language>> getLanguages() {
        return ResponseEntity.ok(languageService.findAll());
    }
}
