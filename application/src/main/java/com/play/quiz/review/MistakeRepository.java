package com.play.quiz.review;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MistakeRepository extends JpaRepository<Mistake, Long> {

    List<Mistake> findByAccountId(Long accountId);

    List<Mistake> findByAccountIdAndQuestionIdIn(Long accountId, Collection<Long> questionIds);

    List<Mistake> findByAccountIdAndDueDateLessThanEqualOrderByDueDate(Long accountId, LocalDate day, Pageable pageable);
}
