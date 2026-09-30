package com.play.quiz.service;

import java.util.List;

import com.play.quiz.domain.Answer;
import com.play.quiz.domain.GlossaryType;

public interface AnswerService {

    // The only way wrong options are drawn: from the glossary type the right answer belongs to, so
    // every option is the same kind of thing. Drawing across a whole category gave the answer away.
    List<Answer> getWrongOptionsByGlossaryTypeWithLimit(final GlossaryType glossaryType, final List<Long> termIdList, int limit, boolean answerByKey);
}
