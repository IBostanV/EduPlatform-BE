package com.play.quiz.service.impl;

import com.play.quiz.domain.UserGroup;
import com.play.quiz.dto.UserGroupDto;
import com.play.quiz.mapper.UserGroupMapper;
import com.play.quiz.repository.UserGroupRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {

    private final AuthenticationFacade authenticationFacade;
    private final UserGroupRepository userGroupRepository;
    private final UserGroupMapper userGroupMapper;

    @Override
    public Set<UserGroup> getUserGroupsByUsername(String username) {
        Set<Long> userGroupIds = userGroupRepository.findUserGroupIds(username);
        return userGroupRepository.findByIdsWithoutSelf(userGroupIds, username);
    }

    public Set<Long> fetchUserIdsByGroupId(long groupId) {
        return userGroupRepository.findUserIdsByUserGroupId(groupId);
    }

    @Override
    @Transactional
    public Set<UserGroupDto> getCurrentUserGroups() {
        String username = authenticationFacade.getPrincipal().getUsername();
        Set<UserGroup> userGroups = getUserGroupsByUsername(username);

        return userGroupMapper.toDtoSet(userGroups);
    }
}
