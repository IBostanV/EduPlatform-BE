package com.play.quiz.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Question;
import com.play.quiz.domain.Quiz;
import com.play.quiz.domain.UserQuizHistory;
import com.play.quiz.dto.UserQuizHistoryDto;
import com.play.quiz.fixtures.AccountFixture;
import com.play.quiz.fixtures.QuestionFixture;
import com.play.quiz.fixtures.QuizFixture;
import com.play.quiz.fixtures.QuizTypeFixture;
import com.play.quiz.fixtures.UserDetailsFixture;
import com.play.quiz.mapper.UserQuizHistoryMapperImpl;
import com.play.quiz.repository.QuizRepository;
import com.play.quiz.repository.QuizTypeRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.impl.UserQuizHistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.User;

// What a finished run pays. The marking is the result page's own, so these go through it rather
// than counting answers a second way.
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class QuizExperienceAwardTest {

    private static final long HISTORY_ID = 9L;
    // Every fixture answer points at the same glossary term, so this is the right one to give.
    private static final long RIGHT_TERM_ID = 1L;

    @Mock private UserService userService;
    @Mock private QuestionService questionService;
    @Mock private GlossaryService glossaryService;
    @Mock private AuthenticationFacade authenticationFacade;
    @Mock private UserQuizHistoryRepository userQuizHistoryRepository;
    @Mock private QuizRepository quizRepository;
    @Mock private QuizTypeRepository quizTypeRepository;
    @Mock private CustomQuizService customQuizService;

    private UserQuizHistoryService historyService;

    @BeforeEach
    void init() {
        historyService = new UserQuizHistoryServiceImpl(userService, questionService, glossaryService,
                new UserQuizHistoryMapperImpl(), authenticationFacade, userQuizHistoryRepository, quizRepository,
                quizTypeRepository, customQuizService);

        Account player = AccountFixture.getAdminAccount();
        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        when(userService.findByEmail(player.getEmail())).thenReturn(player);
        when(quizTypeRepository.findById(1L)).thenReturn(Optional.of(QuizTypeFixture.getDefaultQuizType()));
    }

    @Test
    void given_a_marked_run_when_saving_then_pay_ten_for_each_right_answer_and_two_for_each_wrong() {
        // Three questions, the first two answered with the right term and the third not at all.
        recorded(false, 3, "[{\"1\":{\"answer\":1,\"time\":1200}},{\"2\":{\"answer\":1,\"time\":900}}]");

        historyService.save(run());

        verify(userService).addExperience(AccountFixture.getAdminAccount().getAccountId(), 10 + 10 + 2);
    }

    @Test
    void given_a_run_with_nothing_answered_when_saving_then_still_pay_for_turning_up() {
        recorded(false, 3, "[]");

        historyService.save(run());

        verify(userService).addExperience(AccountFixture.getAdminAccount().getAccountId(), 3 * 2);
    }

    // Otherwise one quiz answered right could be replayed for as many levels as you like. It is
    // still marked, so the run shows its score in the player's history.
    @Test
    void given_a_quiz_this_player_has_finished_before_when_saving_then_score_it_but_pay_nothing() {
        UserQuizHistory recorded = recorded(false, 3, "[{\"1\":{\"answer\":1,\"time\":1200}}]");
        playedBefore();

        historyService.save(run());

        assertEquals(1, recorded.getRightAnswers());
        assertEquals(3, recorded.getTotalAnswers());
        verify(userService, never()).addExperience(anyLong(), anyInt());
    }

    // Players write their own custom quizzes, so paying for them would make a level worth whatever
    // the easiest self-made quiz is worth. The run is still scored: it belongs in the history.
    @Test
    void given_a_custom_quiz_when_saving_then_score_it_but_pay_nothing() {
        UserQuizHistory recorded = recorded(true, 3, "[]");

        historyService.save(run());

        assertEquals(0, recorded.getTotalAnswers(), "A custom quiz is marked by its own scorer");
        verify(userService, never()).addExperience(anyLong(), anyInt());
    }

    // The score is kept on the row, so the profile's history and statistics are a read rather
    // than a re-marking of every run the player has ever taken.
    @Test
    void given_a_marked_run_when_saving_then_keep_its_score_on_the_row() {
        UserQuizHistory recorded = recorded(false, 3, "[{\"1\":{\"answer\":1,\"time\":1200}}]");

        historyService.save(run());

        assertEquals(1, recorded.getRightAnswers());
        assertEquals(3, recorded.getTotalAnswers());
    }

    @Test
    void given_a_run_that_cannot_be_marked_when_saving_then_keep_the_history_and_pay_nothing() {
        // The history is the player's; a marking that blows up must not take it down with it.
        recorded(false, 3, "[]");
        when(questionService.getByIds(any())).thenThrow(new IllegalStateException("questions gone"));

        historyService.save(run());

        verify(userQuizHistoryRepository).save(any());
        verify(userService, never()).addExperience(anyLong(), anyInt());
    }

    /** The saved run, handed back by the repository the way it would be after the insert. */
    private UserQuizHistory recorded(final boolean custom, final int questions, final String answersJson) {
        Quiz quiz = QuizFixture.getQuiz().toBuilder().custom(custom).build();
        UserQuizHistory history = UserQuizHistory.builder()
                .historyId(HISTORY_ID)
                .quiz(quiz)
                .answersJson(answersJson)
                .account(AccountFixture.getAdminAccount())
                .build();

        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(userQuizHistoryRepository.save(any())).thenReturn(history);
        when(userQuizHistoryRepository.getReferenceById(HISTORY_ID)).thenReturn(history);
        when(questionService.getByIds(any())).thenReturn(askedQuestions(questions));
        return history;
    }

    /** A run of the same quiz by the same player, other than the one just recorded. */
    private void playedBefore() {
        when(userQuizHistoryRepository.existsByQuiz_QuizIdAndAccount_AccountIdAndHistoryIdNot(
                1L, AccountFixture.getAdminAccount().getAccountId(), HISTORY_ID)).thenReturn(true);
    }

    // Question n is answered right by giving term RIGHT_TERM_ID, which every fixture answer holds.
    private static List<Question> askedQuestions(final int count) {
        return LongStream.rangeClosed(1, count)
                .mapToObj(id -> QuestionFixture.getGeneratedQuestion(id, "Life"))
                .toList();
    }

    private static UserQuizHistoryDto run() {
        return UserQuizHistoryDto.builder()
                .quiz(QuizFixture.getQuizNoQuestionDto())
                .answersJson("[]")
                .build();
    }
}
