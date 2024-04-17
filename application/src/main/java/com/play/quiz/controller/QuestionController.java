package com.play.quiz.controller;

import com.play.quiz.domain.Category;
import com.play.quiz.dto.AnswerDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.enums.QuestionAttribute;
import com.play.quiz.enums.QuestionType;
import com.play.quiz.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping(value = "/deactivate/{questionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deactivate(@PathVariable final Long questionId) {
        questionService.deactivate(questionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/all-questions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionDto>> getQuestions() {
        return ResponseEntity.ok(questionService.findAll());
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

    @GetMapping(value = "/question-with-options/{questionId}")
    public ResponseEntity<QuestionDto> getQuestionWithOptions(@PathVariable("questionId") final Long questionId) {
        return ResponseEntity.ok(questionService.getQuestionWithOptions(questionId));
    }
}
