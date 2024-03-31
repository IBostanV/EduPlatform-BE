package com.play.quiz.service;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Language;
import com.play.quiz.dto.AccountDto;
import com.play.quiz.enums.UserRole;
import com.play.quiz.record.PasswordInput;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface UserService {

    Account save(AccountDto accountDto);

    Account save(AccountDto accountDto, MultipartFile avatar);

    Account findByEmail(String email);

    List<AccountDto> getAccountList();

    boolean userExists(AccountDto accountDto);

    void sendAccountVerificationEmail(Account accountDto);

    void activateAccount(String verificationToken);

    void changePassword(PasswordInput password);

    boolean verifyOldPassword(PasswordInput password);

    Boolean changeLanguage(Language language);

    Set<UserRole> getUserRoles();

    Set<Account> getUsersByUserGroupId(long userGroupId);

    Set<AccountDto> getUserFriends(Long userId);
}
