package com.play.quiz.service.impl;

import java.util.Collections;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.helpers.AccountInfo;
import com.play.quiz.dto.AccountDto;
import com.play.quiz.dto.RoleDto;
import com.play.quiz.enums.UserRole;
import com.play.quiz.mapper.AccountMapper;
import com.play.quiz.security.jwt.JwtProvider;
import com.play.quiz.service.AuthenticationService;
import com.play.quiz.service.UserService;
import com.play.quiz.util.SystemAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final AccountMapper accountMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public AccountInfo register(final AccountDto accountDto) {
        log.info("Register user with email: {}", accountDto.getEmail());
        SystemAssert.isAccountUnique(userService.userExists(accountDto), accountDto.getEmail());
        Account account = userService.save(assignRoles(accountDto));
        userService.sendAccountVerificationEmail(account);

        return buildAccountInfo(account, authenticate(accountDto));
    }

    private AccountDto assignRoles(final AccountDto accountDto) {
        RoleDto roleDto = RoleDto.builder()
                .name(UserRole.ROLE_USER)
                .roleId(UserRole.ROLE_USER.getRoleId())
                .build();

        return accountDto.toBuilder()
                .roles(Collections.singletonList(roleDto))
                .build();
    }

    @Override
    public AccountInfo login(final AccountDto accountDto) {
        log.info("Login user with email: {}", accountDto.getEmail());
        Account account = userService.findByEmail(accountDto.getEmail());
        SystemAssert.isAccountEnabled(account.isEnabled(), account.getEmail());
        SystemAssert.isAccountNotBlocked(account.isBlocked(), account.getEmail());
        Authentication authentication = authenticate(accountDto);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return buildAccountInfo(account, authentication);
    }

    private AccountInfo buildAccountInfo(final Account account, final Authentication authentication) {
        return new AccountInfo(jwtProvider.generate(authentication), accountMapper.toDto(account));
    }

    private Authentication authenticate(final AccountDto accountDto) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                accountDto.getEmail(), new String(accountDto.getPassword())));
    }
}
