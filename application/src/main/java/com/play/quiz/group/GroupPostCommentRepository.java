package com.play.quiz.group;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupPostCommentRepository extends JpaRepository<GroupPostComment, Long> {

    List<GroupPostComment> findByPostIdInOrderByCreatedDate(Collection<Long> postIds);
}
