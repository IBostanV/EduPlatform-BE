package com.play.quiz.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.play.quiz.domain.Category;
import com.play.quiz.domain.Question;
import com.play.quiz.enums.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByCategory_naturalId(final String naturalId);

    List<Question> findByTypeAndCategory(final QuestionType template, final Category category);

    @Modifying
    @Query("UPDATE Question q SET q.isActive = CASE WHEN q.isActive = TRUE THEN FALSE ELSE TRUE END WHERE q.questionId = :questionId")
    void deactivate(final Long questionId);

    List<Question> findAllByQuestionIdIn(final Set<Long> idList);

    // Admin search: question text, topic or category name. `pattern` is lower-cased and
    // wrapped in %…% by the service. LEFT JOIN so a question without a category still matches.
    @Query("""
            SELECT q FROM Question q LEFT JOIN q.category c
            WHERE LOWER(q.content) LIKE :pattern
               OR LOWER(q.topic) LIKE :pattern
               OR LOWER(c.name) LIKE :pattern
            """)
    Page<Question> search(String pattern, Pageable pageable);

    @Query(nativeQuery = true, value = """
    SELECT q.*
    FROM Q_QUESTION q
    WHERE q.CAT_ID IN (
          SELECT c.CAT_ID
          FROM Q_CATEGORY c
          START WITH c.CAT_ID = :catId
          CONNECT BY NOCYCLE PRIOR c.CAT_ID = c.SUBCATEGORY_ID
      )
      AND (
          :quizType IS NULL
          OR BITAND(:quizType, q.EXCLUDE_TYPE) = 0
      )
      AND (:complexityFrom IS NULL OR q.COMPLEXITY_LEVEL >= :complexityFrom)
      AND (:complexityTo IS NULL OR q.COMPLEXITY_LEVEL <= :complexityTo)
    """)
    List<Question> getByCategoryAndParams(Long catId, Long quizType, Integer complexityFrom, Integer complexityTo,
                                          Pageable pageable);

    // Home page mini game: a random active question with exactly one answer, so a single pick is
    // right or wrong. Map questions are left out: their options only make sense on the map.
    @Query(nativeQuery = true, value = """
    SELECT q.QUESTION_ID
    FROM Q_QUESTION q
    WHERE q.IS_ACTIVE = 1
      AND (SELECT COUNT(*) FROM Q_ANSWER a WHERE a.QUESTION_ID = q.QUESTION_ID) = 1
      AND NOT EXISTS (
          SELECT 1
          FROM Q_ANSWER a
          JOIN Q_GLOSSARY g ON g.TERM_ID = a.TERM_ID
          JOIN Q_GLOSSARY_TYPE t ON t.ID = g.TYPE_ID
          WHERE a.QUESTION_ID = q.QUESTION_ID AND LOWER(t.OPTIONS) LIKE 'map:%'
      )
    ORDER BY DBMS_RANDOM.VALUE
    FETCH FIRST 1 ROWS ONLY
    """)
    Optional<Long> findRandomMiniGameQuestionId();

    // The same kind of question, several at once and all different: a live match's questions.
    @Query(nativeQuery = true, value = """
    SELECT q.QUESTION_ID
    FROM Q_QUESTION q
    WHERE q.IS_ACTIVE = 1
      AND (SELECT COUNT(*) FROM Q_ANSWER a WHERE a.QUESTION_ID = q.QUESTION_ID) = 1
      AND NOT EXISTS (
          SELECT 1
          FROM Q_ANSWER a
          JOIN Q_GLOSSARY g ON g.TERM_ID = a.TERM_ID
          JOIN Q_GLOSSARY_TYPE t ON t.ID = g.TYPE_ID
          WHERE a.QUESTION_ID = q.QUESTION_ID AND LOWER(t.OPTIONS) LIKE 'map:%'
      )
    ORDER BY DBMS_RANDOM.VALUE
    FETCH FIRST :count ROWS ONLY
    """)
    List<Long> findRandomMiniGameQuestionIds(int count);

    /**
     * Every active question added to a visible category after {@code since}, for the news. One
     * row per question: the days are counted in the service, a date function being the database's
     * dialect.
     */
    @Query("SELECT q.category.catId AS categoryId, q.category.name AS categoryName, q.createdDate AS createdDate"
            + " FROM Question q WHERE q.createdDate > :since AND q.category.visible = TRUE"
            + " AND (q.isActive IS NULL OR q.isActive = TRUE)")
    List<AddedQuestion> findAddedSince(LocalDateTime since);

    interface AddedQuestion {
        Long getCategoryId();
        String getCategoryName();
        LocalDateTime getCreatedDate();
    }
}
