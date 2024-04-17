package com.play.quiz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String source;

    @JsonFormat(pattern = "HH:mm yyyy-MM-dd")
    private LocalDateTime createdDate;
}
