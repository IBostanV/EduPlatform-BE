package com.play.quiz.service;

import com.play.quiz.domain.UserGroup;
import com.play.quiz.dto.UserGroupDto;

import java.util.Set;

public interface UserGroupService {

    Set<UserGroup> getUserGroupsByUsername(String username);

    Set<UserGroupDto> getCurrentUserGroups();

    Set<Long> fetchUserIdsByGroupId(long userGroupId);
}
