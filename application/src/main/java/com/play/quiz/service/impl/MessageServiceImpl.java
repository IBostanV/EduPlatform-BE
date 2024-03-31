package com.play.quiz.service.impl;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Message;
import com.play.quiz.dto.MessageDto;
import com.play.quiz.mapper.MessageMapper;
import com.play.quiz.repository.MessageRepository;
import com.play.quiz.service.MessageService;
import com.play.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.play.quiz.controller.RestEndpoint.WS_BROKER_SOLO;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;

    @Override
    @Transactional
    public List<MessageDto> fetchMessageHistory(Long destination, String principalName) {
        if (Objects.nonNull(destination)) {
            List<Message> messages = messageRepository.findBySourceAndDestination_GroupId(principalName, destination);
            return messageMapper.toDtoList(messages);
        }

        List<Message> messages = messageRepository.findByDestination_Id(1L);
        return messageMapper.toDtoList(messages);
    }

    @Override
    public MessageDto sendPublicMessage(final MessageDto payload, String sessionId, final Principal principal) {
        MessageDto message = builder(payload, sessionId, principal);
        Message entity = messageRepository.save(messageMapper.toEntity(message));

        return messageMapper.toDto(entity);
    }

    @Override
    public void sendPrivateMessage(final MessageDto payload, String sessionId, final Principal principal) {
        MessageDto message = builder(payload, sessionId, principal);
        messageRepository.save(messageMapper.toEntity(message));

        Set<Account> users = userService.getUsersByUserGroupId(payload.getDestinationId());
        users.forEach(user -> simpMessagingTemplate.convertAndSendToUser(user.getEmail(), WS_BROKER_SOLO, message));
    }

    private MessageDto builder(final MessageDto payload, String sessionId, final Principal principal) {
        return payload.toBuilder()
                .sessionId(sessionId)
                .source(principal.getName())
                .createdDate(LocalDateTime.now())
                .build();
    }
}
