package com.play.quiz.fixtures;

import java.time.LocalDate;
import java.util.Collections;

import com.play.quiz.dto.AccountDto;

public class AccountDtoFixture {

    private static final String DEFAULT_EMAIL = "email@gmail.com";

    public static AccountDto getEnabledAccountDto() {
        return defaultBuilder()
                .isEnabled(true)
                .email(DEFAULT_EMAIL)
                .build();
    }

    public static AccountDto getDisabledAccountDto() {
        return defaultBuilder()
                .isEnabled(false)
                .email(DEFAULT_EMAIL)
                .build();
    }

    public static AccountDto getAccountDtoWithNoEmail() {
        return defaultBuilder()
                .isEnabled(true)
                .build();
    }

    private static AccountDto.AccountDtoBuilder defaultBuilder() {
        return AccountDto.builder()
                .id(1L)
                .password("Kettle9-blue".toCharArray())
                .name("User")
                .birthday(LocalDate.of(2022, 12, 31))
                .roles(Collections.singletonList(RoleDtoFixture.getUserRole()));
    }
}
