package com.play.quiz.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Feedback;
import com.play.quiz.enums.FeedbackType;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.record.FeedbackEntry;
import com.play.quiz.record.FeedbackInput;
import com.play.quiz.repository.FeedbackRepository;
import com.play.quiz.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplTest {

    @Mock private FeedbackRepository feedbackRepository;

    private FeedbackService feedbackService;

    @BeforeEach
    void init() {
        feedbackService = new FeedbackServiceImpl(feedbackRepository);
    }

    @Test
    void given_feedback_when_send_then_save_it_trimmed_and_open() {
        when(feedbackRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        feedbackService.send(new FeedbackInput(FeedbackType.BUG, "  The map does not load  ", "  ", null, null), null);

        ArgumentCaptor<Feedback> saved = ArgumentCaptor.forClass(Feedback.class);
        verify(feedbackRepository).save(saved.capture());
        assertEquals(FeedbackType.BUG, saved.getValue().getType());
        assertEquals("The map does not load", saved.getValue().getMessage());
        // A blank page is no page.
        assertNull(saved.getValue().getPage());
        assertEquals(false, saved.getValue().isResolved());
    }

    @Test
    void given_guest_feedback_when_send_then_keep_their_contact_address_and_no_sender() {
        when(feedbackRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        FeedbackEntry entry = feedbackService.send(
                new FeedbackInput(FeedbackType.QUESTION, "Is it free?", "/home", null, " guest@mail.com "), null);

        // No account behind a guest: the audit aspect leaves CREATED_BY empty.
        assertNull(entry.from());
        assertEquals("guest@mail.com", entry.contactEmail());
    }

    @Test
    void given_problem_reported_from_a_quiz_when_send_then_keep_which_question_it_is_about() {
        when(feedbackRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        FeedbackEntry entry = feedbackService.send(new FeedbackInput(FeedbackType.BUG, "Wrong answer",
                "/quiz/express", " Question #12: What is the capital of Moldova? ", null), null);

        assertEquals("Question #12: What is the capital of Moldova?", entry.question());
    }

    @Test
    void given_a_screenshot_when_send_then_keep_it_with_its_type() {
        when(feedbackRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        MockMultipartFile picture = new MockMultipartFile("screenshot", "shot.png", "image/png", new byte[]{1, 2, 3});

        FeedbackEntry entry = feedbackService.send(
                new FeedbackInput(FeedbackType.BUG, "Look at this", "/home", null, null), picture);

        ArgumentCaptor<Feedback> saved = ArgumentCaptor.forClass(Feedback.class);
        verify(feedbackRepository).save(saved.capture());
        assertArrayEquals(new byte[]{1, 2, 3}, saved.getValue().getScreenshot());
        assertEquals("image/png", saved.getValue().getScreenshotType());
        // The list only says there is one; the picture is fetched on its own.
        assertTrue(entry.hasScreenshot());
    }

    @Test
    void given_a_file_that_is_not_an_image_when_send_then_reject_it() {
        MockMultipartFile document = new MockMultipartFile("screenshot", "notes.pdf", "application/pdf", new byte[]{1});
        FeedbackInput input = new FeedbackInput(FeedbackType.BUG, "Look at this", "/home", null, null);

        assertThrows(IllegalArgumentException.class, () -> feedbackService.send(input, document));
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    void given_feedback_when_getAll_then_show_the_sender_by_name_never_by_email() {
        Feedback feedback = Feedback.builder().feedbackId(1L).type(FeedbackType.QUESTION).message("How do I play?").build();
        feedback.setCreatedBy(Account.builder().accountId(4L).username("Ion").email("ion@playquiz.com").build());
        when(feedbackRepository.findAllByOrderByResolvedAscCreatedDateDesc()).thenReturn(List.of(feedback));

        FeedbackEntry entry = feedbackService.getAll().getFirst();

        assertEquals("Ion", entry.from().displayName());
        assertEquals("How do I play?", entry.message());
    }

    @Test
    void given_open_feedback_when_setResolved_then_save_it_resolved() {
        Feedback feedback = Feedback.builder().feedbackId(1L).type(FeedbackType.SUGGESTION).message("Dark mode").build();
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));
        when(feedbackRepository.save(feedback)).thenReturn(feedback);

        FeedbackEntry entry = feedbackService.setResolved(1L, true);

        assertTrue(entry.resolved());
    }

    @Test
    void given_unknown_feedback_when_setResolved_then_not_found() {
        when(feedbackRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> feedbackService.setResolved(9L, true));
        verify(feedbackRepository, never()).save(any());
    }
}
