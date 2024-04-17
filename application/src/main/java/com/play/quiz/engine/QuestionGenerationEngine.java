package com.play.quiz.engine;

import static com.play.quiz.enums.QuestionAttribute.ANSWER_BY_KEY;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.play.quiz.domain.Answer;
import com.play.quiz.domain.Glossary;
import com.play.quiz.domain.Question;
import com.play.quiz.enums.QuestionType;
import com.play.quiz.repository.GlossaryRepository;
import com.play.quiz.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@RequiredArgsConstructor
public class QuestionGenerationEngine {

    private final QuestionRepository questionRepository;
    private final GlossaryRepository glossaryRepository;

    @Transactional
    public List<Question> generateByAddedGlossary(final Glossary glossary) {
        log.info("Generating questions for created glossary: [{},{}]", glossary.getTermId(), glossary.getKey());
        List<Question> templateQuestionList = questionRepository.findByTypeAndCategory(
                QuestionType.TEMPLATE, glossary.getCategory());

        return templateQuestionList.stream()
                .map(templateQuestion -> createQuestion(templateQuestion, glossary))
                .toList();
    }

    @Transactional
    public List<Question> generateFromCreatedTemplate(final Question template) {
        log.info("Generating questions from template: [{},{}]", template.getQuestionId(), template.getCategory());
        List<Glossary> categoryGlossaries = glossaryRepository.findAllByCategory(template.getCategory());
        return categoryGlossaries.stream().distinct()
                .map(glossary -> createQuestion(template, glossary))
                .toList();
    }

    private Question createQuestion(final Question templateQuestion, final Glossary glossary) {
        boolean hasAnswerByKey = Objects.nonNull(templateQuestion.getAttributes())
                && templateQuestion.getAttributes().contains(ANSWER_BY_KEY);
        Question question = createQuestion(templateQuestion, glossary, hasAnswerByKey);
        Question questionWithAnswers = addAnswers(question, glossary, hasAnswerByKey);
        return questionRepository.save(questionWithAnswers);
    }

    private static Question createQuestion(final Question templateQuestion, final Glossary glossary, boolean hasAnswerByKey) {
        String placeholder = hasAnswerByKey ? glossary.getValue() : glossary.getKey();
        String formattedContent = templateQuestion.getContent().formatted(placeholder);
        return templateQuestion.copy(QuestionType.GENERATED, formattedContent);
    }

    private static Question addAnswers(final Question question, final Glossary glossary, boolean hasAnswerByKey) {
        String content = hasAnswerByKey ? glossary.getKey() : glossary.getValue();
        Answer answer = Answer.builder().content(content).question(question).glossary(glossary).build();
        question.setAnswers(Collections.singletonList(answer));
        return question;
    }
}
