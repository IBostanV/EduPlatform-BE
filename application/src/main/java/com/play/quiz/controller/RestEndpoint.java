package com.play.quiz.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestEndpoint {

    public static final String CONTEXT_PATH = "/api";

    public static final String WS_BROKER_SOLO = "/solo";
    public static final String WS_BROKER_PARTY = "/party";
    // Live matches: every change to a room, pushed to each player in it.
    public static final String WS_BROKER_LIVE = "/live";

    public static final String REQUEST_MAPPING_AUTH = "/auth";
    public static final String REQUEST_MAPPING_USER = "/user";
    public static final String REQUEST_MAPPING_QUIZ = "/quiz";
    public static final String REQUEST_MAPPING_TRANSLATION = "/translations";
    public static final String REQUEST_MAPPING_MESSAGE = "/message";
    public static final String REQUEST_MAPPING_GLOSSARY = "/glossary";
    public static final String REQUEST_MAPPING_CATEGORY = "/category";
    public static final String REQUEST_MAPPING_BACKGROUND = "/backgrounds";
    public static final String REQUEST_MAPPING_QUESTION = "/question";
    public static final String REQUEST_MAPPING_LANGUAGE = "/language";
    public static final String REQUEST_MAPPING_USER_HISTORY = "/user-history";
    public static final String REQUEST_MAPPING_KNOWLEDGE_BASE = "/knowledge-base";
    public static final String REQUEST_MAPPING_FEEDBACK = "/feedback";
    public static final String REQUEST_MAPPING_CLIENT_ERROR = "/client-error";
    public static final String REQUEST_MAPPING_DONATION = "/donation";
    public static final String REQUEST_MAPPING_CONQUEST = "/conquest";
    public static final String REQUEST_MAPPING_DAILY_TASK = "/daily-task";
    public static final String REQUEST_MAPPING_COIN = "/coin";
    public static final String REQUEST_MAPPING_IQ = "/iq";
    public static final String REQUEST_MAPPING_TROPHY = "/trophy";
    public static final String REQUEST_MAPPING_STATISTICS = "/statistics";
    public static final String REQUEST_MAPPING_FEED = "/feed";
    public static final String REQUEST_MAPPING_ANNOUNCEMENT = "/announcement";
    public static final String REQUEST_MAPPING_SOCIAL = "/social";
    public static final String REQUEST_MAPPING_LEADERBOARD = "/leaderboard";
    public static final String REQUEST_MAPPING_LIVE = "/live";
    public static final String REQUEST_MAPPING_GROUPS = "/groups";
    public static final String REQUEST_MAPPING_DUELS = "/duels";
    public static final String REQUEST_MAPPING_REVIEW = "/review";
    public static final String REQUEST_MAPPING_COSMETICS = "/cosmetics";
    public static final String REQUEST_MAPPING_SEASON = "/season";

    public static final String QUIZ_TYPES = "/types";
    public static final String QUIZ_CUSTOM = "/custom";

    // Under /user, so one rule in WebSecurity keeps account management to admins.
    public static final String USER_ADMIN = "/admin";
}
