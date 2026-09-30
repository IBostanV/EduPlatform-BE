package com.play.quiz.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.play.quiz.coin.Coins;
import com.play.quiz.conquest.ConquestAttempt;
import com.play.quiz.conquest.ConquestAttemptRepository;
import com.play.quiz.domain.Answer;
import com.play.quiz.domain.Glossary;
import com.play.quiz.domain.Question;
import com.play.quiz.domain.Quiz;
import com.play.quiz.domain.UserQuizHistory;
import com.play.quiz.dto.GlossaryDto;
import com.play.quiz.dto.wrapper.HistoryAnswer;
import com.play.quiz.fixtures.AccountFixture;
import com.play.quiz.fixtures.QuizTypeFixture;
import com.play.quiz.mapper.UserQuizHistoryMapperImpl;
import com.play.quiz.repository.QuizRepository;
import com.play.quiz.repository.QuizTypeRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.impl.UserQuizHistoryServiceImpl;
import com.play.quiz.util.ExperiencePayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** A categorized run marked the way its quiz type was played. The question's right answer is 68 (term 1). */
@ExtendWith(MockitoExtension.class)
class QuizTypeScoringTest {

    @Mock private UserService userService;
    @Mock private QuestionService questionService;
    @Mock private GlossaryService glossaryService;
    @Mock private AuthenticationFacade authenticationFacade;
    @Mock private UserQuizHistoryRepository userQuizHistoryRepository;
    @Mock private QuizRepository quizRepository;
    @Mock private QuizTypeRepository quizTypeRepository;
    @Mock private CustomQuizService customQuizService;
    @Mock private ConquestAttemptRepository conquestAttemptRepository;

    private UserQuizHistoryService historyService;

    // France 68, Moldova 2.5, Germany 84 (millions).
    private static final Map<Long, GlossaryDto> TERMS = Map.of(
            1L, GlossaryDto.builder().termId(1L).key("France").value("68").build(),
            2L, GlossaryDto.builder().termId(2L).key("Moldova").value("2.5").build(),
            3L, GlossaryDto.builder().termId(3L).key("Germany").value("84").build());

    @BeforeEach
    void init() {
        historyService = new UserQuizHistoryServiceImpl(userService, questionService, glossaryService,
                new UserQuizHistoryMapperImpl(), authenticationFacade, userQuizHistoryRepository, quizRepository,
                quizTypeRepository, customQuizService, conquestAttemptRepository);
        lenient().when(glossaryService.getById(any())).thenAnswer(call -> TERMS.get(call.<Long>getArgument(0)));
    }

    @Test
    void input_is_right_whatever_the_case_and_spacing() {
        assertNull(score("INPUT", "\"  68 \"").getRightAnswer());
        assertEquals("68", score("INPUT", "\"67\"").getRightAnswer());
    }

    @Test
    void values_range_is_right_when_the_value_is_in_it() {
        assertNull(score("VALUES_RANGE", "{\"from\": 40, \"to\": 76}").getRightAnswer());
        assertNull(score("VALUES_RANGE", "{\"from\": 40, \"to\": null}").getRightAnswer());
        HistoryAnswer wrong = score("VALUES_RANGE", "{\"from\": null, \"to\": 40}");
        assertEquals("< 40", wrong.getUserAnswer());
        assertEquals("68", wrong.getRightAnswer());
    }

    @Test
    void in_order_is_right_smallest_first() {
        assertNull(score("IN_ORDER", "[2, 1, 3]").getRightAnswer());
        HistoryAnswer wrong = score("IN_ORDER", "[1, 2, 3]");
        assertEquals("France → Moldova → Germany", wrong.getUserAnswer());
        assertEquals("Moldova → France → Germany", wrong.getRightAnswer());
    }

    @Test
    void multiple_choice_is_right_only_with_exactly_the_right_answers() {
        assertNull(score("MULTIPLE_CHOICE", "[1]").getRightAnswer());
        assertEquals("68", score("MULTIPLE_CHOICE", "[1, 2]").getRightAnswer());
    }

    @Test
    void one_pick_is_still_marked_by_term() {
        assertNull(score("ONE_FROM_TWO", "1").getRightAnswer());
        assertEquals("68", score("SINGLE_CHOICE", "2").getRightAnswer());
    }

    @Test
    void the_coins_a_run_paid_are_its_first_run_payout_plus_the_conquest_top_up() {
        // 40 right of 40: 40 * PER_RIGHT_ANSWER experience, a tenth of it in coins.
        UserQuizHistory run = UserQuizHistory.builder().historyId(7L).account(AccountFixture.getAdminAccount())
                .quiz(Quiz.builder().quizId(5L).questionIds(Set.of()).build())
                .rightAnswers(40).totalAnswers(40).answersJson("[]").build();
        when(userQuizHistoryRepository.getReferenceById(7L)).thenReturn(run);
        when(questionService.getByIds(any())).thenReturn(List.of());
        int base = Coins.forExperience(ExperiencePayout.forRun(40, 40));

        when(conquestAttemptRepository.findByHistoryId(7L)).thenReturn(Optional.empty());
        assertEquals(base, historyService.getById(7L).getCoinsEarned());

        when(conquestAttemptRepository.findByHistoryId(7L)).thenReturn(Optional.of(new ConquestAttempt()));
        assertEquals(base + Coins.forExperience(ExperiencePayout.conquestTopUp(40, 40)),
                historyService.getById(7L).getCoinsEarned());

        // A replay paid nothing of its own.
        when(userQuizHistoryRepository.existsByQuiz_QuizIdAndAccount_AccountIdAndHistoryIdLessThan(any(), any(), any()))
                .thenReturn(true);
        when(conquestAttemptRepository.findByHistoryId(7L)).thenReturn(Optional.empty());
        assertEquals(0, historyService.getById(7L).getCoinsEarned());
    }

    private HistoryAnswer score(final String quizType, final String answer) {
        Question question = Question.builder().questionId(10L).content("Population of France?").build();
        question.setAnswers(List.of(Answer.builder().content("68")
                .glossary(Glossary.builder().termId(1L).key("France").value("68").build()).build()));
        Quiz quiz = Quiz.builder().quizId(5L).questionIds(Set.of(10L))
                .type(QuizTypeFixture.getQuizType(1L, quizType, 1)).build();
        UserQuizHistory history = UserQuizHistory.builder().historyId(7L).quiz(quiz)
                .answersJson("[{\"10\": {\"answer\": " + answer + ", \"time\": 1200}}]").build();
        when(userQuizHistoryRepository.getReferenceById(7L)).thenReturn(history);
        when(questionService.getByIds(any())).thenReturn(List.of(question));

        return historyService.getById(7L).getAnswers().get(0);
    }
}
