package com.play.quiz.group;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findByGroupIdOrderByJoinedDate(Long groupId);

    List<GroupMember> findByGroupIdIn(Collection<Long> groupIds);

    List<GroupMember> findByAccountId(Long accountId);

    Optional<GroupMember> findByGroupIdAndAccountId(Long groupId, Long accountId);
}
