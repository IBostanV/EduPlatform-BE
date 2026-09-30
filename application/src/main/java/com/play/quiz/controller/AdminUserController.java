package com.play.quiz.controller;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_USER;
import static com.play.quiz.controller.RestEndpoint.USER_ADMIN;

import java.util.List;

import com.play.quiz.record.BlockedStatus;
import com.play.quiz.record.ManagedAccount;
import com.play.quiz.record.ManagedAccountInput;
import com.play.quiz.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Managing the accounts themselves, from the Users tab of the admin dashboard. Kept apart from
// UserController, which is what a signed-in player does with their own account: everything here
// is admin-only (WebSecurity), and it is the only place emails and roles are handed out.
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_USER + USER_ADMIN)
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ManagedAccount>> getAll() {
        return ResponseEntity.ok(userService.getManagedAccounts());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedAccount> create(@Valid @RequestBody final ManagedAccountInput input) {
        return ResponseEntity.ok(userService.createAccount(input));
    }

    // Display name and roles. Not the email or the password: those are the account owner's.
    @PutMapping(value = "/{accountId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedAccount> update(@PathVariable final Long accountId,
                                                 @Valid @RequestBody final ManagedAccountInput input) {
        return ResponseEntity.ok(userService.updateAccount(accountId, input));
    }

    // Blocking keeps the account and everything in it; the owner just cannot sign in any more.
    @PatchMapping(value = "/{accountId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedAccount> setBlocked(@PathVariable final Long accountId,
                                                     @RequestBody final BlockedStatus status) {
        return ResponseEntity.ok(userService.setAccountBlocked(accountId, status.blocked()));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> delete(@PathVariable final Long accountId) {
        userService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}
