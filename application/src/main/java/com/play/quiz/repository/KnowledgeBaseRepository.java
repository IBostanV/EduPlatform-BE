package com.play.quiz.repository;

import com.play.quiz.domain.KnowledgeBaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface KnowledgeBaseRepository extends JpaRepository<KnowledgeBaseRecord, Long> {

    // "Published" = what readers may see: not switched off, and ACTIVE. A null status also
    // counts, because the admin form has never sent one, so every existing record has none.
    String PUBLISHED = """
             (r.visible IS NULL OR r.visible = true)
             AND (r.status IS NULL OR r.status = com.play.quiz.enums.KnowledgeBaseRecordStatus.ACTIVE)
            """;

    // `query` is already lower-cased and wrapped in %…% by the service, or null for no search.
    // Most helpful first, then newest.
    @Query("SELECT r FROM KnowledgeBaseRecord r WHERE" + PUBLISHED + """
            AND (:categoryId IS NULL OR r.category.catId = :categoryId)
            AND (:query IS NULL
                 OR LOWER(r.title) LIKE :query
                 OR LOWER(r.content) LIKE :query
                 OR LOWER(r.tags) LIKE :query)
            ORDER BY r.upvotes DESC NULLS LAST, r.createdDate DESC
            """)
    List<KnowledgeBaseRecord> findPublished(Long categoryId, String query);

    @Query("SELECT r.id FROM KnowledgeBaseRecord r WHERE" + PUBLISHED + " ORDER BY r.id")
    List<Long> findPublishedIds();

    @Query("SELECT r FROM KnowledgeBaseRecord r WHERE r.id = :id AND" + PUBLISHED)
    Optional<KnowledgeBaseRecord> findPublishedById(Long id);

    @Query("SELECT r FROM KnowledgeBaseRecord r WHERE r.parent.id = :parentId AND" + PUBLISHED + " ORDER BY r.title")
    List<KnowledgeBaseRecord> findPublishedChildren(Long parentId);

    /** Articles published in any of these categories after {@code since}, newest first. */
    @Query("SELECT r FROM KnowledgeBaseRecord r WHERE" + PUBLISHED
            + " AND r.category.catId IN :categoryIds AND r.createdDate > :since ORDER BY r.createdDate DESC")
    List<KnowledgeBaseRecord> findPublishedSince(Collection<Long> categoryIds, LocalDateTime since);

    // Counters start as NULL on existing rows, hence COALESCE. Return the rows updated.
    @Modifying
    @Query("UPDATE KnowledgeBaseRecord r SET r.upvotes = COALESCE(r.upvotes, 0) + 1 WHERE r.id = :id")
    int upvote(Long id);

    @Modifying
    @Query("UPDATE KnowledgeBaseRecord r SET r.downvotes = COALESCE(r.downvotes, 0) + 1 WHERE r.id = :id")
    int downvote(Long id);
}
