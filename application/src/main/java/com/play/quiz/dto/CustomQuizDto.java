package com.play.quiz.dto;

import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * What a player fills in on "Create a quiz": the questions they wrote, the settings those
 * questions share, and who to invite. The bounds match the form's own, so a hand-made request
 * cannot bring a thousand questions or a one-second timer.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomQuizDto {

    /** A Q_QUIZ_TYPE id: how every question in the quiz is played. */
    @NotNull
    private Long quizTypeId;

    @Valid
    @NotEmpty
    @Size(max = 50)
    private List<CustomQuestionDto> questions;

    @Min(5)
    @Max(300)
    private int timePerQuestion;

    @NotEmpty
    private Set<Long> categoryIds;

    private Set<Long> invitedUserIds;

    /**
     * One question as the player wrote it: its right answers and its wrong options. How many of
     * each it needs depends on the quiz type (CustomQuizServiceImpl.CUSTOM_QUIZ_TYPES).
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomQuestionDto {

        @NotBlank
        @Size(max = 1000)
        private String content;

        @NotEmpty
        @Size(max = 10)
        private List<@NotBlank @Size(max = 1000) String> answers;

        @Size(max = 10)
        private List<@NotBlank @Size(max = 1000) String> wrongAnswers;
    }
}
