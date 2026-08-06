package com.play.quiz.mapper;

import java.util.List;

import com.play.quiz.domain.Answer;
import com.play.quiz.dto.AnswerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AnswerMapper {

    @Mapping(target = "ansId", source = "id")
    public abstract Answer toEntity(final AnswerDto answerDto);

    @Mapping(target = "id", source = "ansId")
    @Mapping(target = "termId", source = "glossary.termId")
    @Mapping(target = "glossaryAttachment", source = "glossary.attachment")
    @Mapping(target = "glossaryKey", source = "glossary.key")
    @Mapping(target = "glossaryOptions", source = "glossary.options")
    @Mapping(target = "mapLevel", source = "glossary.type.options")
    public abstract AnswerDto toDto(final Answer answer);

    public abstract List<AnswerDto> toDtoList(final List<Answer> answerList);

    public abstract List<Answer> toEntityList(final List<AnswerDto> answerList);
}
