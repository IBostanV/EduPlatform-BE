package com.play.quiz.feed;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsPostRepository extends JpaRepository<NewsPost, Long> {

    List<NewsPost> findByPatchTrueAndCreatedDateAfter(LocalDateTime since);

    List<NewsPost> findByPatchFalseAndCreatedByInAndCreatedDateAfter(Collection<Long> authors, LocalDateTime since);
}
