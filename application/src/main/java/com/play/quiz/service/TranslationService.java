package com.play.quiz.service;

import java.util.Map;

public interface TranslationService {

    Map<String, String> translate(String langCode);
}
