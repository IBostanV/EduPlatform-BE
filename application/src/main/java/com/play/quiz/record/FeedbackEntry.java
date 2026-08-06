package com.play.quiz.record;

import java.time.LocalDateTime;

import com.play.quiz.enums.FeedbackType;

// A message as the admins' Feedback list shows it. A signed-in sender is shown as other users see
// them (UserSummary: never their account email); `from` is null for a guest, whose own contact
// address, if they left one, is in contactEmail.
public record FeedbackEntry(Long id,
                            FeedbackType type,
                            String message,
                            String page,
                            String question,
                            UserSummary from,
                            String contactEmail,
                            LocalDateTime sentAt,
                            boolean resolved,
                            boolean hasScreenshot) {
}
