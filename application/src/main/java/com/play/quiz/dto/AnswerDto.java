package com.play.quiz.dto;

import java.util.List;

import com.play.quiz.dto.translation.AnswerTranslationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
    private Long id;
    private Long termId;
    private String content;
    private byte[] glossaryAttachment;
    private List<AnswerTranslationDto> answerTranslations;

    // For map questions, from the answer's glossary term: its key (ISO country code, continent
    // code or city name), its options (a city's "lat,lng") and its type's options ("map:country",
    // "map:continent" or "map:city"), which say how the map draws it. Null for other answers.
    private String glossaryKey;
    private String glossaryOptions;
    private String mapLevel;
}
