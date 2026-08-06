package com.play.quiz.live;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import com.play.quiz.domain.Account;
import com.play.quiz.dto.AnswerDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.live.LiveService.CreateInput;
import com.play.quiz.live.LiveService.PlayerView;
import com.play.quiz.live.LiveService.RoomView;
import com.play.quiz.record.MiniGameResult;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.UserGroupRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.QuestionService;
import com.play.quiz.service.UserService;
import com.play.quiz.social.Presence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LiveServiceTest {

    private static final long RIGHT = 10L;
    private static final long WRONG = 11L;

    @Mock private QuestionService questionService;
    @Mock private UserService userService;
    @Mock private AccountRepository accountRepository;
    @Mock private UserGroupRepository userGroupRepository;
    @Mock private AuthenticationFacade authenticationFacade;
    @Mock private SimpMessagingTemplate messaging;

    private final Account host = Account.builder().accountId(1L).email("host@quiz").username("Host").build();
    private final Account friend = Account.builder().accountId(2L).email("friend@quiz").username("Friend").build();
    private final Account stranger = Account.builder().accountId(3L).email("stranger@quiz").username("Stranger").build();
    private Account current;
    private LiveService live;

    @BeforeEach
    void init() {
        live = new LiveService(questionService, userService, accountRepository, userGroupRepository,
                authenticationFacade, messaging, new Presence());
        when(authenticationFacade.getPrincipal()).thenAnswer(invocation ->
                new User(current.getEmail(), "secret", List.of()));
        when(userService.findByEmail(anyString())).thenAnswer(invocation -> List.of(host, friend, stranger).stream()
                .filter(account -> account.getEmail().equals(invocation.getArgument(0)))
                .findFirst().orElseThrow());
        when(accountRepository.findFriends(1L)).thenReturn(List.of(friend));
        when(accountRepository.findFriends(2L)).thenReturn(List.of(host));

        QuestionDto question = QuestionDto.builder().id(5L).content("Capital of France?")
                .answers(List.of(AnswerDto.builder().id(RIGHT).content("Paris").build(),
                        AnswerDto.builder().id(WRONG).content("Lyon").build()))
                .build();
        when(questionService.getMiniGameQuestions(any(Integer.class))).thenReturn(List.of(question, question, question));
        when(questionService.checkMiniGameAnswer(anyLong(), any())).thenAnswer(invocation -> {
            AnswerDto choice = invocation.getArgument(1);
            return new MiniGameResult(Long.valueOf(RIGHT).equals(choice.getId()), RIGHT, null, "Paris");
        });
    }

    @AfterEach
    void stop() {
        live.stop();
    }

    @Test
    void given_a_duel_then_only_the_invited_friend_gets_in() {
        current = host;
        RoomView room = live.create(new CreateInput(LiveRoom.Mode.DUEL, Set.of(2L), null, null, null));
        verify(messaging).convertAndSendToUser(eq("friend@quiz"), eq("/live"),
                argThat((LiveService.LiveEvent event) -> "INVITE".equals(event.type())));

        current = stranger;
        assertThrows(IllegalArgumentException.class, () -> live.join(room.code()));

        current = friend;
        assertEquals(2, live.join(room.code()).players().size());
    }

    @Test
    void given_a_duel_against_someone_who_is_not_a_friend_then_refuse_it() {
        current = host;
        assertThrows(IllegalArgumentException.class,
                () -> live.create(new CreateInput(LiveRoom.Mode.DUEL, Set.of(3L), null, null, null)));
    }

    @Test
    void given_both_answered_then_the_reveal_comes_at_once_and_a_quick_right_answer_scores_most() throws Exception {
        current = host;
        String code = live.create(new CreateInput(LiveRoom.Mode.DUEL, Set.of(2L), null, 3, 10)).code();
        current = friend;
        live.join(code);

        current = friend;
        assertThrows(IllegalArgumentException.class, () -> live.start(code));
        current = host;
        assertEquals("COUNTDOWN", live.start(code).phase());

        // The countdown runs on the match's own clock.
        long deadline = System.currentTimeMillis() + 6000;
        while (!"QUESTION".equals(live.get(code).phase()) && System.currentTimeMillis() < deadline) {
            Thread.sleep(50);
        }
        assertEquals("QUESTION", live.get(code).phase());

        live.answer(code, 0, AnswerDto.builder().id(RIGHT).build());
        // An answer once is an answer: the second does not count.
        live.answer(code, 0, AnswerDto.builder().id(WRONG).build());
        current = friend;
        RoomView revealed = live.answer(code, 0, AnswerDto.builder().id(WRONG).build());

        assertEquals("REVEAL", revealed.phase());
        PlayerView first = revealed.players().get(0);
        PlayerView second = revealed.players().get(1);
        assertEquals(1L, first.user().id());
        assertTrue(first.score() > LiveService.BASE_POINTS && first.score() <= LiveService.BASE_POINTS + LiveService.SPEED_POINTS,
                "score " + first.score());
        assertEquals(0, second.score());
        assertFalse(second.lastCorrect());
        assertEquals(RIGHT, revealed.reveal().answerId());
    }
}
