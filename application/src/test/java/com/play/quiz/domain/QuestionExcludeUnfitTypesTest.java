package com.play.quiz.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.play.quiz.enums.QuestionAttribute;
import org.junit.jupiter.api.Test;

class QuestionExcludeUnfitTypesTest {

    private static Answer term(final String value) {
        return Answer.builder().content(value).glossary(Glossary.builder().termId(1L).build()).build();
    }

    private static long excluded(final long chosen, final List<QuestionAttribute> attributes, final Answer... answers) {
        Question question = Question.builder().excludeType(chosen).attributes(attributes).answers(List.of(answers)).build();
        question.excludeUnfitTypes();
        return question.getExcludeType();
    }

    @Test
    void oneAnswerFitsNoneOfTheThree() {
        assertEquals(2 | 16 | 128 | 32, excluded(32, List.of(), term("Paris")));
    }

    @Test
    void typedAnswersOnlyFitMultipleChoice() {
        assertEquals(16 | 128, excluded(0, List.of(), Answer.builder().content("a").build(), Answer.builder().content("b").build()));
    }

    @Test
    void termsFitDragAndDropAndNumbersAlsoInOrder() {
        assertEquals(128, excluded(0, List.of(), term("Paris"), term("Madrid")));
        assertEquals(0, excluded(0, List.of(), term("67 000 000"), term("1,5")));
    }

    @Test
    void answersByKeyAreNotPairs() {
        assertEquals(16 | 128, excluded(0, List.of(QuestionAttribute.ANSWER_BY_KEY), term("1"), term("2")));
    }
}
