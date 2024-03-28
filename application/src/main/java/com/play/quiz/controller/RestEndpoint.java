package com.play.quiz.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestEndpoint {

    public static final String CONTEXT_PATH = "/api";

    public static final String WS_BROKER_SOLO = "/solo";
    public static final String WS_BROKER_PARTY = "/party";

    public static final String REQUEST_MAPPING_AUTH = "/auth";
    public static final String REQUEST_MAPPING_USER = "/user";
    public static final String REQUEST_MAPPING_QUIZ = "/quiz";
    public static final String REQUEST_MAPPING_TRANSLATION = "/translations";
    public static final String REQUEST_MAPPING_MESSAGE = "/message";
    public static final String REQUEST_MAPPING_GLOSSARY = "/glossary";
    public static final String REQUEST_MAPPING_CATEGORY = "/category";
    public static final String REQUEST_MAPPING_QUESTION = "/question";
    public static final String REQUEST_MAPPING_LANGUAGE = "/language";
    public static final String REQUEST_MAPPING_USER_HISTORY = "/user-history";
    public static final String REQUEST_MAPPING_KNOWLEDGE_BASE = "/knowledge-base";
}
