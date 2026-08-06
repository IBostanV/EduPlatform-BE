package com.play.quiz.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.play.quiz.email.EmailService;
import com.play.quiz.email.helper.EmailMessageFactory;
import com.play.quiz.feed.LevelUpRepository;
import com.play.quiz.mapper.AccountMapper;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.LanguageRepository;
import com.play.quiz.repository.RoleRepository;
import com.play.quiz.repository.UserRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

// Friends' news says when somebody reached a level; experience paid is where that is noticed.
@ExtendWith(MockitoExtension.class)
class LevelUpRecordingTest {

    private static final long ACCOUNT_ID = 7L;

    @Mock private AccountMapper accountMapper;
    @Mock private AccountRepository accountRepository;
    @Mock private AuthenticationFacade authenticationFacade;
    @Mock private EmailMessageFactory emailMessageFactory;
    @Mock private EmailService emailService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserGroupService userGroupService;
    @Mock private VerificationTokenService verificationTokenService;
    @Mock private UserRepository userRepository;
    @Mock private LanguageRepository languageRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private LevelUpRepository levelUpRepository;

    private UserService userService;

    @BeforeEach
    void init() {
        userService = new UserServiceImpl(accountMapper, accountRepository, authenticationFacade,
                emailMessageFactory, emailService, passwordEncoder, userGroupService,
                verificationTokenService, userRepository, languageRepository, roleRepository, levelUpRepository);
    }

    // Level 3 starts at 300: 250 + 100 crosses it once.
    @Test
    void given_experience_that_crosses_a_level_then_record_it() {
        when(accountRepository.findExperience(ACCOUNT_ID)).thenReturn(350);

        userService.addExperience(ACCOUNT_ID, 100);

        verify(levelUpRepository).record(ACCOUNT_ID, 3);
    }

    // 0 to 1000 is levels 2, 3, 4 and 5 in one go: each is somebody's news.
    @Test
    void given_experience_that_crosses_several_levels_then_record_each() {
        when(accountRepository.findExperience(ACCOUNT_ID)).thenReturn(1000);

        userService.addExperience(ACCOUNT_ID, 1000);

        verify(levelUpRepository).record(ACCOUNT_ID, 2);
        verify(levelUpRepository).record(ACCOUNT_ID, 3);
        verify(levelUpRepository).record(ACCOUNT_ID, 4);
        verify(levelUpRepository).record(ACCOUNT_ID, 5);
    }

    @Test
    void given_experience_inside_a_level_then_record_nothing() {
        when(accountRepository.findExperience(ACCOUNT_ID)).thenReturn(150);

        userService.addExperience(ACCOUNT_ID, 40);

        verify(levelUpRepository, never()).record(anyLong(), anyInt());
    }
}
