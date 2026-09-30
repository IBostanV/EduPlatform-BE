package com.play.quiz.controller;

import java.security.Principal;
import java.util.List;

import com.play.quiz.dto.MessageDto;
import com.play.quiz.record.EditMessageInput;
import com.play.quiz.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_MESSAGE;
import static com.play.quiz.controller.RestEndpoint.WS_BROKER_PARTY;

@Log4j2
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_MESSAGE)
@RequiredArgsConstructor
public class MessageController {

    private static final String SESSION_ID = "simpSessionId";

    private final MessageService messageService;

    @MessageMapping("/public")
    @SendTo(WS_BROKER_PARTY + "/news")
    public MessageDto sendPublic(@Payload MessageDto payload, @Header(SESSION_ID) String sessionId, Principal principal) {
        return messageService.sendPublicMessage(payload, sessionId, principal);
    }

    @MessageMapping("/private")
    public void sendPrivate(@Payload MessageDto payload, @Header(SESSION_ID) String sessionId, Principal principal) {
        messageService.sendPrivateMessage(payload, sessionId, principal);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MessageDto>> getHistory(@RequestParam(required = false) Long dest, Principal principal) {
        return ResponseEntity.ok(messageService.fetchMessageHistory(dest, principal.getName()));
    }

    // The author changes their own message; every group member gets the new text live.
    @PutMapping(value = "/{messageId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> editMessage(@PathVariable Long messageId, @RequestBody EditMessageInput input,
                                                  Principal principal) {
        return ResponseEntity.ok(messageService.editMessage(messageId, input.content(), principal.getName()));
    }

    // The author removes their own message; it disappears for every group member live.
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId, Principal principal) {
        messageService.deleteMessage(messageId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
