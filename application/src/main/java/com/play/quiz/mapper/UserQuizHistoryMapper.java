package com.play.quiz.mapper;

import java.util.List;

import com.play.quiz.domain.Answer;
import com.play.quiz.domain.Question;
import com.play.quiz.domain.Quiz;
import com.play.quiz.domain.UserQuizHistory;
import com.play.quiz.dto.AnswerDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.dto.UserQuizHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserQuizHistoryMapper {

    // The account is left out: whoever asked for this run is the one who played it, and mapping it
    // would walk Account.friends (a Set<Account>), which is cyclic between two mutual friends and
    // would recurse until the stack gave out.
    @Mapping(target = "account", ignore = true)
    UserQuizHistoryDto toDto(final UserQuizHistory userQuizHistory);

    UserQuizHistory toEntity(final UserQuizHistoryDto userQuizHistoryDto);

    // The quiz a saved run carries comes from the client: `custom` is the server's to set, so it is
    // not taken from there. `type` is named quizType on the DTO (as in QuizMapper), and without
    // this mapping a played quiz would be stored with no type at all.
    @Mapping(target = "custom", ignore = true)
    @Mapping(target = "type", source = "quizType")
    Quiz quizDtoToQuiz(final QuizDto quizDto);

    default AnswerDto answerToAnswerDto(final Answer answer) {
        return AnswerDto.builder()
                .content(answer.getContent())
                .termId(answer.getGlossary().getTermId())
                .glossaryAttachment(answer.getGlossary().getAttachment())
                .build();
    }

    default List<AnswerDto> answersToAnswersDto(final List<Answer> answerList) {
        return answerList.stream()
                .map(this::answerToAnswerDto)
                .toList();
    }

    default QuestionDto questionToQuestionDto(final Question question) {
        return QuestionDto.builder()
                .type(question.getType())
                .id(question.getQuestionId())
                .content(question.getContent())
                .answers(answersToAnswersDto(question.getAnswers()))
                .build();
    }
}
