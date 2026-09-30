package com.play.quiz.fixtures;

import java.util.Collections;
import java.util.List;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Role;
import com.play.quiz.enums.UserRole;

public class AccountFixture {

    public static Account getNoRolesAccount() {
        return defaultBuilder()
                .roles(Collections.emptyList())
                .isEnabled(true)
                .build();
    }
    public static Account getAdminAccount() {
        return defaultBuilder()
                .isEnabled(true)
                .build();
    }

    public static Account getDisabledAdminAccount() {
        return defaultBuilder()
                .isEnabled(false)
                .build();
    }

    private static Account.AccountBuilder defaultBuilder() {
        return Account.builder()
                .accountId(1L)
                .email("vanyok93@yahoo.com")
                .password("{noop}Kettle9-blue".toCharArray())
                .roles(List.of(getUserRole(), getAdminRole()));
    }

    private static Role getUserRole() {
        return new Role(1L, UserRole.ROLE_USER);
    }

    private static Role getAdminRole() {
        return new Role(1L, UserRole.ROLE_ADMIN);
    }
}
