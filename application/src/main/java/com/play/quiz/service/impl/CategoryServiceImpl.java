package com.play.quiz.service.impl;

import java.util.List;
import java.util.Objects;

import com.play.quiz.domain.Category;
import com.play.quiz.dto.CategoryDto;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.mapper.CategoryMapper;
import com.play.quiz.repository.CategoryRepository;
import com.play.quiz.service.CategoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * Changes name, parent, visibility and (only when a new file comes) the image. Loads the
     * stored category and copies it with just those fields replaced, so everything the form does
     * not send (natural id, created date, translations) stays as it is. save(dto) would null them.
     */
    @Override
    @Transactional
    public CategoryDto update(final Long categoryId, final CategoryDto changes, final MultipartFile attachment) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RecordNotFoundException("No records found by category id: " + categoryId));
        if (Objects.equals(changes.getParentId(), categoryId)) {
            throw new IllegalArgumentException("A category cannot be its own parent");
        }

        Category updated = category.toBuilder()
                .name(changes.getName())
                .visible(changes.getVisible())
                .parent(changes.getParentId() == null ? null : Category.builder().catId(changes.getParentId()).build())
                .attachment(attachment == null ? category.getAttachment() : readBytes(attachment))
                .build();

        return categoryMapper.toDto(categoryRepository.save(updated));
    }

    @SneakyThrows
    private static byte[] readBytes(final MultipartFile file) {
        return file.getBytes();
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
    public byte[] getImage(final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(Category::getAttachment)
                .orElseThrow(() -> new RecordNotFoundException("No image for category id: " + categoryId));
    }

    @Override
    public void deleteById(final Long categoryId) {
        log.info("Deleting category with id: {}", categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
