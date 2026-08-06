package com.play.quiz.service;

import com.play.quiz.dto.MessageDto;

import java.security.Principal;
import java.util.List;

public interface MessageService {

    void sendPrivateMessage(final MessageDto payload, String sessionId, final Principal principal);

    MessageDto sendPublicMessage(final MessageDto payload, String sessionId, final Principal principal);

    List<MessageDto> fetchMessageHistory(Long destination, String principalName);

    MessageDto editMessage(Long messageId, String content, String principalName);

    void deleteMessage(Long messageId, String principalName);
}
