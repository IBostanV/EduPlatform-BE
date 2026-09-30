package com.play.quiz.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.play.quiz.domain.QuizType;
import com.play.quiz.dto.translation.QuestionTranslationDto;
import com.play.quiz.enums.QuestionAttribute;
import com.play.quiz.enums.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"answers", "translations"})
public class QuestionDto {
    private Long id;
    private Object tipId;
    private String topic;
    private int priority;
    private Boolean isActive;
    private QuestionType type;
    private int complexityLevel;
    private String categoryName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<AnswerDto> answers;
    private List<QuizType> excludeQuizTypes;

    @NotBlank
    private String content;
    @NotNull
    private Long categoryId;

    @Builder.Default
    private List<QuestionAttribute> attributes = new ArrayList<>();
    @Builder.Default
    private List<QuestionTranslationDto> translations = new ArrayList<>();

    public void setAttributes(List<QuestionAttribute> attributes) {
        this.attributes = (!attributes.isEmpty()) ? attributes : Collections.singletonList(QuestionAttribute.NONE);
    }
}
