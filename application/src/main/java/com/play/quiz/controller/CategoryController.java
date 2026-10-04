package com.play.quiz.controller;

import com.play.quiz.dto.CategoryDto;
import com.play.quiz.repository.QuestionRepository;
import com.play.quiz.service.CategoryService;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URLConnection;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_CATEGORY;

@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_CATEGORY)
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final QuestionRepository questionRepository;

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

    // Edit: name, parent, visibility; the image only changes when a new one is sent.
    @PutMapping(value = "/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable final Long categoryId,
                                                      @RequestPart(name = "request") final CategoryDto changes,
                                                      @RequestPart(required = false) final MultipartFile attachment) {
        return ResponseEntity.ok(categoryService.update(categoryId, changes, attachment));
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

    // Hidden categories too: the content dashboard edits them. Content roles only (WebSecurity).
    @GetMapping(value = "/manage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDto>> getCategoriesForManagement() {
        return ResponseEntity.ok(categoryService.getAllCategoriesForManagement());
    }

    // The picture on its own, for an <img>: the browser loads them side by side and keeps them,
    // where a list carrying every picture as base64 is megabytes on every visit. The ETag lets a
    // repeat visit get a 304; the hour of max-age is how long a replaced picture can look stale.
    @GetMapping("/{categoryId}/image")
    public ResponseEntity<byte[]> getCategoryImage(@PathVariable final Long categoryId, final WebRequest request) {
        byte[] image = categoryService.getImage(categoryId);
        String etag = "\"" + Integer.toHexString(Arrays.hashCode(image)) + "\"";
        if (request.checkNotModified(etag)) {
            return null;
        }
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofHours(1)).cachePublic())
                .eTag(etag)
                .contentType(imageType(image))
                .body(image);
    }

    // Uploads are stored as sent, with no type beside them. The JDK knows JPEG, PNG and GIF by
    // their first bytes; anything else goes out as JPEG, which is what the pages have always
    // labelled these as, and browsers draw an image by its content anyway.
    @SneakyThrows
    private static MediaType imageType(final byte[] image) {
        String type = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(image));
        return type == null ? MediaType.IMAGE_JPEG : MediaType.parseMediaType(type);
    }

    /**
     * The categories with at least one question a quiz of this type (its bit value; 0 or none for
     * any) can ask at this difficulty (the quiz's own complexityFrom/To; none for any). Only their
     * own questions: a parent with none is still playable through its children, which the page
     * works out from the tree.
     */
    @GetMapping(value = "/with-questions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Long>> getCategoryIdsWithQuestions(@RequestParam(required = false) final Long quizType,
                                                                  @RequestParam(required = false) final Integer complexityFrom,
                                                                  @RequestParam(required = false) final Integer complexityTo) {
        // A native query's ids come back as whatever number type the driver picks.
        return ResponseEntity.ok(questionRepository.findCategoryIdsWithQuestionsFor(quizType, complexityFrom, complexityTo).stream()
                .map(Number::longValue).toList());
    }

    @GetMapping(value = "/all-categories-short", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDto>> getAllCategoriesShort() {
        return ResponseEntity.ok(categoryService.getCategoriesShort());
    }
}
