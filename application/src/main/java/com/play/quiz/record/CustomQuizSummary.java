package com.play.quiz.record;

import java.time.LocalDateTime;

// A custom quiz as a list shows it: the player's own "My quizzes" and the admins' list of them all.
// The maker is shown as other users see them (UserSummary: never an email).
public record CustomQuizSummary(Long quizId,
                                String quizType,
                                int questionsCount,
                                Integer questionTime,
                                LocalDateTime createdAt,
                                UserSummary createdBy,
                                long invited,
                                long played) {
}
