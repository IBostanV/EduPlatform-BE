package com.play.quiz.service.impl;

import java.util.List;

import com.play.quiz.domain.Category;
import com.play.quiz.dto.CategoryDto;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.mapper.CategoryMapper;
import com.play.quiz.repository.CategoryRepository;
import com.play.quiz.service.CategoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    public static final String CAT_ID = "catId";

    @Override
    public CategoryDto save(final CategoryDto categoryDto, MultipartFile attachment) {
        Category category = categoryMapper.toEntity(categoryDto, attachment);
        Category entity = categoryRepository.save(category);
        return categoryMapper.toDto(entity);
    }

    @NonNull
    @Override
    public CategoryDto getById(final Long categoryId, final String naturalId) {
        Category category = categoryRepository.findById(categoryId)
                .or(() -> categoryRepository.findByNaturalId(naturalId))
                .orElseThrow(() -> new RecordNotFoundException(
                        "No records found by category id: " + categoryId + " or natural id " + naturalId));
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDto> getCategories() {
        Sort sort = Sort.by(Sort.Direction.ASC, CAT_ID);
        List<Category> categories = categoryRepository.findAllActive(sort);
        return categoryMapper.toDtoList(categories);
    }

    @Override
    public List<CategoryDto> getCategoriesShort() {
        Sort sort = Sort.by(Sort.Direction.ASC, CAT_ID);
        List<Category> categories = categoryRepository.findAllActive(sort);
        return categoryMapper.toShortDtoList(categories);
    }

    @Override
    public void deleteById(final Long categoryId) {
        log.info("Deleting category with id: {}", categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
