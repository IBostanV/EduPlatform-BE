package com.play.quiz.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Role;
import com.play.quiz.email.EmailService;
import com.play.quiz.email.helper.EmailMessageFactory;
import com.play.quiz.enums.UserRole;
import com.play.quiz.exception.DuplicateUserException;
import com.play.quiz.mapper.AccountMapper;
import com.play.quiz.record.ManagedAccount;
import com.play.quiz.record.ManagedAccountInput;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.LanguageRepository;
import com.play.quiz.repository.RoleRepository;
import com.play.quiz.repository.UserRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.impl.UserServiceImpl;
import com.play.quiz.feed.LevelUpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

// The admin Users tab: adding an account, editing one, and the two things that must never happen
// from it — a second account on one email, and an admin shutting themselves out.
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdminAccountManagementTest {

    private static final String ADMIN_EMAIL = "admin@playquiz.io";
    private static final long ADMIN_ID = 7L;

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

        // Who is signed in, for the "not on yourself" rules.
        when(authenticationFacade.getPrincipal()).thenReturn(new User(ADMIN_EMAIL, "encoded", List.of()));
        when(userRepository.findUserByEmail(ADMIN_EMAIL))
                .thenReturn(Optional.of(Account.builder().accountId(ADMIN_ID).email(ADMIN_EMAIL).build()));

        when(accountRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> "encoded:" + invocation.getArgument(0));
        when(roleRepository.findAllById(List.of(UserRole.ROLE_USER.getRoleId())))
                .thenReturn(List.of(Role.builder().roleId(UserRole.ROLE_USER.getRoleId())
                        .name(UserRole.ROLE_USER).build()));
    }

    @Test
    void given_new_account_when_create_then_store_it_enabled_with_an_encoded_password() {
        when(accountRepository.findByEmail("player@playquiz.io")).thenReturn(Optional.empty());

        ManagedAccount created = userService.createAccount(input("player@playquiz.io", "Quizmaster"));

        assertEquals("player@playquiz.io", created.email());
        assertEquals("Quizmaster", created.displayName());
        assertEquals(List.of(UserRole.ROLE_USER), created.roles());
        // An account an admin adds is not made to answer a verification email.
        assertEquals(false, created.blocked());

        Account saved = savedAccount();
        assertTrue(saved.isEnabled());
        assertNotEquals("s3cret-enough", String.valueOf(saved.getPassword()));
        assertEquals("encoded:s3cret-enough", String.valueOf(saved.getPassword()));
    }

    @Test
    void given_a_used_email_when_create_then_refuse() {
        when(accountRepository.findByEmail("taken@playquiz.io"))
                .thenReturn(Optional.of(Account.builder().accountId(3L).build()));

        assertThrows(DuplicateUserException.class,
                () -> userService.createAccount(input("taken@playquiz.io", "Someone")));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void given_no_password_when_create_then_refuse() {
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.createAccount(
                new ManagedAccountInput("player@playquiz.io", "Quizmaster", null, List.of(UserRole.ROLE_USER))));
    }

    @Test
    void given_another_email_when_update_then_keep_their_own() {
        when(accountRepository.findById(4L))
                .thenReturn(Optional.of(Account.builder().accountId(4L).email("four@playquiz.io").build()));

        ManagedAccount updated = userService.updateAccount(4L, input("five@playquiz.io", "Four"));

        assertEquals("four@playquiz.io", updated.email());
    }

    @Test
    void given_their_own_email_when_update_then_allow_it() {
        Account account = Account.builder().accountId(4L).email("four@playquiz.io").build();
        when(accountRepository.findById(4L)).thenReturn(Optional.of(account));

        ManagedAccount updated = userService.updateAccount(4L, input("four@playquiz.io", "Renamed"));

        assertEquals("Renamed", updated.displayName());
        assertEquals(List.of(UserRole.ROLE_USER), updated.roles());
    }

    @Test
    void given_their_own_account_when_block_or_delete_then_refuse() {
        assertThrows(IllegalArgumentException.class, () -> userService.setAccountBlocked(ADMIN_ID, true));
        assertThrows(IllegalArgumentException.class, () -> userService.deleteAccount(ADMIN_ID));

        verify(accountRepository, never()).save(any());
        verify(accountRepository, never()).delete(any());
    }

    @Test
    void given_another_account_when_block_then_store_the_flag() {
        when(accountRepository.findById(4L))
                .thenReturn(Optional.of(Account.builder().accountId(4L).email("four@playquiz.io").build()));

        assertTrue(userService.setAccountBlocked(4L, true).blocked());
        assertTrue(savedAccount().isBlocked());
    }

    private Account savedAccount() {
        org.mockito.ArgumentCaptor<Account> saved = org.mockito.ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(saved.capture());
        return saved.getValue();
    }

    private static ManagedAccountInput input(final String email, final String displayName) {
        return new ManagedAccountInput(email, displayName, "s3cret-enough".toCharArray(),
                List.of(UserRole.ROLE_USER));
    }
}
