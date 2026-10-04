package com.play.quiz.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.CustomAnswer;
import com.play.quiz.domain.CustomQuestion;
import com.play.quiz.domain.Quiz;
import com.play.quiz.enums.UserRole;
import com.play.quiz.domain.QuizInvite;
import com.play.quiz.dto.CustomQuizDto;
import com.play.quiz.dto.CustomQuizPlayDto;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.dto.wrapper.HistoryAnswer;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.fixtures.AccountFixture;
import com.play.quiz.fixtures.QuizFixture;
import com.play.quiz.fixtures.QuizTypeFixture;
import com.play.quiz.fixtures.UserDetailsFixture;
import com.play.quiz.mapper.QuizMapperImpl;
import com.play.quiz.record.CustomQuizSummary;
import com.play.quiz.record.QuizInvitation;
import com.play.quiz.repository.CategoryRepository;
import com.play.quiz.repository.CustomQuestionRepository;
import com.play.quiz.repository.QuizInviteRepository;
import com.play.quiz.repository.QuizRepository;
import com.play.quiz.repository.QuizTypeRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.repository.UserRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.impl.CustomQuizServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
class CustomQuizServiceImplTest {

    @Mock private AuthenticationFacade authenticationFacade;
    @Mock private CategoryRepository categoryRepository;
    @Mock private CustomQuestionRepository customQuestionRepository;
    @Mock private QuizInviteRepository quizInviteRepository;
    @Mock private QuizRepository quizRepository;
    @Mock private QuizTypeRepository quizTypeRepository;
    @Mock private UserQuizHistoryRepository userQuizHistoryRepository;
    @Mock private UserRepository userRepository;

    private CustomQuizService customQuizService;

    @BeforeEach
    void init() {
        customQuizService = new CustomQuizServiceImpl(authenticationFacade, categoryRepository, customQuestionRepository,
                quizInviteRepository, new QuizMapperImpl(), quizRepository, quizTypeRepository, userQuizHistoryRepository,
                userRepository);
    }

    // ---- create --------------------------------------------------------------------------------

    @Test
    void given_custom_quiz_when_create_then_save_its_questions_and_invite_everyone_but_the_creator() {
        Account creator = AccountFixture.getAdminAccount();
        Account friend = Account.builder().accountId(2L).email("friend@playquiz.com").build();
        Quiz savedQuiz = QuizFixture.getQuiz();

        when(quizTypeRepository.findById(1L)).thenReturn(Optional.of(QuizTypeFixture.getDefaultQuizType()));
        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        when(userRepository.findUserByEmail(creator.getEmail())).thenReturn(Optional.of(creator));
        when(quizRepository.save(any())).thenReturn(savedQuiz);
        when(customQuestionRepository.save(any())).thenReturn(
                CustomQuestion.builder().questionId(10L).build(), CustomQuestion.builder().questionId(11L).build());
        // The creator's own id is in the request and must not come back out of it.
        when(userRepository.findByUserIds(Set.of(friend.getAccountId()))).thenReturn(Set.of(friend));

        QuizDto result = customQuizService.create(CustomQuizDto.builder()
                .quizTypeId(1L)
                .questions(List.of(
                        new CustomQuizDto.CustomQuestionDto(" Capital of Moldova? ", List.of(" Chisinau "),
                                List.of(" Balti ", "Cahul")),
                        new CustomQuizDto.CustomQuestionDto("Largest ocean?", List.of("Pacific"), List.of("Atlantic"))))
                .timePerQuestion(30)
                .categoryIds(Set.of(1L))
                .invitedUserIds(Set.of(creator.getAccountId(), friend.getAccountId()))
                .build());

        ArgumentCaptor<CustomQuestion> questions = ArgumentCaptor.forClass(CustomQuestion.class);
        verify(customQuestionRepository, times(2)).save(questions.capture());
        CustomQuestion first = questions.getAllValues().getFirst();
        assertEquals("Capital of Moldova?", first.getContent());
        assertEquals(0, first.getPosition());
        // Right answers first, then the wrong options, trimmed, numbered, and pointing back.
        assertEquals(List.of("Chisinau", "Balti", "Cahul"), first.getAnswers().stream().map(CustomAnswer::getContent).toList());
        assertEquals(List.of(true, false, false), first.getAnswers().stream().map(CustomAnswer::isRight).toList());
        assertEquals(List.of(0, 1, 2), first.getAnswers().stream().map(CustomAnswer::getPosition).toList());
        assertTrue(first.getAnswers().stream().allMatch(answer -> answer.getQuestion() == first));

        // Saved once, after its questions: QUESTION_IDS lists their ids, in the order written.
        ArgumentCaptor<Quiz> quiz = ArgumentCaptor.forClass(Quiz.class);
        verify(quizRepository, times(1)).save(quiz.capture());
        assertEquals(30, quiz.getValue().getQuestionTime());
        // Flagged, so its ids are read as Q_CUSTOM_QUESTION ones, not Q_QUESTION ones.
        assertTrue(quiz.getValue().isCustom());
        assertEquals(List.of(10L, 11L), new ArrayList<>(quiz.getValue().getQuestionIds()));

        // Asked for per question, answered as the whole run's budget.
        assertEquals(60, result.getQuizTime());
        verify(quizInviteRepository, times(1)).save(any());
    }

