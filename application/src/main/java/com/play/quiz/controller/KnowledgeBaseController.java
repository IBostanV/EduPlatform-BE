package com.play.quiz.controller;

import com.play.quiz.dto.KnowledgeBaseRecordDto;
import com.play.quiz.record.KnowledgeBaseArticle;
import com.play.quiz.record.VoteInput;
import com.play.quiz.service.KnowledgeBaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_KNOWLEDGE_BASE;

@RestController
@RequestMapping(value = RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_KNOWLEDGE_BASE)
@RequiredArgsConstructor
public class KnowledgeBaseController {

    private final KnowledgeBaseService knowledgeBaseService;

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KnowledgeBaseRecordDto> save(
            @Valid @RequestPart(name = "request") final KnowledgeBaseRecordDto recordDto,
            @RequestPart(required = false) final MultipartFile attachment) {
        return ResponseEntity.ok(knowledgeBaseService.save(recordDto, attachment));
    }

    @GetMapping(value = "/all-records", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<KnowledgeBaseRecordDto>> getAllRecords() {
        return ResponseEntity.ok(knowledgeBaseService.getAllRecords());
    }

    // Reader-facing: published records only, optionally in one category and/or matching a
    // search over title, content and tags.
    @GetMapping(value = "/records", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<KnowledgeBaseRecordDto>> getPublishedRecords(
            @RequestParam(required = false) final Long categoryId,
            @RequestParam(required = false) final String query) {
        return ResponseEntity.ok(knowledgeBaseService.getPublishedRecords(categoryId, query));
    }

    // Home page "Did you know": today's record, or 204 when nothing is published.
    @GetMapping(value = "/records/daily", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KnowledgeBaseRecordDto> getDailyRecord() {
        return knowledgeBaseService.getDailyRecord()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping(value = "/records/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KnowledgeBaseArticle> getArticle(@PathVariable final Long id) {
        return ResponseEntity.ok(knowledgeBaseService.getArticle(id));
    }

    // "Was this helpful?" Returns the record with its updated counts.
    @PostMapping(value = "/records/{id}/vote", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KnowledgeBaseRecordDto> vote(@PathVariable final Long id, @RequestBody final VoteInput input) {
        return ResponseEntity.ok(knowledgeBaseService.vote(id, input.helpful()));
    }
}
