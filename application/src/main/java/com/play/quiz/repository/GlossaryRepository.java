package com.play.quiz.repository;

import java.util.List;
import java.util.Optional;

import com.play.quiz.domain.Category;
import com.play.quiz.domain.Glossary;
import com.play.quiz.domain.GlossaryType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GlossaryRepository extends JpaRepository<Glossary, Long> {

    // Terms with no type. A question's wrong options are drawn from the right answer's glossary
    // type, so a term without one leaves its questions with nothing plausible to offer.
    long countByTypeIsNull();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Glossary gl " +
            " SET gl.isActive = CASE WHEN gl.isActive = TRUE THEN FALSE ELSE TRUE END" +
            " WHERE gl.termId = :id")
    int toggleGlossary(@Param("id") final Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Glossary gl " +
            " SET gl.key = :#{#glossary.key}" +
            ", gl.type = :#{#glossary.type}" +
            ", gl.value = :#{#glossary.value}" +
            ", gl.parent = :#{#glossary.parent}" +
            ", gl.options = :#{#glossary.options}" +
            ", gl.isActive = :#{#glossary.isActive}" +
            ", gl.category = :#{#glossary.category}" +
            " WHERE gl.termId = :#{#glossary.termId}")
    int saveWithoutAttachment(@Param("glossary") final Glossary glossary);

    // Random order, so a question gets different wrong options each time instead of the same first rows.
    @Query("SELECT g FROM Glossary g WHERE g.type = :type AND g.isActive = TRUE AND g.termId NOT IN :excludedTermIds"
            + " ORDER BY FUNCTION('random')")
    List<Glossary> findWrongOptions(final GlossaryType type, final List<Long> excludedTermIds, Pageable pageable);

    Optional<Glossary> findByKey(String key);

    List<Glossary> findAllByCategory(final Category category);

    // Child terms: Q_GLOSSARY.PARENT_ID blocks deleting the parent.
    @Query("SELECT COUNT(g) FROM Glossary g WHERE g.parent.termId = :termId")
    long countChildren(Long termId);

    @Query(value = """
            WITH RECURSIVE tree (CAT_ID) AS (
                SELECT c.CAT_ID FROM Q_CATEGORY c WHERE c.CAT_ID = :categoryId
                UNION
                SELECT c.CAT_ID FROM Q_CATEGORY c JOIN tree t ON c.SUBCATEGORY_ID = t.CAT_ID
            )
            SELECT qg.* FROM Q_GLOSSARY qg WHERE qg.CAT_ID IN (SELECT CAT_ID FROM tree)
            """, nativeQuery = true)
    Optional<List<Glossary>> findHierarchicalByCategoryId(final Long categoryId);
}
