package com.play.quiz.controller;

import com.play.quiz.dto.CategoryDto;
import com.play.quiz.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_CATEGORY;

@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_CATEGORY)
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> getCategory(@RequestParam(required = false) Long id,
                                                   @RequestParam(required = false) String naturalId) {
        return ResponseEntity.ok(categoryService.getById(id, naturalId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> saveCategory(@Valid @RequestPart(name = "request") final CategoryDto requestCategory,
                                                    @RequestPart(required = false) final MultipartFile attachment) {
        return ResponseEntity.ok(categoryService.save(requestCategory, attachment));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable final Long categoryId) {
        categoryService.deleteById(categoryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/all-categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @GetMapping(value = "/all-categories-short", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDto>> getAllCategoriesShort() {
        return ResponseEntity.ok(categoryService.getCategoriesShort());
    }
}
