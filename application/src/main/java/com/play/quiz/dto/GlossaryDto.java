package com.play.quiz.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.play.quiz.domain.GlossaryType;
import com.play.quiz.dto.translation.GlossaryTranslationDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlossaryDto {
    private Long termId;
    private String value;
    private Long parentId;
    private String options;
    private Boolean isActive;
    private String parentKey;
    private byte[] attachment;
    private GlossaryType type;
    private String parentValue;
    private String categoryName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    private List<GlossaryTranslationDto> glossaryTranslations;

    @NotBlank
    private String key;
    @NotNull
    private Long categoryId;
}
