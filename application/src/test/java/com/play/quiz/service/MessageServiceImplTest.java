package com.play.quiz.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Message;
import com.play.quiz.domain.MessageGroup;
import com.play.quiz.dto.MessageDto;
import com.play.quiz.enums.MessageEvent;
import com.play.quiz.mapper.MessageMapper;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.MessageRepository;
import com.play.quiz.repository.UserGroupRepository;
import com.play.quiz.service.impl.MessageServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MessageServiceImplTest {

    private static final String AUTHOR = "author@play.quiz";
    private static final String MEMBER = "member@play.quiz";
    private static final long GROUP_ID = 5L;
    private static final long MESSAGE_ID = 42L;

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;
    @Mock
    private UserService userService;
    @Mock
    private UserGroupRepository userGroupRepository;
    @Mock
    private AccountRepository accountRepository;

    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageServiceImpl(messageRepository, messageMapper, simpMessagingTemplate, userService,
                userGroupRepository, accountRepository);

        Message stored = Message.builder()
                .messageId(MESSAGE_ID)
                .source(AUTHOR)
                .content("<p>old</p>")
                .destination(MessageGroup.builder().groupId(GROUP_ID).build())
                .build();
        when(messageRepository.findById(MESSAGE_ID)).thenReturn(Optional.of(stored));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(messageMapper.toDto(any(Message.class))).thenAnswer(invocation -> {
            Message message = invocation.getArgument(0);
            return MessageDto.builder()
                    .messageId(message.getMessageId())
                    .content(message.getContent())
                    .destinationId(message.getDestination().getGroupId())
                    .build();
        });
        when(userService.getUsersByUserGroupId(GROUP_ID)).thenReturn(Set.of(
                Account.builder().email(AUTHOR).build(),
                Account.builder().email(MEMBER).build()));
    }

    @Test
    void editByAnotherUserIsRefusedAndNothingIsSaved() {
        assertThrows(AccessDeniedException.class, () -> messageService.editMessage(MESSAGE_ID, "<p>hijack</p>", MEMBER));
        verify(messageRepository, never()).save(any());
    }

    @Test
    void editStripsScriptsAndHandlersAndTellsEveryMember() {
        MessageDto result = messageService.editMessage(MESSAGE_ID,
                "<p onclick=\"steal()\">new <b>text</b><script>alert(1)</script></p>", AUTHOR);

        assertEquals("<p>new <b>text</b></p>", result.getContent());

        ArgumentCaptor<MessageDto> pushed = ArgumentCaptor.forClass(MessageDto.class);
        verify(simpMessagingTemplate).convertAndSendToUser(eq(MEMBER), any(), pushed.capture());
        assertEquals(MessageEvent.EDITED, pushed.getValue().getEvent());
        assertEquals("<p>new <b>text</b></p>", pushed.getValue().getContent());
    }

    @Test
    void editToNothingButMarkupIsRefused() {
        assertThrows(IllegalArgumentException.class,
                () -> messageService.editMessage(MESSAGE_ID, "<p><script>alert(1)</script></p>", AUTHOR));
        verify(messageRepository, never()).save(any());
    }

    @Test
    void deleteByAuthorRemovesItAndTellsEveryMemberWhichOne() {
        messageService.deleteMessage(MESSAGE_ID, AUTHOR);

        verify(messageRepository).delete(any(Message.class));
        ArgumentCaptor<MessageDto> pushed = ArgumentCaptor.forClass(MessageDto.class);
        verify(simpMessagingTemplate).convertAndSendToUser(eq(MEMBER), any(), pushed.capture());
        assertEquals(MessageEvent.DELETED, pushed.getValue().getEvent());
        assertEquals(MESSAGE_ID, pushed.getValue().getMessageId());
        assertEquals(GROUP_ID, pushed.getValue().getDestinationId());
    }

    @Test
    void historyOfAGroupIsRefusedToNonMembers() {
        when(userGroupRepository.isMember(GROUP_ID, "outsider@play.quiz")).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> messageService.fetchMessageHistory(GROUP_ID, "outsider@play.quiz"));
        verify(messageRepository, never()).findByDestination_GroupId(any());
    }

    @Test
    void historyOfAGroupIsReturnedToMembers() {
        when(userGroupRepository.isMember(GROUP_ID, MEMBER)).thenReturn(true);
        when(messageRepository.findByDestination_GroupId(GROUP_ID)).thenReturn(List.of());
        when(messageMapper.toDtoList(List.of())).thenReturn(List.of());

        assertEquals(List.of(), messageService.fetchMessageHistory(GROUP_ID, MEMBER));
    }

    @Test
    void deleteByAnotherUserIsRefused() {
        assertThrows(AccessDeniedException.class, () -> messageService.deleteMessage(MESSAGE_ID, MEMBER));
        verify(messageRepository, never()).delete(any());
    }

    @Test
    void historyCarriesEachAuthorsIdAndNameForTheirProfileLink() {
        when(userGroupRepository.isMember(GROUP_ID, MEMBER)).thenReturn(true);
        when(messageRepository.findByDestination_GroupId(GROUP_ID)).thenReturn(List.of());
        when(messageMapper.toDtoList(any())).thenReturn(List.of(
                MessageDto.builder().messageId(1L).source(AUTHOR).build(),
                MessageDto.builder().messageId(2L).source(AUTHOR).build()));
        when(accountRepository.findByEmail(AUTHOR))
                .thenReturn(Optional.of(Account.builder().accountId(7L).email(AUTHOR).username("quizzer").build()));

        List<MessageDto> history = messageService.fetchMessageHistory(GROUP_ID, MEMBER);

        assertEquals(7L, history.get(1).getParticipantId());
        assertEquals("quizzer", history.get(1).getParticipantUsername());
        verify(accountRepository).findByEmail(AUTHOR);
    }

    @Test
    void theAuthorsEmailIsNeverSentToTheMembers() throws Exception {
        String json = new com.fasterxml.jackson.databind.ObjectMapper()
                .writeValueAsString(MessageDto.builder().source(AUTHOR).participantId(7L).build());

        assertEquals(false, json.contains(AUTHOR));
    }
}
