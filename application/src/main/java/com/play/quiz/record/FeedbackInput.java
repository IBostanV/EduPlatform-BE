package com.play.quiz.record;

import com.play.quiz.enums.FeedbackType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// What a player or guest sends: the kind of message, the message, the page they were on, the
// question it is about when reported from inside a quiz, and, from a guest, optionally an address
// to reply to. Sized to the Q_FEEDBACK columns.
public record FeedbackInput(@NotNull FeedbackType type,
                            @NotBlank @Size(max = 2000) String message,
                            @Size(max = 500) String page,
                            @Size(max = 4000) String question,
                            @Email @Size(max = 254) String contactEmail) {
}
