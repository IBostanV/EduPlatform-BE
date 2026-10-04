package com.play.quiz.group;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupPostRepository extends JpaRepository<GroupPost, Long> {

    List<GroupPost> findByGroupIdOrderByCreatedDateDesc(Long groupId, Pageable pageable);

    List<GroupPost> findByPostIdIn(Collection<Long> postIds);

    List<GroupPost> findByAccountId(Long accountId);
}
