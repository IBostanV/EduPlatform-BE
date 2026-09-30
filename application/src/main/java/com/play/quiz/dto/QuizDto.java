package com.play.quiz.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.play.quiz.domain.QuizType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
    private Long quizId;
    private Integer quizTime;
    private Integer questionTime;
    // Q_QUIZ.IS_CUSTOM: built by a player from questions they wrote. Read-only: the server sets it,
    // and the mappers ignore it on the way back in, so a client cannot claim a quiz is custom.
    private boolean custom;
    private QuizType quizType;
    private Set<Long> questionIds;
    private LocalDateTime updatedDate;
    private LocalDateTime createdDate;
    private List<QuestionDto> questionList;

    @PositiveOrZero
    private int questionsCount;
    @NotNull
    private CategoryDto category;
}
