package com.play.quiz.service.impl;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.MessageGroup;
import com.play.quiz.domain.UserGroup;
import com.play.quiz.dto.UserGroupDto;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.exception.UserNotFoundException;
import com.play.quiz.mapper.UserGroupMapper;
import com.play.quiz.repository.MessageGroupRepository;
import com.play.quiz.repository.MessageRepository;
import com.play.quiz.repository.UserGroupRepository;
import com.play.quiz.repository.UserRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {

    private final AuthenticationFacade authenticationFacade;
    private final UserGroupRepository userGroupRepository;
    private final MessageGroupRepository messageGroupRepository;
    private final MessageRepository messageRepository;
    private final UserGroupMapper userGroupMapper;
    // Repository, not UserService: UserServiceImpl already depends on this service.
    private final UserRepository userRepository;

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

    /**
     * A group is one Q_USER_GROUP row per participant sharing a GROUP_ID. The creator is
     * always added, so the group shows up in their own list.
     */
    @Override
    @Transactional
    public Long createGroup(String name, Set<Long> participantIds) {
        String username = authenticationFacade.getPrincipal().getUsername();
        Account creator = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("No user found with email: " + username));

        Set<Long> ids = new HashSet<>(participantIds == null ? Set.of() : participantIds);
        ids.remove(creator.getAccountId());
        // Checked before querying: an empty IN () is invalid SQL.
        Set<Account> participants = ids.isEmpty() ? new HashSet<>() : new HashSet<>(userRepository.findByUserIds(ids));
        if (participants.isEmpty()) {
            throw new IllegalArgumentException("A group needs at least one other participant");
        }
        participants.add(creator);

        String groupName = StringUtils.hasText(name) ? name.trim() : null;
        Long groupId = messageGroupRepository.save(MessageGroup.builder().name(groupName).build()).getGroupId();
        // save() one by one, not saveAll(): the audit aspect only intercepts save().
        participants.forEach(account -> userGroupRepository.save(UserGroup.builder()
                .groupId(groupId)
                .participant(account)
                .build()));
        log.info("Created message group id: {} with {} participants ({} other requested)",
                groupId, participants.size(), ids.size());

        return groupId;
    }

    /**
     * Deletes the group for everyone: its messages, every membership row, then the group.
     * That order follows the foreign keys (Q_MESSAGE and Q_USER_GROUP both point at
     * Q_MESSAGE_GROUP). Any member may do it; anyone else gets a 403.
     */
    @Override
    @Transactional
    public void deleteGroup(Long groupId) {
        String username = authenticationFacade.getPrincipal().getUsername();
        if (!userGroupRepository.isMember(groupId, username)) {
            throw new AccessDeniedException("Only members can delete group " + groupId);
        }

        log.info("Deleting message group {}", groupId);
        messageRepository.deleteByDestinationGroupId(groupId);
        userGroupRepository.deleteByGroupId(groupId);
        messageGroupRepository.deleteById(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Long> getMutedGroupIds() {
        return userGroupRepository.findMutedGroupIds(authenticationFacade.getPrincipal().getUsername());
    }

    /**
     * Mutes or unmutes the group for the signed-in user only. Messages still arrive; muting just
     * tells the client not to notify about them.
     */
    @Override
    @Transactional
    public void setMuted(Long groupId, boolean muted) {
        String username = authenticationFacade.getPrincipal().getUsername();
        if (userGroupRepository.setMuted(groupId, username, muted) == 0) {
            throw new AccessDeniedException("Only members can mute group " + groupId);
        }
        log.info("Message group {} muted: {}", groupId, muted);
    }

    /**
     * The group's picture, shown to every member, so any member may change it. Sending nothing
     * clears it and the group goes back to its initials.
     */
    @Override
    @Transactional
    public void setPhoto(Long groupId, final MultipartFile photo) {
        String username = authenticationFacade.getPrincipal().getUsername();
        if (!userGroupRepository.isMember(groupId, username)) {
            throw new AccessDeniedException("Only members can set the picture of group " + groupId);
        }

        MessageGroup group = messageGroupRepository.findById(groupId)
                .orElseThrow(() -> new RecordNotFoundException("No group with id: " + groupId));
        boolean given = Objects.nonNull(photo) && !photo.isEmpty();
        if (given && !String.valueOf(photo.getContentType()).startsWith("image/")) {
            throw new IllegalArgumentException("A group picture has to be an image");
        }

        messageGroupRepository.save(group.toBuilder()
                .photo(given ? readBytes(photo) : null)
                .photoType(given ? photo.getContentType() : null)
                .build());
        log.info("Message group {} picture {}", groupId, given ? "set" : "cleared");
    }

    private static byte[] readBytes(final MultipartFile photo) {
        try {
            return photo.getBytes();
        } catch (IOException exception) {
            throw new IllegalArgumentException("Could not read the group picture", exception);
        }
    }
}
