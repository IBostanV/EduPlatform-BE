package com.play.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserQuizParams {
    private Long categoryId;
    private Long quizType;
    // The difficulty band the player picked, as Q_QUESTION.COMPLEXITY_LEVEL bounds (1-10). Both
    // null means any difficulty.
    private Integer complexityFrom;
    private Integer complexityTo;
    // How many questions the player asked for; null means the system's default length.
    private Integer questionCount;
}
