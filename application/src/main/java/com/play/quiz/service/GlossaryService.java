package com.play.quiz.service;

import java.util.List;

import com.play.quiz.domain.Glossary;
import com.play.quiz.domain.GlossaryType;
import com.play.quiz.dto.GlossaryDto;
import com.play.quiz.dto.GlossaryTypeDto;
import org.springframework.web.multipart.MultipartFile;

public interface GlossaryService {

    GlossaryDto save(final GlossaryDto glossaryDto, final MultipartFile attachment);

    GlossaryDto getById(final Long glossaryId);

    GlossaryDto getByKey(String glossaryKey);

    List<GlossaryDto> getByCategoryId(final Long categoryId);

    Integer toggleGlossary(final Long glossaryId);

    GlossaryType saveGlossaryType(final GlossaryType glossaryType);

    GlossaryType updateGlossaryType(final Long typeId, final GlossaryType changes);

    void deleteGlossaryType(final Long typeId);

    void deleteGlossary(final Long glossaryId);

    List<GlossaryTypeDto> getGlossaryTypes();

    /** How many terms have no type, which is a gap the content dashboard shows as a badge. */
    long countWithoutType();

    Glossary getEntityById(final Long glossaryId);
}
