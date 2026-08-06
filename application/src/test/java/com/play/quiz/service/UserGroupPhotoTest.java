package com.play.quiz.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.play.quiz.domain.MessageGroup;
import com.play.quiz.fixtures.AccountFixture;
import com.play.quiz.fixtures.UserDetailsFixture;
import com.play.quiz.mapper.UserGroupMapper;
import com.play.quiz.repository.MessageGroupRepository;
import com.play.quiz.repository.MessageRepository;
import com.play.quiz.repository.UserGroupRepository;
import com.play.quiz.repository.UserRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.impl.UserGroupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;

/** A chat group's picture: members set it, and only an image counts. */
@ExtendWith(MockitoExtension.class)
class UserGroupPhotoTest {

    @Mock private AuthenticationFacade authenticationFacade;
    @Mock private UserGroupRepository userGroupRepository;
    @Mock private MessageGroupRepository messageGroupRepository;
    @Mock private MessageRepository messageRepository;
    @Mock private UserGroupMapper userGroupMapper;
    @Mock private UserRepository userRepository;

    private UserGroupService userGroupService;

    @BeforeEach
    void init() {
        userGroupService = new UserGroupServiceImpl(authenticationFacade, userGroupRepository, messageGroupRepository,
                messageRepository, userGroupMapper, userRepository);
    }

    @Test
    void given_a_member_when_setting_a_picture_then_keep_it_with_its_type() {
        signedInAsMember(true);
        when(messageGroupRepository.findById(3L)).thenReturn(Optional.of(MessageGroup.builder().groupId(3L).build()));

        userGroupService.setPhoto(3L, new MockMultipartFile("photo", "team.png", "image/png", new byte[]{7, 8}));

        ArgumentCaptor<MessageGroup> saved = ArgumentCaptor.forClass(MessageGroup.class);
        verify(messageGroupRepository).save(saved.capture());
        assertArrayEquals(new byte[]{7, 8}, saved.getValue().getPhoto());
        assertEquals("image/png", saved.getValue().getPhotoType());
    }

    @Test
    void given_no_file_when_setting_a_picture_then_clear_it_and_go_back_to_initials() {
        signedInAsMember(true);
        when(messageGroupRepository.findById(3L))
                .thenReturn(Optional.of(MessageGroup.builder().groupId(3L).photo(new byte[]{1}).photoType("image/png").build()));

        userGroupService.setPhoto(3L, null);

        ArgumentCaptor<MessageGroup> saved = ArgumentCaptor.forClass(MessageGroup.class);
        verify(messageGroupRepository).save(saved.capture());
        assertNull(saved.getValue().getPhoto());
        assertNull(saved.getValue().getPhotoType());
    }

    @Test
    void given_someone_outside_the_group_when_setting_a_picture_then_deny() {
        signedInAsMember(false);

        MockMultipartFile picture = new MockMultipartFile("photo", "team.png", "image/png", new byte[]{7});

        assertThrows(AccessDeniedException.class, () -> userGroupService.setPhoto(3L, picture));
        verify(messageGroupRepository, never()).save(any());
    }

    @Test
    void given_a_file_that_is_not_an_image_when_setting_a_picture_then_reject_it() {
        signedInAsMember(true);
        when(messageGroupRepository.findById(3L)).thenReturn(Optional.of(MessageGroup.builder().groupId(3L).build()));

        MockMultipartFile document = new MockMultipartFile("photo", "notes.pdf", "application/pdf", new byte[]{1});

        assertThrows(IllegalArgumentException.class, () -> userGroupService.setPhoto(3L, document));
        verify(messageGroupRepository, never()).save(any());
    }

    private void signedInAsMember(boolean member) {
        when(authenticationFacade.getPrincipal()).thenReturn((User) UserDetailsFixture.getAdminUserDetails());
        when(userGroupRepository.isMember(3L, AccountFixture.getAdminAccount().getEmail())).thenReturn(member);
    }
}