    @Test
    void given_choice_question_without_wrong_options_when_create_then_reject_before_saving_anything() {
        when(quizTypeRepository.findById(1L)).thenReturn(Optional.of(QuizTypeFixture.getDefaultQuizType()));

        // A single choice with nothing to choose between.
        CustomQuizDto request = request(1L, new CustomQuizDto.CustomQuestionDto("Largest ocean?", List.of("Pacific"), null));

        assertThrows(IllegalArgumentException.class, () -> customQuizService.create(request));
        verify(quizRepository, never()).save(any());
        verify(customQuestionRepository, never()).save(any());
    }

    @Test
    void given_typed_answer_question_with_wrong_options_when_create_then_reject() {
        when(quizTypeRepository.findById(4L)).thenReturn(Optional.of(QuizTypeFixture.getQuizType(4L, "INPUT", 8)));

        CustomQuizDto request = request(4L,
                new CustomQuizDto.CustomQuestionDto("Largest ocean?", List.of("Pacific"), List.of("Atlantic")));

        assertThrows(IllegalArgumentException.class, () -> customQuizService.create(request));
    }

    @Test
    void given_map_quiz_type_when_create_then_reject() {
        when(quizTypeRepository.findById(6L)).thenReturn(Optional.of(QuizTypeFixture.getQuizType(6L, "MAP_CHOICE", 32)));

        CustomQuizDto request = request(6L,
                new CustomQuizDto.CustomQuestionDto("Where is Chisinau?", List.of("Moldova"), List.of("Romania")));

        assertThrows(IllegalArgumentException.class, () -> customQuizService.create(request));
    }

    // ---- getForPlay --------------------------------------------------------------------------

    @Test
    void given_quiz_made_from_admin_questions_when_getForPlay_then_not_found() {
        when(quizRepository.findById(1L)).thenReturn(Optional.of(QuizFixture.getQuiz()));

        assertThrows(RecordNotFoundException.class, () -> customQuizService.getForPlay(1L));
    }

    // getForPlay reads the caller's roles (admins may open any custom quiz).
    private void signedIn(final String role) {
        when(authenticationFacade.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(
                UserDetailsFixture.getAdminUserDetails(), null, List.of(new SimpleGrantedAuthority(role))));
    }

