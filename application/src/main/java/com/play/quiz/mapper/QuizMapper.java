package com.play.quiz.mapper;

import java.util.List;

import com.play.quiz.domain.Answer;
import com.play.quiz.domain.Question;
import com.play.quiz.domain.Quiz;
import com.play.quiz.domain.translation.QuestionTranslation;
import com.play.quiz.dto.AnswerDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.dto.translation.QuestionTranslationDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface QuizMapper {

    // `custom` is the server's to set (CustomQuizServiceImpl.create), never the client's.
    @Mapping(target = "custom", ignore = true)
    Quiz toEntity(final QuizDto quizDto);

    @IterableMapping(qualifiedByName = "handlingQuestions")
    List<QuestionDto> toQuestionList(final List<Question> questionList);

    @Named("handlingQuestions")
    @Mapping(source = "questionId", target = "id")
    @Mapping(source = "category.catId", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    QuestionDto withQuestions(final Question question);

    QuestionTranslationDto withQuestionTranslationDto(final QuestionTranslation questionTranslation);

    @Mapping(target = "quizType", source = "type")
    @Mapping(target = "category.parentId", source = "category.parent.catId")
    @Mapping(target = "category.parentName", source = "category.parent.name")
    QuizDto toDto(final Quiz quiz);

    @IterableMapping(qualifiedByName = "handlingAnswers")
    List<AnswerDto> toAnswerDtoList(final List<Answer> asnwerList);

    @Named("handlingAnswers")
    @Mapping(target = "answerTranslations", ignore = true)
    @Mapping(target = "termId", source = "glossary.termId")
    @Mapping(target = "glossaryAttachment", source = "glossary.attachment")
    @Mapping(target = "glossaryKey", source = "glossary.key")
    @Mapping(target = "glossaryOptions", source = "glossary.options")
    @Mapping(target = "mapLevel", source = "glossary.type.options")
    AnswerDto toAnswerDto(final Answer answer);
}
