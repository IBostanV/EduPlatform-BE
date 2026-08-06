package com.play.quiz.record;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Role;
import com.play.quiz.enums.UserRole;

// An account as the admin user list shows it. Unlike UserSummary, which is what one player may
// see of another, this one carries the email, the roles and whether the account is blocked, so it
// is only ever sent from the admin-only endpoints.
//
// displayName is the stored username as it stands, empty included: the edit form writes back what
// it was given, and a filled-in "Player #12" would be saved as if the admin had typed it.
public record ManagedAccount(Long id,
                             String email,
                             String displayName,
                             List<UserRole> roles,
                             boolean blocked,
                             LocalDateTime registeredAt) {

    public static ManagedAccount of(final Account account) {
        return new ManagedAccount(
                account.getAccountId(),
                account.getEmail(),
                account.getUsername(),
                rolesOf(account),
                account.isBlocked(),
                account.getCreatedDate());
    }

    // @SuperBuilder drops the field's "= new ArrayList<>()", so an Account that was built rather
    // than loaded has no roles list at all. An account with no roles is listed, not a 500.
    private static List<UserRole> rolesOf(final Account account) {
        return Objects.isNull(account.getRoles())
                ? List.of()
                : account.getRoles().stream().map(Role::getName).toList();
    }
}