    @Test
    void given_someone_neither_creator_nor_invited_when_getForPlay_then_deny() {
        Quiz quiz = customQuiz("SINGLE_CHOICE");
        quiz.setCreatedBy(Account.builder().accountId(9L).email("creator@playquiz.com").build());
        String stranger = AccountFixture.getAdminAccount().getEmail();

        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        when(quizInviteRepository.existsByQuiz_QuizIdAndAccount_Email(1L, stranger)).thenReturn(false);
        signedIn(UserRole.ROLE_USER.name());

        assertThrows(AccessDeniedException.class, () -> customQuizService.getForPlay(1L));
    }

    @Test
    void given_invited_player_when_getForPlay_then_questions_with_all_their_options_and_the_run_time() {
        givenInvitedPlayerOf(customQuiz("SINGLE_CHOICE"), capitalQuestion());

        CustomQuizPlayDto result = customQuizService.getForPlay(1L);

        assertEquals(150, result.quiz().getQuizTime());
        CustomQuizPlayDto.Question question = result.questions().getFirst();
        assertEquals(Set.of("Chisinau", "Balti"),
                question.answers().stream().map(CustomQuizPlayDto.Option::content).collect(Collectors.toSet()));
    }

    @Test
    void given_typed_answer_quiz_when_getForPlay_then_send_no_options_since_they_are_the_answers() {
        givenInvitedPlayerOf(customQuiz("INPUT"), capitalQuestion());

        CustomQuizPlayDto result = customQuizService.getForPlay(1L);

        assertTrue(result.questions().getFirst().answers().isEmpty());
    }

    // ---- getMyInvitations ------------------------------------------------------------------

    @Test
    void given_invites_when_getMyInvitations_then_list_each_quiz_with_its_inviter_and_whether_it_was_played() {
        String me = AccountFixture.getAdminAccount().getEmail();
        QuizInvite invite = QuizInvite.builder()
                .quiz(customQuiz("IN_ORDER"))
                .account(AccountFixture.getAdminAccount())
                .build();
        invite.setCreatedBy(Account.builder().accountId(9L).username("Ion").email("ion@playquiz.com").build());

        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        when(quizInviteRepository.findByInvitedEmail(me)).thenReturn(List.of(invite));
        when(userQuizHistoryRepository.existsByQuiz_QuizIdAndAccount_Email(1L, me)).thenReturn(true);

        QuizInvitation result = customQuizService.getMyInvitations().getFirst();

        assertEquals(1L, result.quizId());
        assertEquals("IN_ORDER", result.quizType());
        assertEquals(5, result.questionsCount());
        assertEquals(30, result.questionTime());
        // Shown by name, never by email.
        assertEquals("Ion", result.invitedBy().displayName());
        assertTrue(result.played());
    }

    @Test
    void given_an_admin_when_getForPlay_then_open_a_quiz_they_neither_made_nor_were_invited_to() {
        Quiz quiz = customQuiz("SINGLE_CHOICE");
        quiz.setCreatedBy(Account.builder().accountId(9L).email("someone@playquiz.com").build());

        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        when(customQuestionRepository.findAllById(Set.of(5L))).thenReturn(List.of(capitalQuestion()));
        // Moderation: no invite needed.
        signedIn(UserRole.ROLE_ADMIN.name());

        assertEquals(1, customQuizService.getForPlay(1L).questions().size());
    }

    // ---- delete ---------------------------------------------------------------------------

    @Test
    void given_my_own_quiz_when_delete_then_remove_its_runs_invitations_and_questions_with_it() {
        Quiz quiz = customQuiz("SINGLE_CHOICE");
        quiz.setCreatedBy(AccountFixture.getAdminAccount());

        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        signedIn(UserRole.ROLE_USER.name());

        customQuizService.delete(1L);

        // The rows pointing at the quiz go before the quiz itself.
        verify(userQuizHistoryRepository).deleteByQuiz_QuizId(1L);
        verify(quizInviteRepository).deleteByQuiz_QuizId(1L);
        verify(customQuestionRepository).deleteAllById(Set.of(5L));
        verify(quizRepository).delete(quiz);
    }

