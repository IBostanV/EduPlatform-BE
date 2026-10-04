package com.play.quiz.review;

import com.play.quiz.controller.RestEndpoint;
import com.play.quiz.dto.QuizDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The mistakes deck. Needs an account, which the security chain's catch-all rule asks for. */
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_REVIEW)
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewService.Status> status() {
        return ResponseEntity.ok(reviewService.status());
    }

    @GetMapping(value = "/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizDto> quiz() {
        return ResponseEntity.ok(reviewService.quiz());
    }
}
