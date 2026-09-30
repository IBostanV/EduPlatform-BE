package com.play.quiz.record;

import java.time.LocalDateTime;

// An error as the admins' Errors list shows it. `from` is the player as other users see them
// (UserSummary: never their account email), null for a guest.
public record ClientErrorEntry(Long id,
                               String kind,
                               String message,
                               String stack,
                               String page,
                               String userAgent,
                               UserSummary from,
                               LocalDateTime sentAt) {
}
