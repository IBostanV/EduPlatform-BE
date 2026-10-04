package com.play.quiz.season;

import lombok.Getter;

/**
 * The week's quests: bigger goals than the day's tasks, paying season points. Progress is read off
 * what the player did this week (SeasonService), never counted up, as with the daily tasks.
 */
@Getter
public enum WeeklyQuest {

    PLAY_QUIZZES(15, 150),
    RIGHT_ANSWERS(120, 150),
    DAILY_PUZZLES(4, 150),
    DUEL_ROUNDS(5, 150);

    private final int target;
    private final int points;

    WeeklyQuest(final int target, final int points) {
        this.target = target;
        this.points = points;
    }

    public String claimCode() {
        return "QUEST:" + name();
    }
}
