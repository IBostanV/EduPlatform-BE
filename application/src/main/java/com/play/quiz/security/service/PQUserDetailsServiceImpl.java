package com.play.quiz.security.service;

import java.util.Collections;
import java.util.List;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Role;
import com.play.quiz.enums.UserRole;
import com.play.quiz.exception.UserNotFoundException;
import com.play.quiz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Log4j2
@Service("quizUserDetailsService")
@RequiredArgsConstructor
public class PQUserDetailsServiceImpl implements PQUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadByEmail(String userEmail) {
        final Account account = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("No user found with email: " + userEmail));
        return buildUserDetails(account);
    }

    private static UserDetails buildUserDetails(final Account account) {
        return User.builder()
                .username(account.getEmail())
                .password(String.copyValueOf(account.getPassword()))
                .authorities(getUserAuthorities(account))
                // A blocked account is refused at login, and the token filter drops the tokens
                // already out there rather than waiting for each to expire.
                .disabled(account.isBlocked())
                .build();
    }

    private static List<? extends GrantedAuthority> getUserAuthorities(final Account account) {
        if (CollectionUtils.isEmpty(account.getRoles())) {
            log.info("No roles specified. Give default [{}] for user: {}", UserRole.ROLE_USER.name(), account.getEmail());
            return Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.name()));
        }

        log.debug("Given user roles: {}", account.getRoles());
        return account.getRoles().stream()
                .map(Role::getName)
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String emailAsUsername) throws UsernameNotFoundException {
        return loadByEmail(emailAsUsername);
    }
}
