package com.play.quiz.service;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Language;
import com.play.quiz.dto.AccountDto;
import com.play.quiz.enums.UserRole;
import com.play.quiz.record.ManagedAccount;
import com.play.quiz.record.ManagedAccountInput;
import com.play.quiz.record.UserSummary;
import com.play.quiz.record.PasswordInput;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public interface UserService {

    Account save(AccountDto accountDto);

    Account save(AccountDto accountDto, MultipartFile avatar);

    Account findByEmail(String email);

    List<UserSummary> getAccountList();

    boolean userExists(AccountDto accountDto);

    void sendAccountVerificationEmail(Account accountDto);

    void activateAccount(String verificationToken);

    void changePassword(PasswordInput password);

    boolean verifyOldPassword(PasswordInput password);

    Boolean changeLanguage(Language language);

    /** Whether the signed-in player's express quizzes lean to their occupations. */
    boolean occupationQuizzes();

    boolean setOccupationQuizzes(boolean enabled);

    Set<UserRole> getUserRoles();

    Set<Account> getUsersByUserGroupId(long userGroupId);

    List<UserSummary> getCurrentUserFriends();

    void addFriend(Long friendId);

    void removeFriend(Long friendId);

    /** Adds to what an account has collected, which is what its level is worked out from. */
    void addExperience(Long accountId, int amount);

    /**
     * Marks today as visited and keeps the run of consecutive days, paying for the first visit of
     * each day. Called wherever the signed-in player is read, so simply turning up counts.
     */
    Account recordVisit(String email);

    // Admin-only account management. Everything below is reachable from /api/user/admin, which
    // WebSecurity keeps to ROLE_ADMIN.

    List<ManagedAccount> getManagedAccounts();

    ManagedAccount createAccount(ManagedAccountInput input);

    ManagedAccount updateAccount(Long accountId, ManagedAccountInput input);

    ManagedAccount setAccountBlocked(Long accountId, boolean blocked);

    void deleteAccount(Long accountId);

    /**
     * Fills the birthday and photo of this account from a social sign-in, where they are still
     * empty: what the player set themselves is never overwritten. The photo is only fetched when needed.
     */
    void fillSocialProfile(String email, LocalDate birthday, Supplier<byte[]> photo);

    /** The account with this id, occupations and favourite categories loaded, for its profile page. */
    Account getProfileAccount(Long accountId);
}
