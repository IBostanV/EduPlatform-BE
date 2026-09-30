package com.play.quiz.service.impl;

import com.play.quiz.domain.Glossary;
import com.play.quiz.domain.GlossaryType;
import com.play.quiz.dto.GlossaryDto;
import com.play.quiz.dto.GlossaryTypeDto;
import com.play.quiz.exception.EntityNotUpdatedException;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.mapper.GlossaryMapper;
import com.play.quiz.mapper.GlossaryTypeMapper;
import com.play.quiz.repository.AnswerRepository;
import com.play.quiz.repository.GlossaryRepository;
import com.play.quiz.repository.GlossaryTypeRepository;
import com.play.quiz.service.GlossaryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.play.quiz.util.Constant.SAVE_OPERATION_SUCCESSFULLY;

@Log4j2
@Service
@RequiredArgsConstructor
public class GlossaryServiceImpl implements GlossaryService {

    private final GlossaryMapper glossaryMapper;
    private final GlossaryRepository glossaryRepository;
    private final GlossaryTypeMapper glossaryTypeMapper;
    private final GlossaryTypeRepository glossaryTypeRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public GlossaryDto save(final GlossaryDto glossaryDto, final MultipartFile attachment) {
        Glossary glossary = glossaryMapper.toEntity(glossaryDto, attachment);

        if (Objects.nonNull(attachment) || Objects.isNull(glossaryDto.getTermId())) {
            Glossary entity = glossaryRepository.save(glossary);
            log.info("Saved glossary id: {}, key: {}, with attachment: {}", entity.getTermId(), entity.getKey(), Objects.nonNull(attachment));
            return glossaryMapper.toDto(entity);
        }

        return saveWithoutAttachment(glossary);
    }

    private GlossaryDto saveWithoutAttachment(final Glossary glossary) {
        int savedWithoutAttachmentResult = glossaryRepository.saveWithoutAttachment(glossary);

        if (!Objects.equals(savedWithoutAttachmentResult, SAVE_OPERATION_SUCCESSFULLY)) {
            log.warn("Glossary id: {} not updated, rows affected: {}", glossary.getTermId(), savedWithoutAttachmentResult);
            throw new EntityNotUpdatedException("Exception during glossary saving");
        }

        log.info("Updated glossary id: {} keeping its attachment", glossary.getTermId());
        return glossaryMapper.toDto(glossaryRepository.getReferenceById(glossary.getTermId()));
    }

    @Override
    public GlossaryDto getById(final Long glossaryId) {
        Glossary glossary = glossaryRepository.findById(glossaryId)
                .orElseThrow(() -> new RecordNotFoundException("No records found by glossary id: " + glossaryId));
        return glossaryMapper.toDto(glossary);
    }

    @Override
    public GlossaryDto getByKey(String glossaryKey) {
        Glossary glossary = glossaryRepository.findByKey(glossaryKey)
                .orElseThrow(() -> new RecordNotFoundException("No records found by key: " + glossaryKey));
        return glossaryMapper.toDto(glossary);
    }

    @Override
    @Transactional
    public List<GlossaryDto> getByCategoryId(final Long categoryId) {
        List<Glossary> glossaryList = glossaryRepository.findHierarchicalByCategoryId(categoryId)
                .orElseThrow(() -> new RecordNotFoundException("No glossary found by category id: " + categoryId));
        return glossaryMapper.toDto(glossaryList);
    }

    @NonNull
    @Override
    @Transactional
    public Integer toggleGlossary(final Long glossaryId) {
        Integer updated = glossaryRepository.toggleGlossary(glossaryId);
        log.info("Toggled glossary id: {}, rows affected: {}", glossaryId, updated);
        return updated;
    }

    @Override
    public GlossaryType saveGlossaryType(final GlossaryType glossaryType) {
        GlossaryType saved = glossaryTypeRepository.save(glossaryType);
        log.info("Saved glossary type id: {}, name: {}", saved.getId(), saved.getName());
        return saved;
    }

    // Loads and copies the stored type, so its created date and anything else not edited stay.
    @Override
    @Transactional
    public GlossaryType updateGlossaryType(final Long typeId, final GlossaryType changes) {
        GlossaryType type = glossaryTypeRepository.findById(typeId)
                .orElseThrow(() -> new RecordNotFoundException("No glossary type with id: " + typeId));
        GlossaryType saved = glossaryTypeRepository.save(type.toBuilder()
                .name(changes.getName())
                .options(changes.getOptions())
                .isActive(changes.getIsActive())
                .build());
        log.info("Updated glossary type id: {}, name: {}, active: {}", typeId, saved.getName(), saved.getIsActive());
        return saved;
    }

    // Refused while glossaries use it, with a message the admin can act on, rather than the
    // database's foreign-key error.
    @Override
    @Transactional
    public void deleteGlossaryType(final Long typeId) {
        long usedBy = glossaryTypeRepository.countGlossariesUsing(typeId);
        if (usedBy > 0) {
            throw new IllegalArgumentException("This type is used by " + usedBy
                    + " glossar" + (usedBy == 1 ? "y" : "ies") + ". Change their type first.");
        }
        glossaryTypeRepository.deleteById(typeId);
        log.info("Deleted glossary type id: {}", typeId);
    }

    // Same idea: questions answered by this term, or terms nested under it, keep it in place.
    // Its translations go with it (cascade).
    @Override
    @Transactional
    public void deleteGlossary(final Long glossaryId) {
        long answers = answerRepository.countUsingGlossary(glossaryId);
        if (answers > 0) {
            throw new IllegalArgumentException("This glossary is the answer to " + answers
                    + " question" + (answers == 1 ? "" : "s") + ". Delete or change those questions first.");
        }
        long children = glossaryRepository.countChildren(glossaryId);
        if (children > 0) {
            throw new IllegalArgumentException("This glossary is the parent of " + children
                    + " other glossar" + (children == 1 ? "y" : "ies") + ". Move or delete them first.");
        }
        glossaryRepository.deleteById(glossaryId);
        log.info("Deleted glossary id: {}", glossaryId);
    }

    @Override
    public List<GlossaryTypeDto> getGlossaryTypes() {
        List<GlossaryType> glossaryTypeList = glossaryTypeRepository.findAll();
        return glossaryTypeMapper.toDtoList(glossaryTypeList);
    }

    @Override
    @Transactional(readOnly = true)
    public long countWithoutType() {
        return glossaryRepository.countByTypeIsNull();
    }

    @Override
    public Glossary getEntityById(final Long glossaryId) {
        return glossaryRepository.getReferenceById(glossaryId);
    }
}
