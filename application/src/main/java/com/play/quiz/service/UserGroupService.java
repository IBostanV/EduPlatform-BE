package com.play.quiz.service;

import com.play.quiz.domain.UserGroup;
import com.play.quiz.dto.UserGroupDto;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

public interface UserGroupService {


    Set<UserGroup> getUserGroupsByUsername(String username);

    Set<UserGroupDto> getCurrentUserGroups();

    Set<Long> fetchUserIdsByGroupId(long userGroupId);

    Long createGroup(String name, Set<Long> participantIds);

    void deleteGroup(Long groupId);

    Set<Long> getMutedGroupIds();

    void setMuted(Long groupId, boolean muted);

    /** Sets the group's picture, or clears it when given nothing. Members only. */
    void setPhoto(Long groupId, final MultipartFile photo);
}
