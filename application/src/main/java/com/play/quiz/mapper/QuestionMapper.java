package com.play.quiz.mapper;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Answer;
import com.play.quiz.domain.Glossary;
import com.play.quiz.domain.Question;
import com.play.quiz.domain.QuizType;
import com.play.quiz.domain.translation.QuestionTranslation;
import com.play.quiz.dto.AnswerDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.dto.translation.QuestionTranslationDto;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.QuizTypeService;
import com.play.quiz.service.UserService;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring",
        imports = LocalDateTime.class)
public abstract class QuestionMapper {

    @Autowired private AuthenticationFacade authenticationFacade;
    @Autowired private UserService userService;
    @Autowired private QuizTypeService quizTypeService;

    @Mapping(target = "id", source = "questionId")
    @Mapping(target = "categoryId", source = "category.catId")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "excludeQuizTypes", source = "excludeType", qualifiedByName = "excludeTypeHandler")
    public abstract QuestionDto mapToDto(final Question question);

    public List<QuestionDto> mapToDtoList(final List<Question> questionList) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            return mapToDtoListInternal(questionList);
        }
        return mapDtoNoAnswerList(questionList);
    }

    protected abstract List<QuestionDto> mapToDtoListInternal(final List<Question> questionList);

    @IterableMapping(qualifiedByName = "withoutAnswers")
    public abstract List<QuestionDto> mapDtoNoAnswerList(final List<Question> questionList);

    @Mapping(target = "questionId", source = "id")
    @Mapping(target = "category.catId", source = "categoryId")
    @Mapping(target = "category.name", source = "categoryName")
    @Mapping(target = "account", expression = "java(getAccount())")
    @Mapping(target = "createdDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "excludeType", source = "questionDto", qualifiedByName = "excludeTypeHandler")
    public abstract Question mapToEntity(final QuestionDto questionDto);

    @Mapping(target = "glossary", source = "termId", qualifiedByName = "answerGlossaryHandler")
    @Mapping(target = "createdDate", expression = "java(LocalDateTime.now())")
    protected abstract Answer answerDtoToAnswer(AnswerDto answerDto);

    @Named("withoutAnswers")
    @Mapping(target = "id", source = "questionId")
    @Mapping(target = "answers", ignore = true)
    protected abstract QuestionDto withoutAnswers(Question question);

    protected abstract QuestionTranslationDto mapTranslationToDto(QuestionTranslation source);

    @Mapping(target = "id", source = "ansId")
    @Mapping(target = "termId", source = "glossary.termId")
    @Mapping(target = "glossaryAttachment", source = "glossary.attachment")
    @Mapping(target = "glossaryKey", source = "glossary.key")
    @Mapping(target = "glossaryOptions", source = "glossary.options")
    @Mapping(target = "mapLevel", source = "glossary.type.options")
    protected abstract AnswerDto mapAnswersToDto(Answer source);

    protected Account getAccount() {
        String emailAsUsername = authenticationFacade.getPrincipal().getUsername();
        return userService.findByEmail(emailAsUsername);
    }

    public abstract List<Question> mapToEntityList(final List<QuestionDto> quizQuestions);

    @Named("excludeTypeHandler")
    protected List<QuizType> handleExcludeType(Long quizTypeBit) {
        return quizTypeService.includesType(quizTypeBit);
    }

    @Named("excludeTypeHandler")
    protected Integer handleExcludeType(final QuestionDto entity) {
        // A typed answer is text with no glossary term; a picked term's content is filled in on save.
        boolean answerByInput = Objects.requireNonNullElse(entity.getAnswers(), List.<AnswerDto>of()).stream()
                .anyMatch(answer -> Objects.isNull(answer.getTermId())
                        && Objects.nonNull(answer.getContent()) && !answer.getContent().isBlank());

        if (answerByInput) {
            return quizTypeService.includeOnlyInputType();
        }

        return quizTypeService.toTypeBits(entity.getExcludeQuizTypes());
    }

    @Named("answerGlossaryHandler")
    protected Glossary answerGlossaryHandler(Long termId) {
        if (Objects.nonNull(termId)) {
            return Glossary.builder()
                    .termId(termId)
                    .build();
        }

        return null;
    }
}
