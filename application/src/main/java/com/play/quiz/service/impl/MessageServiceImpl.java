package com.play.quiz.service.impl;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Message;
import com.play.quiz.dto.MessageDto;
import com.play.quiz.enums.MessageEvent;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.mapper.MessageMapper;
import com.play.quiz.record.UserSummary;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.MessageRepository;
import com.play.quiz.repository.UserGroupRepository;
import com.play.quiz.service.MessageService;
import com.play.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.play.quiz.controller.RestEndpoint.WS_BROKER_SOLO;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    // Q_MESSAGE.CONTENT is VARCHAR(800); a longer message would fail in the database instead.
    private static final int MAX_CONTENT_LENGTH = 800;
    // The editor's formatting survives; scripts, event handlers and javascript: links do not.
    // Every member renders the HTML as-is, so it is cleaned before it is stored.
    private static final Safelist MESSAGE_HTML = Safelist.relaxed();

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;
    private final UserGroupRepository userGroupRepository;
    private final AccountRepository accountRepository;

    // A group's history is for its members only: without this check any signed-in user could read
    // any group by changing `dest`. No destination means the public channel, open to everyone.
    @Override
    @Transactional
    public List<MessageDto> fetchMessageHistory(Long destination, String principalName) {
        if (Objects.nonNull(destination)) {
            if (!userGroupRepository.isMember(destination, principalName)) {
                throw new AccessDeniedException("Not a member of group " + destination);
            }
            List<Message> messages = messageRepository.findByDestination_GroupId(destination);
            return withSenders(messageMapper.toDtoList(messages));
        }

        List<Message> messages = messageRepository.findByDestination_GroupId(1L);
        return withSenders(messageMapper.toDtoList(messages));
    }

    @Override
    public MessageDto sendPublicMessage(final MessageDto payload, String sessionId, final Principal principal) {
        MessageDto message = builder(payload, sessionId, principal);
        Message entity = messageRepository.save(messageMapper.toEntity(message));

        return withSender(messageMapper.toDto(entity));
    }

    // Pushes the saved message, not the payload: only the saved one has its id, and the chat
    // needs the id to edit or delete a message that arrived live.
    @Override
    public void sendPrivateMessage(final MessageDto payload, String sessionId, final Principal principal) {
        MessageDto message = builder(payload, sessionId, principal);
        Message saved = messageRepository.save(messageMapper.toEntity(message));

        pushToGroup(saved.getDestination().getGroupId(),
                withEvent(withSender(messageMapper.toDto(saved)), MessageEvent.CREATED));
    }

    /**
     * Replaces the text of the caller's own message and tells every group member, so their chat
     * updates in place. The save stamps UPDATED_DATE, which marks the message as edited.
     */
    @Override
    @Transactional
    public MessageDto editMessage(final Long messageId, final String content, final String principalName) {
        Message message = findOwnMessage(messageId, principalName);
        Message saved = messageRepository.save(message.toBuilder().content(clean(content)).build());

        MessageDto dto = withSender(messageMapper.toDto(saved));
        pushToGroup(saved.getDestination().getGroupId(), withEvent(dto, MessageEvent.EDITED));
        return dto;
    }

    // Deletes the caller's own message and tells every group member to take it off screen.
    @Override
    @Transactional
    public void deleteMessage(final Long messageId, final String principalName) {
        Message message = findOwnMessage(messageId, principalName);
        Long groupId = message.getDestination().getGroupId();
        messageRepository.delete(message);

        pushToGroup(groupId, MessageDto.builder()
                .messageId(messageId)
                .destinationId(groupId)
                .event(MessageEvent.DELETED)
                .build());
    }

    // Only the author may change or remove a message; SOURCE holds the author's email.
    private Message findOwnMessage(final Long messageId, final String principalName) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RecordNotFoundException("No message with id: " + messageId));
        if (!Objects.equals(message.getSource(), principalName)) {
            throw new AccessDeniedException("Only the author can change message " + messageId);
        }
        return message;
    }

    private void pushToGroup(final Long groupId, final MessageDto message) {
        Set<Account> users = userService.getUsersByUserGroupId(groupId);
        users.forEach(user -> simpMessagingTemplate.convertAndSendToUser(user.getEmail(), WS_BROKER_SOLO, message));
    }

    // SOURCE holds the author's email, which the chat cannot link to a profile. The id and name
    // (the same name UserSummary shows everywhere else) ride along in participantId/Username.
    // One lookup per author, not per message: a history is a handful of people talking.
    private List<MessageDto> withSenders(final List<MessageDto> messages) {
        Map<String, UserSummary> senders = messages.stream()
                .map(MessageDto::getSource)
                .filter(Objects::nonNull)
                .distinct()
                .flatMap(email -> accountRepository.findByEmail(email).stream())
                .collect(Collectors.toMap(Account::getEmail, UserSummary::of));

        return messages.stream().map(message -> {
            UserSummary sender = senders.get(message.getSource());
            return Objects.isNull(sender) ? message : message.toBuilder()
                    .participantId(sender.id())
                    .participantUsername(sender.displayName())
                    .build();
        }).toList();
    }

    private MessageDto withSender(final MessageDto message) {
        return withSenders(List.of(message)).get(0);
    }

    private static MessageDto withEvent(final MessageDto message, final MessageEvent event) {
        return message.toBuilder().event(event).build();
    }

    private static String clean(final String html) {
        String cleaned = html == null ? "" : Jsoup.clean(html, MESSAGE_HTML);
        if (Jsoup.parse(cleaned).text().isBlank()) {
            throw new IllegalArgumentException("A message cannot be empty");
        }
        if (cleaned.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException("A message can be at most " + MAX_CONTENT_LENGTH + " characters long");
        }
        return cleaned;
    }

    private MessageDto builder(final MessageDto payload, String sessionId, final Principal principal) {
        return payload.toBuilder()
                .content(clean(payload.getContent()))
                .sessionId(sessionId)
                .source(principal.getName())
                .createdDate(LocalDateTime.now())
                .build();
    }
}
