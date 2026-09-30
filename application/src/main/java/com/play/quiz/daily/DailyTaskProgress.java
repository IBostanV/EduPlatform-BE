package com.play.quiz.daily;

/**
 * One of today's tasks as it stands for the player asking.
 *
 * <p>{@code awarded} is true only on the read that paid it, which is what the page needs to say
 * so the experience shown in the navbar catches up.
 */
public record DailyTaskProgress(String code,
                                int progress,
                                int target,
                                int experience,
                                boolean completed,
                                boolean awarded) {
}
