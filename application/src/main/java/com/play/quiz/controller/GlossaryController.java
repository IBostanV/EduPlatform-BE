package com.play.quiz.controller;

import com.play.quiz.domain.GlossaryType;
import com.play.quiz.dto.GlossaryDto;
import com.play.quiz.dto.GlossaryTypeDto;
import com.play.quiz.service.GlossaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_GLOSSARY;

@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_GLOSSARY)
@RequiredArgsConstructor
public class GlossaryController {

    private final GlossaryService glossaryService;

    @GetMapping(value = "/id/{glossaryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GlossaryDto> getGlossaryById(@PathVariable final Long glossaryId) {
        return ResponseEntity.ok().body(glossaryService.getById(glossaryId));
    }

    @GetMapping(value = "/key/{glossaryKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GlossaryDto> getGlossaryByKey(@PathVariable String glossaryKey) {
        return ResponseEntity.ok().body(glossaryService.getByKey(glossaryKey));
    }

    @GetMapping(value = "/category/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GlossaryDto>> getGlossariesByCategory(@PathVariable final Long categoryId) {
        return ResponseEntity.ok().body(glossaryService.getByCategoryId(categoryId));
    }

    @PatchMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlossaryDto> saveGlossary(@Valid @RequestPart(name = "request") final GlossaryDto glossaryDto,
                                                    @RequestPart(required = false) final MultipartFile attachment) {
        return ResponseEntity.ok(glossaryService.save(glossaryDto, attachment));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> toggleGlossary(@RequestParam final Long id) {
        return ResponseEntity.ok(glossaryService.toggleGlossary(id));
    }

    @PostMapping(value = "/save-type", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GlossaryType> saveGlossaryType(@RequestBody GlossaryType glossaryType) {
        return ResponseEntity.ok(glossaryService.saveGlossaryType(glossaryType));
    }

    @GetMapping(value = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GlossaryTypeDto>> getGlossaryTypes() {
        return ResponseEntity.ok(glossaryService.getGlossaryTypes());
    }

    // The badge on the content dashboard's Glossaries entry: terms still without a type. Their
    // questions have nothing plausible to draw wrong options from, so they go out short.
    @GetMapping(value = "/missing-type-count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> countMissingType() {
        return ResponseEntity.ok(glossaryService.countWithoutType());
    }

    @PutMapping(value = "/types/{typeId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GlossaryType> updateGlossaryType(@PathVariable final Long typeId, @RequestBody final GlossaryType changes) {
        return ResponseEntity.ok(glossaryService.updateGlossaryType(typeId, changes));
    }

    // 400 with a readable reason while glossaries still use the type.
    @DeleteMapping("/types/{typeId}")
    public ResponseEntity<Void> deleteGlossaryType(@PathVariable final Long typeId) {
        glossaryService.deleteGlossaryType(typeId);
        return ResponseEntity.noContent().build();
    }

    // 400 with a readable reason while questions or child glossaries still use the term.
    @DeleteMapping("/{glossaryId}")
    public ResponseEntity<Void> deleteGlossary(@PathVariable final Long glossaryId) {
        glossaryService.deleteGlossary(glossaryId);
        return ResponseEntity.noContent().build();
    }
}
