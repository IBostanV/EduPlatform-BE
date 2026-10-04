package com.play.quiz.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// An error the browser hit: what kind (uncaught, unhandled rejection, network), its message and
// stack, and the page it happened on. The frontend trims each to these sizes before sending.
public record ClientErrorInput(@NotBlank @Size(max = 50) String kind,
                               @NotBlank @Size(max = 2000) String message,
                               @Size(max = 8000) String stack,
                               @Size(max = 500) String page,
                               @Size(max = 500) String userAgent) {
}
