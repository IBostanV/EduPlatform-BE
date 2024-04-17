package com.play.quiz.repository;

import com.play.quiz.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query("SELECT ug.groupId FROM UserGroup ug WHERE ug.participant.email = :username")
    Set<Long> findUserGroupIds(String username);

    @Query("SELECT ug FROM UserGroup ug JOIN ug.participant p WHERE ug.groupId IN :ids AND p.email <> :currentUsername")
    Set<UserGroup> findByIdsWithoutSelf(Set<Long> ids, String currentUsername);

    @Query("SELECT ug.participant.accountId FROM UserGroup ug WHERE ug.groupId = :userGroupId")
    Set<Long> findUserIdsByUserGroupId(long userGroupId);
}
