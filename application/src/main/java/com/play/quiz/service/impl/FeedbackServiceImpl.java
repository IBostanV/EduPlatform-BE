package com.play.quiz.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.play.quiz.domain.Feedback;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.record.FeedbackEntry;
import com.play.quiz.record.FeedbackInput;
import com.play.quiz.record.UserSummary;
import com.play.quiz.repository.FeedbackRepository;
import com.play.quiz.service.FeedbackService;
import com.play.quiz.util.ServerText;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    // The sender (none for a guest) and the time are filled in by the audit aspect on save.
    @Override
    @Transactional
    public FeedbackEntry send(final FeedbackInput input, final MultipartFile screenshot) {
        Feedback saved = feedbackRepository.save(Feedback.builder()
                .screenshot(screenshotBytes(screenshot))
                .screenshotType(hasScreenshot(screenshot) ? screenshot.getContentType() : null)
                .type(input.type())
                .message(input.message().trim())
                .page(StringUtils.hasText(input.page()) ? input.page().trim() : null)
                .question(StringUtils.hasText(input.question()) ? input.question().trim() : null)
                .contactEmail(StringUtils.hasText(input.contactEmail()) ? input.contactEmail().trim() : null)
                .build());
        log.info("Saved feedback id: {}, type: {}, screenshot: {}",
                saved.getFeedbackId(), saved.getType(), Objects.nonNull(saved.getScreenshot()));
        return toEntry(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Feedback getWithScreenshot(final Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .filter(feedback -> Objects.nonNull(feedback.getScreenshot()))
                .orElseThrow(() -> new RecordNotFoundException("No screenshot on feedback: " + feedbackId));
    }

    // Only a picture, and only as big as the upload limit allows (application.yml: 2MB).
    private static byte[] screenshotBytes(final MultipartFile screenshot) {
        if (!hasScreenshot(screenshot)) {
            return null;
        }
        if (!String.valueOf(screenshot.getContentType()).startsWith("image/")) {
            throw new IllegalArgumentException(ServerText.t("err_screenshot_not_image", "A screenshot has to be an image"));
        }
        try {
            return screenshot.getBytes();
        } catch (IOException exception) {
            throw new IllegalArgumentException(ServerText.t("err_screenshot_unreadable", "Could not read the screenshot"), exception);
        }
    }

    private static boolean hasScreenshot(final MultipartFile screenshot) {
        return Objects.nonNull(screenshot) && !screenshot.isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackEntry> getAll() {
        return feedbackRepository.findAllByOrderByResolvedAscCreatedDateDesc().stream()
                .map(FeedbackServiceImpl::toEntry)
                .toList();
    }

    @Override
    public long countOpen() {
        return feedbackRepository.countByResolvedFalse();
    }

    @Override
    @Transactional
    public FeedbackEntry setResolved(final Long feedbackId, boolean resolved) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RecordNotFoundException("No feedback with id: " + feedbackId));
        feedback.setResolved(resolved);
        log.info("Feedback id: {} marked resolved: {}", feedbackId, resolved);
        // save(), not just the dirty entity: the audit aspect records the admin as UPDATED_BY.
        return toEntry(feedbackRepository.save(feedback));
    }

    private static FeedbackEntry toEntry(final Feedback feedback) {
        return new FeedbackEntry(
                feedback.getFeedbackId(),
                feedback.getType(),
                feedback.getMessage(),
                feedback.getPage(),
                feedback.getQuestion(),
                Optional.ofNullable(feedback.getCreatedBy()).map(UserSummary::of).orElse(null),
                feedback.getContactEmail(),
                feedback.getCreatedDate(),
                feedback.isResolved(),
                Objects.nonNull(feedback.getScreenshot()));
    }
}
