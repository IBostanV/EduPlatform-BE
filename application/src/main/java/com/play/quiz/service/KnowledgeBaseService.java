package com.play.quiz.service;

import java.util.List;
import java.util.Optional;

import com.play.quiz.dto.KnowledgeBaseRecordDto;
import com.play.quiz.record.KnowledgeBaseArticle;
import org.springframework.web.multipart.MultipartFile;

public interface KnowledgeBaseService {

    KnowledgeBaseRecordDto save(KnowledgeBaseRecordDto knowledgeBaseRecordDto, MultipartFile attachment);

    List<KnowledgeBaseRecordDto> getAllRecords();

    List<KnowledgeBaseRecordDto> getPublishedRecords(Long categoryId, String query);

    KnowledgeBaseArticle getArticle(Long id);

    Optional<KnowledgeBaseRecordDto> getDailyRecord();

    KnowledgeBaseRecordDto vote(Long id, boolean helpful);

    /** An article to read for each of these categories that has one: its most helpful. */
    java.util.Map<Long, ArticleLink> bestArticles(java.util.Collection<Long> categoryIds);

    record ArticleLink(Long id, String title) {}
}
