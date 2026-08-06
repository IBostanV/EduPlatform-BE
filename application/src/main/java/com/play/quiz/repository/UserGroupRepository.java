package com.play.quiz.repository;

import com.play.quiz.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query("SELECT ug.groupId FROM UserGroup ug WHERE ug.participant.email = :username")
    Set<Long> findUserGroupIds(String username);

    @Query("SELECT ug FROM UserGroup ug JOIN ug.participant p WHERE ug.groupId IN :ids AND p.email <> :currentUsername")
    Set<UserGroup> findByIdsWithoutSelf(Set<Long> ids, String currentUsername);

    @Query("SELECT ug.participant.accountId FROM UserGroup ug WHERE ug.groupId = :userGroupId")
    Set<Long> findUserIdsByUserGroupId(long userGroupId);

    @Query("SELECT COUNT(ug) > 0 FROM UserGroup ug WHERE ug.groupId = :groupId AND ug.participant.email = :username")
    boolean isMember(Long groupId, String username);

    @Modifying
    @Query("DELETE FROM UserGroup ug WHERE ug.groupId = :groupId")
    void deleteByGroupId(Long groupId);

    // MUTED is per membership row, so each member mutes a group only for themselves.
    @Query("SELECT ug.groupId FROM UserGroup ug WHERE ug.participant.email = :username AND ug.muted = true")
    Set<Long> findMutedGroupIds(String username);

    // Returns the rows updated: 0 means the user is not in that group.
    @Modifying
    @Query("UPDATE UserGroup ug SET ug.muted = :muted WHERE ug.groupId = :groupId AND ug.participant.email = :username")
    int setMuted(Long groupId, String username, boolean muted);
}
