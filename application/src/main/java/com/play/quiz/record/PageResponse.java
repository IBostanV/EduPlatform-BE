package com.play.quiz.record;

import org.springframework.data.domain.Page;

import java.util.List;

// One page of a list plus what a pager needs. Returned instead of Spring's Page, whose JSON
// shape is not a stable contract (Spring Data warns when PageImpl is serialised directly).
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <T> PageResponse<T> of(Page<?> page, List<T> content) {
        return new PageResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
