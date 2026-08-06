package com.play.quiz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.play.quiz.enums.MessageEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private byte[] attachment;
    private String content;
    private Long destinationId;
    private Long messageId;
    private Long participantId;
    private String participantUsername;
    private String sessionId;

    // The author's email: kept server-side (who may edit or delete), never sent to the members,
    // who know the author by participantId and participantUsername instead.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String source;

    @JsonFormat(pattern = "HH:mm yyyy-MM-dd")
    private LocalDateTime createdDate;

    // Changed after it was sent (Q_MESSAGE.UPDATED_DATE is set).
    private boolean edited;

    // Only on socket pushes: what the chat should do with this message. Absent in history.
    private MessageEvent event;
}
