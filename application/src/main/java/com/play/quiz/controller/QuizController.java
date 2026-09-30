package com.play.quiz.controller;

import static com.play.quiz.controller.RestEndpoint.QUIZ_CUSTOM;
import static com.play.quiz.controller.RestEndpoint.QUIZ_TYPES;
import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_QUIZ;

import com.play.quiz.domain.QuizType;
import com.play.quiz.dto.CustomQuizDto;
import com.play.quiz.dto.CustomQuizPlayDto;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.dto.UserQuizParams;
import com.play.quiz.record.CustomQuizSummary;
import com.play.quiz.record.QuizInvitation;
import com.play.quiz.service.CustomQuizService;
import com.play.quiz.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_QUIZ)
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
    private final CustomQuizService customQuizService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizDto> createQuiz(@RequestBody final QuizDto quizDto) {
        return ResponseEntity.ok(quizService.create(quizDto));
    }

    @PostMapping(value = QUIZ_CUSTOM, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizDto> createCustomQuiz(@Valid @RequestBody final CustomQuizDto customQuizDto) {
        return ResponseEntity.ok(customQuizService.create(customQuizDto));
    }

    // A literal path, so it wins over the /{quizId} one below.
    @GetMapping(value = QUIZ_CUSTOM + "/invitations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuizInvitation>> getMyInvitations() {
        return ResponseEntity.ok(customQuizService.getMyInvitations());
    }

    @GetMapping(value = QUIZ_CUSTOM + "/mine", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomQuizSummary>> getMyQuizzes() {
        return ResponseEntity.ok(customQuizService.getMyQuizzes());
    }

    // Admin-only (WebSecurity): every custom quiz, for moderating what players write.
    @GetMapping(value = QUIZ_CUSTOM + "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomQuizSummary>> getAllCustomQuizzes() {
        return ResponseEntity.ok(customQuizService.getAllCustomQuizzes());
    }

    // Its creator or an admin; takes its questions, its invitations and every run of it with it.
    @DeleteMapping(value = QUIZ_CUSTOM + "/{quizId}")
    public ResponseEntity<Void> deleteCustomQuiz(@PathVariable final Long quizId) {
        customQuizService.delete(quizId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = QUIZ_CUSTOM + "/{quizId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomQuizPlayDto> getCustomQuiz(@PathVariable final Long quizId) {
        return ResponseEntity.ok(customQuizService.getForPlay(quizId));
    }

    @GetMapping(value = "/{quizId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizDto> getQuiz(@PathVariable final Long quizId) {
        return ResponseEntity.ok(quizService.getById(quizId));
    }

    @GetMapping(value = "/express", produces = MediaType.APPLICATION_JSON_VALUE)
    // Principal is null for a guest, who gets general knowledge only.
    public ResponseEntity<QuizDto> getExpressQuiz(final Principal principal) {
        return ResponseEntity.ok(quizService.getExpressQuiz(Objects.isNull(principal) ? null : principal.getName()));
    }

    @GetMapping(value = "/categorized/{catId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizDto> getExpressQuiz(@PathVariable Long catId, UserQuizParams userQuizParams) {
        return ResponseEntity.ok(quizService.getQuizByCategoryAndParams(catId, userQuizParams));
    }

    @GetMapping(value = QUIZ_TYPES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuizType>> getQuizTypes() {
        return ResponseEntity.ok(quizService.getQuizTypes());
    }
}
