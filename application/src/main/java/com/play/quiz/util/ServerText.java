package com.play.quiz.util;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.play.quiz.service.TranslationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Text the server writes for a player (error messages, trophies, the activation email), in the
 * language of the request — the site sends it as Accept-Language — from the same Q_TRANSLATION
 * rows the site's own text comes from, with the same {{name}} placeholders.
 *
 * <p>Static, so a service throwing an exception needs no extra dependency; until Spring has made
 * the bean (unit tests) everything stays in English.
 */
@Log4j2
@Component
public class ServerText {

    private static TranslationService translations;
    // ponytail: a language's rows are read once and kept until restart; translations only change
    // with a migration, which comes with one. Clear it on a timer if they get edited live.
    private static final Map<String, Map<String, String>> LOADED = new ConcurrentHashMap<>();

    ServerText(final TranslationService translationService) {
        translations = translationService;
    }

    /** {@code english} in the request's language when there is a row for {@code key}; params are name, value pairs. */
    public static String t(final String key, final String english, final Object... params) {
        // Outside a request (a job) there is no context, and the JVM's own locale is not a player's.
        String language = LocaleContextHolder.getLocaleContext() == null ? "EN"
                : LocaleContextHolder.getLocale().getLanguage().toUpperCase(Locale.ROOT);
        String text = english;
        if (translations != null && language.matches("[A-Z]{2}") && !"EN".equals(language)) {
            text = LOADED.computeIfAbsent(language, ServerText::load).getOrDefault(key, english);
        }
        for (int i = 0; i + 1 < params.length; i += 2) {
            text = text.replace("{{" + params[i] + "}}", String.valueOf(params[i + 1]));
        }
        return text;
    }

    // A language with no column (or a row with no text in it) reads as English rather than failing.
    private static Map<String, String> load(final String language) {
        try {
            return translations.translate(language);
        } catch (RuntimeException exception) {
            log.warn("No server text in {}: {}", language, exception.getMessage());
            return Map.of();
        }
    }
}
