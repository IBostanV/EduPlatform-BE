package com.play.quiz.record;

import java.time.LocalDateTime;

// One custom quiz the signed-in player was invited to, as their invitations page lists it. The
// inviter is shown as other users see them (UserSummary: never an email).
public record QuizInvitation(Long quizId,
                             String quizType,
                             int questionsCount,
                             Integer questionTime,
                             UserSummary invitedBy,
                             LocalDateTime invitedAt,
                             boolean played) {
}
