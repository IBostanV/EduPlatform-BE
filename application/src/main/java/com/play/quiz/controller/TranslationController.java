package com.play.quiz.controller;

import com.play.quiz.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_TRANSLATION;

@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_TRANSLATION)
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    @GetMapping(value = "/{langCode}")
    public ResponseEntity<Map<String, String>> test(@PathVariable(value = "langCode") String langCode) {
        return ResponseEntity.ok(translationService.translate(langCode));
    }
}
