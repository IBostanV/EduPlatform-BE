package com.play.quiz.controller;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Language;
import com.play.quiz.dto.AccountDto;
import com.play.quiz.dto.UserGroupDto;
import com.play.quiz.dto.UserOccupationDto;
import com.play.quiz.enums.UserRole;
import com.play.quiz.iq.IqService;
import com.play.quiz.mapper.AccountMapper;
import com.play.quiz.record.CreateGroupInput;
import com.play.quiz.record.UserSummary;
import com.play.quiz.record.MuteInput;
import com.play.quiz.record.PasswordInput;
import com.play.quiz.record.PublicProfile;
import com.play.quiz.enums.ProfileVisibility;
import com.play.quiz.profile.ProfileActivityService;
import com.play.quiz.service.UserGroupService;
import com.play.quiz.service.UserOccupationService;
import com.play.quiz.service.UserService;
import com.play.quiz.trophy.TrophyCatalog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import static com.play.quiz.controller.RestEndpoint.REQUEST_MAPPING_USER;

@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + REQUEST_MAPPING_USER)
@RequiredArgsConstructor
public class UserController {
    private final AccountMapper accountMapper;
    private final UserService userService;
    private final UserGroupService userGroupService;
    private final UserOccupationService userOccupationService;
    private final TrophyCatalog trophyCatalog;
    private final IqService iqService;
    private final ProfileActivityService profileActivityService;

    public record VisibilityInput(ProfileVisibility visibility) {}

    // Reading the signed-in player is also what marks the day as visited: it is asked for on
    // every page, which is what "visited today" means. The day is the player's own (PlayerZone).
    @GetMapping(value = "/get-current-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> getUser(Principal principal,
                                              @RequestHeader(value = PlayerZone.HEADER, required = false) String timeZone) {
        Account account = userService.recordVisit(principal.getName(), PlayerZone.of(timeZone));
        AccountDto dto = accountMapper.toDto(account);
        dto.setTrophy(trophyCatalog.faceOf(account.getPreferredTrophy()).orElse(null));

        return ResponseEntity.ok(dto);
    }

    // Another player's profile, read-only; PublicProfile is what it holds and what it leaves out.
    @GetMapping(value = "/{accountId}/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublicProfile> getProfile(@PathVariable final Long accountId) {
        Account account = userService.getProfileAccount(accountId);
        return ResponseEntity.ok(PublicProfile.of(account,
                trophyCatalog.faceOf(account.getPreferredTrophy()).orElse(null),
                iqService.latest(accountId).orElse(null)));
    }

    // What the player has been doing, if they let the reader see it (ProfileActivityService).
    @GetMapping(value = "/{accountId}/activity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileActivityService.ProfileActivity> getActivity(@PathVariable final Long accountId) {
        return ResponseEntity.ok(profileActivityService.activity(accountId));
    }

    @PutMapping(value = "/profile-visibility", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> setProfileVisibility(@RequestBody final VisibilityInput input) {
        userService.setProfileVisibility(input.visibility());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserSummary>> getAccountList() {
        return ResponseEntity.ok(userService.getAccountList());
    }

    @GetMapping(value = "/occupations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserOccupationDto>> getOccupationList() {
        return ResponseEntity.ok(userOccupationService.getAllOccupations());
    }

    // Whether express quizzes lean to the player's occupations; a switch on the profile page.
    @GetMapping(value = "/occupation-quizzes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> getOccupationQuizzes() {
        return ResponseEntity.ok(userService.occupationQuizzes());
    }

    @PutMapping(value = "/occupation-quizzes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> setOccupationQuizzes(@RequestBody final boolean enabled) {
        return ResponseEntity.ok(userService.setOccupationQuizzes(enabled));
    }

    @GetMapping(value = "/get-user-roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<UserRole>> getUserRoles() {
        return ResponseEntity.ok(userService.getUserRoles());
    }

    @GetMapping(value = "/get-user-groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<UserGroupDto>> getUserGroups() {
        return ResponseEntity.ok(userGroupService.getCurrentUserGroups());
    }

    // Returns the new group's id, so the client can open /chat/{groupId} straight away.
    @PostMapping(value = "/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createGroup(@RequestBody CreateGroupInput input) {
        return ResponseEntity.ok(userGroupService.createGroup(input.name(), input.participantIds()));
    }

    // Ids of the groups the signed-in user has muted.
    @GetMapping(value = "/groups/muted", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Long>> getMutedGroups() {
        return ResponseEntity.ok(userGroupService.getMutedGroupIds());
    }

    // The group's picture, for every member to see; sending no file clears it.
    @PutMapping(value = "/groups/{groupId}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> setGroupPhoto(@PathVariable Long groupId,
                                              @RequestPart(required = false) final MultipartFile photo) {
        userGroupService.setPhoto(groupId, photo);
        return ResponseEntity.noContent().build();
    }

    // Mute or unmute one group for the signed-in user; other members are unaffected.
    @PutMapping("/groups/{groupId}/mute")
    public ResponseEntity<Void> setGroupMuted(@PathVariable Long groupId, @RequestBody MuteInput input) {
        userGroupService.setMuted(groupId, input.muted());
        return ResponseEntity.noContent().build();
    }

    // Deletes the group and its messages for every member; only a member may do it.
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        userGroupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    // The signed-in user's friends; no user id in the path, so nobody can list someone else's.
    @GetMapping(value = "/friends", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserSummary>> getFriends() {
        return ResponseEntity.ok(userService.getCurrentUserFriends());
    }

    @PostMapping("/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable Long friendId) {
        userService.addFriend(friendId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long friendId) {
        userService.removeFriend(friendId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AccountDto> saveUserProfileInfo(@RequestPart(name = "request") final AccountDto accountDto,
                                                          @RequestPart(required = false) final MultipartFile avatar) {
        Account account = userService.save(accountDto, avatar);
        return ResponseEntity.ok(accountMapper.toDto(account));
    }

    @PostMapping(value = "/verify-password")
    public ResponseEntity<Boolean> verifyPassword(@RequestBody PasswordInput password) {
        return ResponseEntity.ok(userService.verifyOldPassword(password));
    }

    @PostMapping(value = "/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(@RequestBody PasswordInput password) {
        userService.changePassword(password);
    }

    @PostMapping("/tour-seen")
    public ResponseEntity<Void> markTourSeen() {
        userService.markTourSeen();
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/change-language")
    public ResponseEntity<Boolean> changeLanguage(@RequestBody Language language) {
        return ResponseEntity.ok(userService.changeLanguage(language));
    }
}
