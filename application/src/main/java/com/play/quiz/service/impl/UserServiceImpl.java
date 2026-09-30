package com.play.quiz.service.impl;

import com.play.quiz.aop.annotation.Conditional;
import com.play.quiz.coin.Coins;
import com.play.quiz.domain.Account;
import com.play.quiz.domain.Language;
import com.play.quiz.domain.VerificationToken;
import com.play.quiz.dto.AccountDto;
import com.play.quiz.email.EmailService;
import com.play.quiz.email.helper.EmailMessage;
import com.play.quiz.email.helper.EmailMessageFactory;
import com.play.quiz.enums.UserRole;
import com.play.quiz.exception.EmailSendFailedException;
import com.play.quiz.feed.LevelUpRepository;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.exception.UserNotFoundException;
import com.play.quiz.exception.UserUpdateException;
import com.play.quiz.domain.Role;
import com.play.quiz.mapper.AccountMapper;
import com.play.quiz.record.ManagedAccount;
import com.play.quiz.record.ManagedAccountInput;
import com.play.quiz.record.PlayerLevel;
import com.play.quiz.record.UserSummary;
import com.play.quiz.record.PasswordInput;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.LanguageRepository;
import com.play.quiz.repository.RoleRepository;
import com.play.quiz.repository.UserRepository;
import com.play.quiz.util.ExperiencePayout;
import com.play.quiz.util.PasswordPolicy;
import com.play.quiz.util.SystemAssert;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserGroupService;
import com.play.quiz.service.UserService;
import com.play.quiz.service.VerificationTokenService;
import jakarta.mail.MessagingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final AuthenticationFacade authenticationFacade;
    private final EmailMessageFactory emailMessageFactory;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserGroupService userGroupService;
    private final VerificationTokenService verificationTokenService;
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;
    private final RoleRepository roleRepository;
    private final LevelUpRepository levelUpRepository;

    // English, the language a new account starts in until its owner changes it.
    private static final long DEFAULT_LANGUAGE_ID = 1L;

    @Override
    @Transactional
    public Account save(final AccountDto accountDto) {
        return save(accountDto, null);
    }

    @Override
    @SneakyThrows
    @Transactional
    public Account save(final AccountDto accountDto, final MultipartFile avatar) {
        if (Objects.isNull(accountDto.getLanguage().getLangId())) {
            Language englishLang = Language.builder().langId(1L).build();
            accountDto.setLanguage(englishLang);
        }

        final Account account = accountMapper.toEntity(accountDto, avatar);
        Account saved = userRepository.save(account);
        log.info("Saved account id: {}, new avatar: {}", saved.getAccountId(), Objects.nonNull(avatar));
        return saved;
    }

    @Override
    @Transactional
    public Account findByEmail(final @NonNull String userEmail) {
        log.debug("Find user by email: {}", userEmail);
        return userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("No user found with email: " + userEmail));
    }

    @Override
    public List<UserSummary> getAccountList() {
        List<Account> accounts = userRepository.findAll();
        return accounts.stream().map(UserSummary::of).toList();
    }

    @Override
    @Transactional
    public boolean userExists(final AccountDto accountDto) {
        return userRepository.findUserByEmail(accountDto.getEmail()).isPresent();
    }

    @Override
    @Transactional
    public void activateAccount(final @NonNull String token) {
        VerificationToken verificationToken = verificationTokenService.findByToken(token)
                .orElseThrow(() -> new RecordNotFoundException("No verification token found"));

        handleAccountActivation(verificationToken);
        log.info("Activated account id: {}", verificationToken.getAccount().getAccountId());
    }

    private void handleAccountActivation(final VerificationToken verificationToken) {
        enableAccount(verificationToken.getAccount());
        updateVerificationToken(verificationToken);
    }

    private void enableAccount(final Account account) {
        account.enable();
        userRepository.enableAccount(account.getAccountId());
    }

    private void updateVerificationToken(final VerificationToken verificationToken) {
        verificationToken.setActivationDate(LocalDateTime.now());
        verificationTokenService.save(verificationToken);
    }

    @Async
    @Override
    @Conditional(property = "application.email.sending.enabled", value = "true", matchIfMissing = true)
    public void sendAccountVerificationEmail(final Account account) {
        VerificationToken verificationToken = verificationTokenService.createVerificationToken(account);
        EmailMessage emailMessage = emailMessageFactory.createAccountVerificationEmailMessage(account, verificationToken);
        handleEmailSending(emailMessage);
        log.info("Sent verification email for account id: {}", account.getAccountId());
    }

    private void handleEmailSending(final EmailMessage emailMessage) {
        try {
            log.debug("Sending email to: {}", emailMessage.getTo());
            emailService.sendEmail(emailMessage);
        } catch (MessagingException exception) {
            log.warn("Sending email failed: {}", exception.getMessage(), exception);
            throw new EmailSendFailedException(exception.getMessage());
        }
    }

    @Override
    public boolean verifyOldPassword(PasswordInput providedPassword) {
        String userStoredPassword = authenticationFacade.getPrincipal().getPassword();
        return passwordEncoder.matches(String.valueOf(providedPassword.password()), userStoredPassword);
    }

    @Override
    public void changePassword(PasswordInput newPassword) {
        PasswordPolicy.requireStrong(newPassword.password(), authenticationFacade.getPrincipal().getUsername());
        char[] passwordCharArray = passwordEncoder.encode(new String(newPassword.password())).toCharArray();
        String username = authenticationFacade.getPrincipal().getUsername();
        updatePassword(username, passwordCharArray);
        log.info("Password changed");
    }

    private void updatePassword(String username, char[] passwordCharArray) {
        int updated = userRepository.updateUserPassword(username, passwordCharArray);
        if (updated == 0) {
            throw new UserUpdateException("User password could not be updated");
        }
    }

    @Override
    public boolean occupationQuizzes() {
        return accountRepository.findOccupationQuizzes(authenticationFacade.getPrincipal().getUsername());
    }

    @Override
    @Transactional
    public boolean setOccupationQuizzes(final boolean enabled) {
        accountRepository.setOccupationQuizzes(authenticationFacade.getPrincipal().getUsername(), enabled);
        log.info("Occupation quizzes set to: {}", enabled);
        return enabled;
    }

    @Override
    public Boolean changeLanguage(Language language) {
        String userEmail = authenticationFacade.getPrincipal().getUsername();
        Boolean changed = userRepository.changeLanguage(language, userEmail);
        log.info("Language change to id: {}, result: {}", language.getLangId(), changed);
        return changed;
    }

    @Override
    public Set<UserRole> getUserRoles() {
        Collection<GrantedAuthority> authorities = authenticationFacade.getPrincipal().getAuthorities();

        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Account> getUsersByUserGroupId(long userGroupId) {
        Set<Long> userIdList = userGroupService.fetchUserIdsByGroupId(userGroupId);
        return userRepository.findByUserIds(userIdList);
    }

    // Always the signed-in user's friends; the old /friends/{userId} let anyone read anyone's.
    @Override
    @Transactional(readOnly = true)
    public List<UserSummary> getCurrentUserFriends() {
        return accountRepository.findFriends(getCurrentAccountId()).stream()
                .map(UserSummary::withPhoto)
                .toList();
    }

    // Friendship is mutual: one add writes both directions, so each sees the other.
    @Override
    @Transactional
    public void addFriend(Long friendId) {
        Long userId = getCurrentAccountId();
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("You cannot add yourself as a friend");
        }
        if (!accountRepository.existsById(friendId)) {
            throw new UserNotFoundException("No user found with id: " + friendId);
        }

        accountRepository.addFriend(userId, friendId);
        accountRepository.addFriend(friendId, userId);
        log.info("Account {} and account {} are now friends", userId, friendId);
    }

    @Override
    @Transactional
    public void removeFriend(Long friendId) {
        Long userId = getCurrentAccountId();
        accountRepository.removeFriend(userId, friendId);
        log.info("Account {} removed friend {}", userId, friendId);
    }

    @Override
    @Transactional
    public void addExperience(final Long accountId, final int amount) {
        if (amount <= 0) {
            log.debug("No experience to award account {}: amount {}", accountId, amount);
            return;
        }
        int coins = Coins.forExperience(amount);
        accountRepository.addExperience(accountId, amount);
        accountRepository.addCoins(accountId, coins);

        // Friends' news says when somebody levelled up, which the experience total cannot. The
        // update above holds the row until commit, so what is read back is exactly what this call
        // left: the total before it is that minus the amount.
        int after = Objects.requireNonNullElse(accountRepository.findExperience(accountId), 0);
        int reached = PlayerLevel.levelFor(after);
        log.info("Account {} gained {} experience ({} -> {}) and {} coins, level {} -> {}",
                accountId, amount, after - amount, after, coins, PlayerLevel.levelFor(after - amount), reached);
        for (int level = PlayerLevel.levelFor(after - amount) + 1; level <= reached; level++) {
            levelUpRepository.record(accountId, level);
        }
    }

    /**
     * The first time a player is seen on a given day: the run of days grows if yesterday was the
     * last one and starts again if it was not, and the day is paid for. Days missed in between
     * are covered by the player's streak freezes, one each, if they hold enough for all of them.
     *
     * <p>Days visited rather than days logged in: the token lasts longer than a day, so counting
     * logins would break the run of anybody who simply stays signed in.
     */
    @Override
    @Transactional
    public Account recordVisit(final String email) {
        Account account = findByEmail(email);
        LocalDate today = LocalDate.now();
        if (today.equals(account.getLastSeenDate())) {
            log.debug("Account {} already recorded a visit today", account.getAccountId());
            return account;
        }

        int missed = account.getLastSeenDate() == null ? 0
                : (int) ChronoUnit.DAYS.between(account.getLastSeenDate(), today) - 1;
        boolean frozen = missed > 0 && account.getStreakFreezes() >= missed;
        int streak = missed == 0 || frozen ? account.getLoginStreak() + 1 : 1;
        if (accountRepository.recordVisit(account.getAccountId(), today, streak) != 1) {
            // Another request got there first this morning; it paid, so this one does not.
            log.info("Visit of account {} already recorded by a concurrent request", account.getAccountId());
            return findByEmail(email);
        }
        // Only the request that won the day gets here, so the freezes cannot be spent twice.
        if (frozen) {
            accountRepository.useStreakFreezes(account.getAccountId(), missed);
            log.info("Account {} kept its streak with {} freeze(s)", account.getAccountId(), missed);
        } else if (missed > 0) {
            log.info("Account {} streak reset: missed {} day(s), had {} freeze(s), streak was {}",
                    account.getAccountId(), missed, account.getStreakFreezes(), account.getLoginStreak());
        }

        addExperience(account.getAccountId(), ExperiencePayout.forVisit(streak));
        log.info("Account {} visited on day {} of a run", account.getAccountId(), streak);

        return findByEmail(email);
    }

    private Long getCurrentAccountId() {
        return findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
    }

    // ---- Admin account management -----------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<ManagedAccount> getManagedAccounts() {
        return accountRepository.findAllWithRoles().stream().map(ManagedAccount::of).toList();
    }

    @Override
    @Transactional
    public ManagedAccount createAccount(final ManagedAccountInput input) {
        if (Objects.isNull(input.password()) || input.password().length == 0) {
            throw new IllegalArgumentException("A new account needs a password");
        }
        SystemAssert.isAccountUnique(accountRepository.findByEmail(input.email()).isPresent(), input.email());

        Account account = Account.builder()
                .email(input.email())
                .username(input.displayName())
                .password(passwordEncoder.encode(new String(input.password())).toCharArray())
                // No verification email: an admin adding the account is the vouching.
                .isEnabled(true)
                .language(languageRepository.findById(DEFAULT_LANGUAGE_ID).orElse(null))
                .roles(rolesOf(input))
                // Q_USER.CREATED_DATE is NOT NULL, and the pre-save aspect only covers the JDBC
                // repository, not this one.
                .createdDate(LocalDateTime.now())
                .build();

        Account saved = accountRepository.save(account);
        log.info("Admin created account id: {}, roles: {}", saved.getAccountId(), input.roles());
        return ManagedAccount.of(saved);
    }

    @Override
    @Transactional
    public ManagedAccount updateAccount(final Long accountId, final ManagedAccountInput input) {
        Account account = getAccount(accountId);

        // The email is the owner's sign-in name, so an admin cannot change it: whatever email the
        // form sends is ignored and the account keeps its own.
        account.setUsername(input.displayName());
        account.setRoles(rolesOf(input));
        account.setUpdatedDate(LocalDateTime.now());

        ManagedAccount updated = ManagedAccount.of(accountRepository.save(account));
        log.info("Admin updated account id: {}, roles: {}", accountId, input.roles());
        return updated;
    }

    @Override
    @Transactional
    public ManagedAccount setAccountBlocked(final Long accountId, final boolean blocked) {
        refuseOnSelf(accountId, "You cannot block your own account");
        Account account = getAccount(accountId);

        account.setIsBlocked(blocked);
        account.setUpdatedDate(LocalDateTime.now());

        ManagedAccount updated = ManagedAccount.of(accountRepository.save(account));
        log.info("Admin set account id: {} blocked: {}", accountId, blocked);
        return updated;
    }

    // Everything the account left behind (quizzes, history, messages) is tied to it by a foreign
    // key, so the database refuses this for anyone who has played; the error reaches the admin,
    // who can block the account instead.
    @Override
    @Transactional
    public void deleteAccount(final Long accountId) {
        refuseOnSelf(accountId, "You cannot delete your own account");
        accountRepository.delete(getAccount(accountId));
        log.info("Admin deleted account id: {}", accountId);
    }

    @Override
    @Transactional
    public void fillSocialProfile(final String email, final LocalDate birthday, final Supplier<byte[]> photo) {
        accountRepository.findByEmail(email).ifPresentOrElse(account -> {
            boolean fillBirthday = Objects.isNull(account.getBirthday());
            boolean fillAvatar = Objects.isNull(account.getAvatar());
            if (fillBirthday) {
                account.setBirthday(birthday);
            }
            if (fillAvatar) {
                account.setAvatar(photo.get());
            }
            log.debug("Social profile for account {}: birthday filled: {}, avatar filled: {}",
                    account.getAccountId(), fillBirthday, fillAvatar);
        }, () -> log.warn("No account found to fill the social profile of"));
    }

    @Override
    public Account getProfileAccount(final Long accountId) {
        return accountRepository.findProfileByAccountId(accountId)
                .orElseThrow(() -> new UserNotFoundException("No user found with id: " + accountId));
    }

    private Account getAccount(final Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new UserNotFoundException("No user found with id: " + accountId));
    }

    // The admin pages hide these buttons on the signed-in admin's own row; this is the same rule
    // where it counts, on the endpoint itself, so nobody locks themselves out with a raw request.
    private void refuseOnSelf(final Long accountId, final String message) {
        if (Objects.equals(accountId, getCurrentAccountId())) {
            throw new IllegalArgumentException(message);
        }
    }

    // Managed Role rows: the join table write needs them attached, not rebuilt from the enum.
    private List<Role> rolesOf(final ManagedAccountInput input) {
        List<Long> roleIds = input.roles().stream().map(UserRole::getRoleId).toList();
        List<Role> roles = roleRepository.findAllById(roleIds);

        if (roles.size() != roleIds.size()) {
            throw new RecordNotFoundException("Unknown role in: " + input.roles());
        }
        return roles;
    }
}
