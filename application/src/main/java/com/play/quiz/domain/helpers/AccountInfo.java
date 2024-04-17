package com.play.quiz.domain.helpers;

import com.play.quiz.dto.AccountDto;
import lombok.Builder;

@Builder
public record AccountInfo(String jwtToken, AccountDto account) {
}
