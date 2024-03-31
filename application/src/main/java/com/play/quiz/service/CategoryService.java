package com.play.quiz.service;

import java.util.List;

import com.play.quiz.dto.CategoryDto;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService {

    CategoryDto save(final CategoryDto category, MultipartFile avatar);

    CategoryDto getById(final Long categoryId, final String naturalId);

    void deleteById(final Long categoryId);

    List<CategoryDto> getCategories();

    List<CategoryDto> getCategoriesShort();
}