    @Test
    void given_someone_elses_quiz_when_delete_then_deny_and_remove_nothing() {
        Quiz quiz = customQuiz("SINGLE_CHOICE");
        quiz.setCreatedBy(Account.builder().accountId(9L).email("someone@playquiz.com").build());

        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        signedIn(UserRole.ROLE_USER.name());

        assertThrows(AccessDeniedException.class, () -> customQuizService.delete(1L));
        verify(quizRepository, never()).delete(any());
        verify(customQuestionRepository, never()).deleteAllById(any());
    }

    @Test
    void given_someone_elses_quiz_when_an_admin_deletes_it_then_remove_it() {
        Quiz quiz = customQuiz("SINGLE_CHOICE");
        quiz.setCreatedBy(Account.builder().accountId(9L).email("someone@playquiz.com").build());

        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        // Moderation: an admin may remove what a player wrote.
        signedIn(UserRole.ROLE_ADMIN.name());

        customQuizService.delete(1L);

        verify(quizRepository).delete(quiz);
    }

    // ---- getMyQuizzes ---------------------------------------------------------------------

    @Test
    void given_quizzes_i_made_when_getMyQuizzes_then_list_each_with_how_many_were_invited_and_played() {
        String me = AccountFixture.getAdminAccount().getEmail();
        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        when(quizRepository.findByCustomTrueAndCreatedBy_EmailOrderByCreatedDateDesc(me))
                .thenReturn(List.of(customQuiz("INPUT")));
        when(quizInviteRepository.countByQuiz_QuizId(1L)).thenReturn(3L);
        when(userQuizHistoryRepository.countByQuiz_QuizId(1L)).thenReturn(2L);

        CustomQuizSummary result = customQuizService.getMyQuizzes().getFirst();

        assertEquals(1L, result.quizId());
        assertEquals("INPUT", result.quizType());
        assertEquals(5, result.questionsCount());
        assertEquals(30, result.questionTime());
        assertEquals(3, result.invited());
        assertEquals(2, result.played());
    }

    @Test
    void given_every_custom_quiz_when_getAllCustomQuizzes_then_list_them_with_who_made_each() {
        Quiz quiz = customQuiz("SINGLE_CHOICE");
        quiz.setCreatedBy(Account.builder().accountId(9L).username("Ion").email("ion@playquiz.com").build());
        when(quizRepository.findByCustomTrueOrderByCreatedDateDesc()).thenReturn(List.of(quiz));
        when(quizInviteRepository.countByQuiz_QuizId(1L)).thenReturn(1L);
        when(userQuizHistoryRepository.countByQuiz_QuizId(1L)).thenReturn(0L);

        CustomQuizSummary result = customQuizService.getAllCustomQuizzes().getFirst();

        // Shown by name, never by email.
        assertEquals("Ion", result.createdBy().displayName());
        assertEquals(1, result.invited());
    }

    // ---- score -----------------------------------------------------------------------------------

    @Test
    void given_in_order_quiz_when_items_are_out_of_order_then_wrong_with_the_right_order_shown() {
        HistoryAnswer result = score("IN_ORDER", orderQuestion(), "[10, 12, 11]");

        assertEquals("One → Three → Two", result.getUserAnswer());
        assertEquals("One → Two → Three", result.getRightAnswer());
    }

    @Test
    void given_in_order_quiz_when_items_are_in_order_then_right() {
        assertNull(score("IN_ORDER", orderQuestion(), "[10, 11, 12]").getRightAnswer());
    }

    @Test
    void given_multiple_choice_quiz_when_every_right_answer_is_picked_in_any_order_then_right() {
        // One and Three are right; Two is the wrong option.
        CustomQuestion question = question(answer(10L, "One", true, 0), answer(12L, "Three", true, 1), answer(11L, "Two", false, 2));

        HistoryAnswer result = score("MULTIPLE_CHOICE", question, "[12, 10]");

        assertEquals("Three, One", result.getUserAnswer());
        assertNull(result.getRightAnswer());
    }

