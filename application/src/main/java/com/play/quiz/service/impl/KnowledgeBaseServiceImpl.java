package com.play.quiz.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.play.quiz.domain.KnowledgeBaseRecord;
import com.play.quiz.dto.KnowledgeBaseRecordDto;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.mapper.KnowledgeBaseMapper;
import com.play.quiz.record.KnowledgeBaseArticle;
import com.play.quiz.repository.KnowledgeBaseRepository;
import com.play.quiz.service.KnowledgeBaseService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

    // The editor's formatting (headings, lists, links, images, code…) survives; scripts, event
    // handlers and javascript: URLs do not.
    private static final Safelist ARTICLE_HTML = Safelist.relaxed();

    private final KnowledgeBaseMapper knowledgeBaseMapper;
    private final KnowledgeBaseRepository knowledgeBaseRepository;

    @Override
    public KnowledgeBaseRecordDto save(KnowledgeBaseRecordDto knowledgeBaseRecordDto, MultipartFile attachment) {
        knowledgeBaseRecordDto.setContent(sanitize(knowledgeBaseRecordDto.getContent()));
        KnowledgeBaseRecord entity = knowledgeBaseMapper.toEntity(knowledgeBaseRecordDto, attachment);
        KnowledgeBaseRecord knowledgeBaseRecord = knowledgeBaseRepository.save(entity);
        return knowledgeBaseMapper.toDto(knowledgeBaseRecord);
    }

    @Override
    public List<KnowledgeBaseRecordDto> getAllRecords() {
        List<KnowledgeBaseRecord> knowledgeBaseRecords = knowledgeBaseRepository.findAll();
        return knowledgeBaseMapper.toDto(knowledgeBaseRecords);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KnowledgeBaseRecordDto> getPublishedRecords(Long categoryId, String query) {
        String pattern = StringUtils.hasText(query) ? "%" + query.trim().toLowerCase(Locale.ROOT) + "%" : null;
        return knowledgeBaseRepository.findPublished(categoryId, pattern).stream()
                .map(this::toPublicDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public KnowledgeBaseArticle getArticle(Long id) {
        KnowledgeBaseRecord record = findPublished(id);
        // A hidden or draft parent is simply not shown, rather than leaking it.
        KnowledgeBaseRecordDto parent = record.getParent() == null ? null
                : knowledgeBaseRepository.findPublishedById(record.getParent().getId()).map(this::toPublicDto).orElse(null);
        List<KnowledgeBaseRecordDto> children = knowledgeBaseRepository.findPublishedChildren(id).stream()
                .map(this::toPublicDto)
                .toList();

        return new KnowledgeBaseArticle(toPublicDto(record), parent, children);
    }

    // Anonymous, like reading. One vote per reader is kept by the client; the counters are
    // a helpfulness signal, not a ballot.
    @Override
    @Transactional
    public KnowledgeBaseRecordDto vote(Long id, boolean helpful) {
        findPublished(id);
        if (helpful) {
            knowledgeBaseRepository.upvote(id);
        } else {
            knowledgeBaseRepository.downvote(id);
        }
        // The bulk UPDATE bypassed the persistence context, so read the new counts afresh.
        return knowledgeBaseRepository.findPublishedById(id).map(this::toPublicDto).orElseThrow();
    }

    private KnowledgeBaseRecord findPublished(Long id) {
        return knowledgeBaseRepository.findPublishedById(id)
                .orElseThrow(() -> new RecordNotFoundException("No knowledge base article with id: " + id));
    }

    // Sanitised on the way out too, so rows saved before sanitising existed are safe to render.
    // "Did you know": the same record all day for everyone, a different one the next day,
    // cycling through every published record. Adding or hiding records reshuffles the cycle.
    @Override
    @Transactional(readOnly = true)
    public Optional<KnowledgeBaseRecordDto> getDailyRecord() {
        List<Long> ids = knowledgeBaseRepository.findPublishedIds();
        if (ids.isEmpty()) {
            return Optional.empty();
        }
        Long id = ids.get((int) Math.floorMod(LocalDate.now().toEpochDay(), (long) ids.size()));
        return knowledgeBaseRepository.findPublishedById(id).map(this::toPublicDto);
    }

    private KnowledgeBaseRecordDto toPublicDto(KnowledgeBaseRecord record) {
        KnowledgeBaseRecordDto dto = knowledgeBaseMapper.toDto(record);
        dto.setContent(sanitize(dto.getContent()));
        return dto;
    }

    private static String sanitize(String html) {
        return html == null ? null : Jsoup.clean(html, ARTICLE_HTML);
    }
}
