package com.play.quiz.controller;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_FEEDBACK;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.play.quiz.domain.Feedback;
import com.play.quiz.record.FeedbackEntry;
import com.play.quiz.record.FeedbackInput;
import com.play.quiz.record.FeedbackStatus;
import com.play.quiz.security.FeedbackRateLimiter;
import com.play.quiz.service.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// Sending is open to everyone, guests included; the rest is admin-only (WebSecurity).
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_FEEDBACK)
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackRateLimiter rateLimiter;

    // Open to guests, so capped per sender: their account when signed in, else their address.
    // Answered here rather than thrown: the generic handler would turn it into a 500.
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> send(@Valid @RequestPart(name = "request") final FeedbackInput input,
                                  @RequestPart(required = false) final MultipartFile screenshot,
                                  final Principal principal,
                                  final HttpServletRequest request) {
        String sender = Objects.nonNull(principal) ? principal.getName() : request.getRemoteAddr();
        if (!rateLimiter.tryAcquire(sender)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("You have sent several messages in a short time. Please try again in a few minutes.");
        }
        return ResponseEntity.ok(feedbackService.send(input, screenshot));
    }

    // The picture itself, admin-only like the rest of the list; kept out of the list, which would
    // otherwise carry every screenshot at once.
    @GetMapping(value = "/{feedbackId}/screenshot")
    public ResponseEntity<byte[]> getScreenshot(@PathVariable final Long feedbackId) {
        Feedback feedback = feedbackService.getWithScreenshot(feedbackId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        Optional.ofNullable(feedback.getScreenshotType()).orElse(MediaType.IMAGE_PNG_VALUE)))
                .body(feedback.getScreenshot());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FeedbackEntry>> getAll() {
        return ResponseEntity.ok(feedbackService.getAll());
    }

    // The badge on the admin dashboard link.
    @GetMapping(value = "/open-count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> countOpen() {
        return ResponseEntity.ok(feedbackService.countOpen());
    }

    @PatchMapping(value = "/{feedbackId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeedbackEntry> setResolved(@PathVariable final Long feedbackId,
                                                     @RequestBody final FeedbackStatus status) {
        return ResponseEntity.ok(feedbackService.setResolved(feedbackId, status.resolved()));
    }
}
