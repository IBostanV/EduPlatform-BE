package com.play.quiz.controller;

import com.play.quiz.domain.Category;
import com.play.quiz.dto.AnswerDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.enums.QuestionAttribute;
import com.play.quiz.enums.QuestionType;
import com.play.quiz.record.MiniGameResult;
import com.play.quiz.record.PageResponse;
import com.play.quiz.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_QUESTION;

@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_QUESTION)
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDto> saveQuestion(@Valid @RequestBody final QuestionDto questionDto) {
        return ResponseEntity.ok(questionService.save(questionDto));
    }

    // Edit the question's own fields; answers and translations are kept as they are.
    @PutMapping(value = "/{questionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDto> updateQuestion(@PathVariable final Long questionId, @RequestBody final QuestionDto changes) {
        return ResponseEntity.ok(questionService.update(questionId, changes));
    }

    // Deletes the question with its answers and translations.
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable final Long questionId) {
        questionService.delete(questionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/deactivate/{questionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deactivate(@PathVariable final Long questionId) {
        questionService.deactivate(questionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/all-questions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionDto>> getQuestions() {
        return ResponseEntity.ok(questionService.findAll());
    }

    // One page of questions, newest first, with totals for the admin pager. `page` is 0-based;
    // `query` optionally searches question text, topic and category name; `sort` (a column
    // name, e.g. complexityLevel) with `direction` asc|desc orders it, newest first otherwise.
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<QuestionDto>> getQuestionPage(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size,
            @RequestParam(required = false) final String query,
            @RequestParam(required = false) final String sort,
            @RequestParam(required = false) final String direction) {
        return ResponseEntity.ok(questionService.findPage(page, size, query, sort, direction));
    }

    @GetMapping(value = "/category/{category}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionDto>> getCategoryQuestions(@PathVariable final Category category) {
        return ResponseEntity.ok(questionService.findByCategory(category));
    }

    @PostMapping(value = "/generator/template", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionDto>> generateFromTemplate(@Valid @RequestBody final QuestionDto questionDto) {
        return ResponseEntity.ok(questionService.generateFromTemplate(questionDto));
    }

    @GetMapping(value = "/question-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionType[]> getQuestionTypes() {
        return ResponseEntity.ok(QuestionType.values());
    }

    @GetMapping(value = "/question-attributes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionAttribute[]> getQuestionAttributes() {
        return ResponseEntity.ok(QuestionAttribute.values());
    }

    @GetMapping(value = "/fetch-answers/{questionId}")
    public ResponseEntity<List<AnswerDto>> checkAnswers(@PathVariable Long questionId) {
        return ResponseEntity.ok(questionService.getAnswers(questionId));
    }

    // Home page mini game, open to everyone: a random question with its options (204 if there
    // is none), and the check of a pick.
    @GetMapping(value = "/mini-game", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDto> getMiniGameQuestion() {
        return questionService.getMiniGameQuestion()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping(value = "/mini-game/{questionId}/check", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MiniGameResult> checkMiniGameAnswer(@PathVariable final Long questionId,
                                                              @RequestBody final AnswerDto choice) {
        return ResponseEntity.ok(questionService.checkMiniGameAnswer(questionId, choice));
    }

    @GetMapping(value = "/question-with-options/{questionId}")
    public ResponseEntity<QuestionDto> getQuestionWithOptions(@PathVariable("questionId") final Long questionId) {
        return ResponseEntity.ok(questionService.getQuestionWithAnswerOptions(questionId));
    }
}
