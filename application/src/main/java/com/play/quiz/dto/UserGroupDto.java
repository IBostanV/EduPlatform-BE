package com.play.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupDto {
    private Long id;
    private Long groupId;
    private String name;
    private String theme;
    private boolean muted;
    private Long participantId;
    private String participantUsername;
}
