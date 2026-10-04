package com.play.quiz.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.play.quiz.conquest.ConquestAttemptRepository;
import com.play.quiz.domain.Quiz;
import com.play.quiz.domain.Account;
import com.play.quiz.domain.QuizType;
import com.play.quiz.domain.UserQuizHistory;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.dto.UserQuizHistoryDto;
import com.play.quiz.fixtures.AccountFixture;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

/** Saving a finished run: Q_QUIZ.TYPE is NOT NULL, whatever the player filtered by. */
@ExtendWith(MockitoExtension.class)
class UserQuizHistorySaveTest {

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

    @BeforeEach
    void init() {
        historyService = new UserQuizHistoryServiceImpl(userService, questionService, glossaryService,
                new UserQuizHistoryMapperImpl(), authenticationFacade, userQuizHistoryRepository, quizRepository,
                quizTypeRepository, customQuizService, conquestAttemptRepository);
    }

    @Test
    void given_a_quiz_played_with_no_type_filter_when_saving_the_run_then_store_the_default_type() {
        signedIn();
        when(quizTypeRepository.findById(1L)).thenReturn(Optional.of(QuizTypeFixture.getDefaultQuizType()));

        historyService.save(UserQuizHistoryDto.builder()
                .quiz(QuizFixture.getQuizNoQuestionDto().toBuilder().quizId(null).quizType(null).build())
                .answersJson("[]")
                .build());

        // Without this the insert would put NULL in a NOT NULL column.
        assertNotNull(saved().getQuiz().getType());
    }

    @Test
    void given_a_quiz_played_with_a_type_when_saving_the_run_then_keep_that_type() {
        signedIn();
        QuizType picked = QuizTypeFixture.getQuizType(2L, "MULTIPLE_CHOICE", 2);

        historyService.save(UserQuizHistoryDto.builder()
                .quiz(QuizFixture.getQuizNoQuestionDto().toBuilder().quizId(null).quizType(picked).build())
                .answersJson("[]")
                .build());

        assertEquals("MULTIPLE_CHOICE", saved().getQuiz().getType().getName());
    }

    @Test
    void given_a_player_with_a_mutual_friend_when_saving_the_run_then_map_it_without_walking_them() {
        // A friendship is mutual, so the two accounts point at each other: mapping the account
        // would go round for ever.
        Account me = AccountFixture.getAdminAccount();
        Account friend = AccountFixture.getNoRolesAccount();
        me.setFriends(new HashSet<>(Set.of(friend)));
        friend.setFriends(new HashSet<>(Set.of(me)));

        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        when(userService.findByEmail(me.getEmail())).thenReturn(me);
        when(userQuizHistoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(quizTypeRepository.findById(1L)).thenReturn(Optional.of(QuizTypeFixture.getDefaultQuizType()));

        UserQuizHistoryDto result = historyService.save(UserQuizHistoryDto.builder()
                .quiz(QuizFixture.getQuizNoQuestionDto().toBuilder().quizId(null).quizType(null).build())
                .answersJson("[]")
                .build());

        // Whoever asked for the run is the one who played it, so it is not sent back.
        assertNull(result.getAccount());
    }

    private void signedIn() {
        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        when(userService.findByEmail(AccountFixture.getAdminAccount().getEmail()))
                .thenReturn(AccountFixture.getAdminAccount());
        when(userQuizHistoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private UserQuizHistory saved() {
        ArgumentCaptor<UserQuizHistory> history = ArgumentCaptor.forClass(UserQuizHistory.class);
        org.mockito.Mockito.verify(userQuizHistoryRepository).save(history.capture());
        return history.getValue();
    }
}
