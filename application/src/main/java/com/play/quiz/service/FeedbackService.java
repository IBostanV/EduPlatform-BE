package com.play.quiz.service;

import java.util.List;

import com.play.quiz.domain.Feedback;
import com.play.quiz.record.FeedbackEntry;
import com.play.quiz.record.FeedbackInput;
import org.springframework.web.multipart.MultipartFile;

/** Messages players send the admins: sent by anyone signed in, read and resolved by admins. */
public interface FeedbackService {

    FeedbackEntry send(final FeedbackInput input, final MultipartFile screenshot);

    /** The picture attached to one message, for the admins to look at. */
    Feedback getWithScreenshot(final Long feedbackId);

    List<FeedbackEntry> getAll();

    long countOpen();

    FeedbackEntry setResolved(final Long feedbackId, boolean resolved);
}
