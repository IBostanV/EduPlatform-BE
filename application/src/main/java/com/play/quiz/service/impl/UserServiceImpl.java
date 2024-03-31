package com.play.quiz.service.impl;

import com.play.quiz.aop.annotation.Conditional;
import com.play.quiz.domain.Account;
import com.play.quiz.domain.Language;
import com.play.quiz.domain.VerificationToken;
import com.play.quiz.dto.AccountDto;
import com.play.quiz.email.EmailService;
import com.play.quiz.email.helper.EmailMessage;
import com.play.quiz.email.helper.EmailMessageFactory;
import com.play.quiz.enums.UserRole;
import com.play.quiz.exception.EmailSendFailedException;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.exception.UserNotFoundException;
import com.play.quiz.exception.UserUpdateException;
import com.play.quiz.mapper.AccountMapper;
import com.play.quiz.record.PasswordInput;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.UserRepository;
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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    @Override
    @Transactional
    public Account save(final AccountDto accountDto) {
        final Account account = accountMapper.toEntity(accountDto);
        return userRepository.save(account);
    }

    @Override
    @SneakyThrows
    @Transactional
    public Account save(final AccountDto accountDto, final MultipartFile avatar) {
        final Account account = accountMapper.toEntity(accountDto, avatar);
        return userRepository.save(account);
    }

    @Override
    @Transactional
    public Account findByEmail(final @NonNull String userEmail) {
        log.debug("Find user by email: {}", userEmail);
        return userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("No user found with email: " + userEmail));
    }

    @Override
    public List<AccountDto> getAccountList() {
        List<Account> accounts = userRepository.findAll();
        return accountMapper.toDtoList(accounts);
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
                .orElseThrow(() -> new RecordNotFoundException("No records found for token: " + token));

        handleAccountActivation(verificationToken);
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
    }

    private void handleEmailSending(final EmailMessage emailMessage) {
        try {
            log.debug("Sending email to: {}", emailMessage.getTo());
            emailService.sendEmail(emailMessage);
        } catch (MessagingException exception) {
            log.warn(exception.getMessage());
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
        char[] passwordCharArray = passwordEncoder.encode(new String(newPassword.password())).toCharArray();
        String username = authenticationFacade.getPrincipal().getUsername();
        updatePassword(username, passwordCharArray);
    }

    private void updatePassword(String username, char[] passwordCharArray) {
        int updated = userRepository.updateUserPassword(username, passwordCharArray);
        if (updated == 0) {
            throw new UserUpdateException("User password could not be updated");
        }
    }

    @Override
    public Boolean changeLanguage(Language language) {
        String userEmail = authenticationFacade.getPrincipal().getUsername();
        return userRepository.changeLanguage(language, userEmail);
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

    @Override
    @Transactional(readOnly = true)
    public Set<AccountDto> getUserFriends(Long userId) {
        Optional<Account> account = accountRepository.findById(userId);

        if (account.isPresent()) {
            AccountDto accountDto = accountMapper.toDto(account.get());
            return accountDto.getFriends();
        } else {
            log.info("No user found with id: {}", userId);
            return Collections.emptySet();

        }
    }
}
