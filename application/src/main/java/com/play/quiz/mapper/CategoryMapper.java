package com.play.quiz.mapper;

import com.play.quiz.domain.Category;
import com.play.quiz.dto.CategoryDto;
import lombok.SneakyThrows;
import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

    @Mapping(target = "categoryTranslations", ignore = true)
    @Mapping(target = "parentId", source = "parent.catId")
    @Mapping(target = "parentName", source = "parent.name")
    @Mapping(target = "naturalId", source = "name", qualifiedByName = "upperCase")
    @Mapping(target = "hasImage", expression = "java(category.getAttachment() != null)")
    public abstract CategoryDto toDto(final Category category);

    public abstract List<CategoryDto> toDtoList(final List<Category> categories);

    @Mapping(target = "naturalId", source = "name", qualifiedByName = "upperCase")
    @Mapping(target = "parent", source = "parentId", qualifiedByName = "handleParent")
    @Mapping(target = "attachment", expression = "java(handleAttachment(attachment))")
    public abstract Category toEntity(final CategoryDto categoryDto, @Context final MultipartFile attachment);

    @Named("upperCase")
    protected String upperCase(String naturalId) {
        Assert.notNull(naturalId, "Natural id of category cannot be null");
        return naturalId.toUpperCase().replace(" ", "_");
    }

    @Named("handleParent")
    protected Category handleParent(final Long catId) {
        if (Objects.nonNull(catId)) {
            return Category.builder().catId(catId).build();
        }
        return null;
    }

    @SneakyThrows
    @Named("handleAttachment")
    protected byte[] handleAttachment(final MultipartFile attachment) {
        return Objects.nonNull(attachment) ? attachment.getBytes() : null;
    }

    @IterableMapping(qualifiedByName = "shortInfo")
    public abstract List<CategoryDto> toShortDtoList(List<Category> categories);

    @Named("shortInfo")
    @Mapping(target = "visible", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "naturalId", ignore = true)
    @Mapping(target = "attachment", ignore = true)
    @Mapping(target = "parentName", ignore = true)
    @Mapping(target = "categoryTranslations", ignore = true)
    @Mapping(target = "hasImage", expression = "java(question.getAttachment() != null)")
    protected abstract CategoryDto shortInfo(Category question);
}