    @Test
    void given_multiple_choice_quiz_when_a_right_answer_is_missed_then_wrong() {
        CustomQuestion question = question(answer(10L, "One", true, 0), answer(12L, "Three", true, 1), answer(11L, "Two", false, 2));

        assertEquals("One, Three", score("MULTIPLE_CHOICE", question, "[10]").getRightAnswer());
    }

    @Test
    void given_single_choice_quiz_when_the_wrong_option_is_picked_then_wrong() {
        HistoryAnswer result = score("SINGLE_CHOICE", capitalQuestion(), "21");

        assertEquals("Balti", result.getUserAnswer());
        assertEquals("Chisinau", result.getRightAnswer());
    }

    @Test
    void given_input_quiz_when_typed_with_other_case_and_spaces_then_right() {
        HistoryAnswer result = score("INPUT", capitalQuestion(), "\"  chisinau \"");

        assertEquals("chisinau", result.getUserAnswer());
        assertNull(result.getRightAnswer());
    }

    @Test
    void given_question_left_unanswered_when_score_then_no_pick_and_the_right_answer_shown() {
        Quiz quiz = customQuiz("SINGLE_CHOICE");
        when(customQuestionRepository.findAllById(Set.of(5L))).thenReturn(List.of(capitalQuestion()));

        HistoryAnswer result = customQuizService.score(quiz, "[]").getFirst();

        assertNull(result.getUserAnswer());
        assertEquals("Chisinau", result.getRightAnswer());
    }

    // ---- helpers ---------------------------------------------------------------------------------

    private HistoryAnswer score(final String quizTypeName, final CustomQuestion question, final String pickJson) {
        when(customQuestionRepository.findAllById(Set.of(5L))).thenReturn(List.of(question));
        String saved = "[{\"" + question.getQuestionId() + "\": {\"answer\": " + pickJson + ", \"time\": 1200}}]";
        return customQuizService.score(customQuiz(quizTypeName), saved).getFirst();
    }

    private void givenInvitedPlayerOf(final Quiz quiz, final CustomQuestion question) {
        String invited = AccountFixture.getAdminAccount().getEmail();
        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        when(quizInviteRepository.existsByQuiz_QuizIdAndAccount_Email(1L, invited)).thenReturn(true);
        when(customQuestionRepository.findAllById(Set.of(5L))).thenReturn(List.of(question));
        signedIn(UserRole.ROLE_USER.name());
    }

    /** A custom quiz: five questions at thirty seconds each, of the given type. */
    private static Quiz customQuiz(final String quizTypeName) {
        return QuizFixture.getQuiz().toBuilder()
                .custom(true)
                .questionIds(Set.of(5L))
                .type(QuizTypeFixture.getQuizType(1L, quizTypeName, 1))
                .questionTime(30)
                .build();
    }

    private static CustomQuestion capitalQuestion() {
        return question(answer(20L, "Chisinau", true, 0), answer(21L, "Balti", false, 1));
    }

    private static CustomQuestion orderQuestion() {
        return question(answer(10L, "One", true, 0), answer(11L, "Two", true, 1), answer(12L, "Three", true, 2));
    }

    private static CustomQuestion question(final CustomAnswer... answers) {
        return CustomQuestion.builder()
                .questionId(5L)
                .content("The question")
                .answers(new ArrayList<>(List.of(answers)))
                .build();
    }

    private static CustomAnswer answer(final Long id, final String content, boolean right, int position) {
        return CustomAnswer.builder().answerId(id).content(content).right(right).position(position).build();
    }

    private static CustomQuizDto request(final Long quizTypeId, final CustomQuizDto.CustomQuestionDto question) {
        return CustomQuizDto.builder()
                .quizTypeId(quizTypeId)
                .questions(List.of(question))
                .timePerQuestion(30)
                .categoryIds(Set.of(1L))
                .build();
    }
}
