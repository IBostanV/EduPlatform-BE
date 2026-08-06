package com.play.quiz.record;

import com.play.quiz.dto.KnowledgeBaseRecordDto;

import java.util.List;

// One article for the reader: the record, the topic it sits under (if any) and its sub-articles.
public record KnowledgeBaseArticle(KnowledgeBaseRecordDto record,
                                   KnowledgeBaseRecordDto parent,
                                   List<KnowledgeBaseRecordDto> children) {}
