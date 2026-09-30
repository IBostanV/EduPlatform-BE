package com.play.quiz.social;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.play.quiz.controller.RestEndpoint;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Playing with others without being online together: challenges, the daily challenge, a group's
 * table, reactions on friends' news, and which friends are on the site. All of it needs an
 * account, which the security chain's catch-all rule already asks for.
 */
@Validated
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_SOCIAL)
@RequiredArgsConstructor
public class SocialController {

    private final ChallengeService challengeService;
    private final DailyChallengeService dailyChallengeService;
    private final GroupLeaderboardService groupLeaderboardService;
    private final ReactionService reactionService;
    private final Presence presence;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    public record ChallengeInput(@NotNull Long historyId, @NotEmpty Set<Long> friendIds) {}

    public record ReactionInput(@NotNull String key, @NotNull Reaction.Kind kind) {}

    /** A friend on the site, and what they are doing there if it is something to join. */
    public record OnlineFriend(Long id, Presence.Activity activity) {}

    @PostMapping(value = "/challenges", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChallengeService.ChallengeView>> challenge(@RequestBody @Validated final ChallengeInput input) {
        return ResponseEntity.ok(challengeService.send(input.historyId(), input.friendIds()));
    }

    @GetMapping(value = "/challenges", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChallengeService.Challenges> challenges() {
        return ResponseEntity.ok(challengeService.mine());
    }

    @GetMapping(value = "/challenges/{challengeId}/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizDto> challengeQuiz(@PathVariable final Long challengeId) {
        return ResponseEntity.ok(challengeService.quiz(challengeId));
    }

    @GetMapping(value = "/daily", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DailyChallengeService.Status> daily() {
        return ResponseEntity.ok(dailyChallengeService.status());
    }

    @GetMapping(value = "/daily/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizDto> dailyQuiz() {
        return ResponseEntity.ok(dailyChallengeService.quiz());
    }

    @GetMapping(value = "/groups/{groupId}/leaderboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GroupLeaderboardService.Row>> leaderboard(
            @PathVariable final Long groupId,
            @RequestParam(defaultValue = "WEEK") final GroupLeaderboardService.Period period) {
        return ResponseEntity.ok(groupLeaderboardService.leaderboard(groupId, period));
    }

    /** Tallies for the given news lines: ?keys=FRIEND_POST-12,FRIEND_CONQUEST-34-5 */
    @GetMapping(value = "/reactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<ReactionService.Tally>>> reactions(@RequestParam final String keys) {
        return ResponseEntity.ok(reactionService.tallies(Arrays.asList(keys.split(","))));
    }

    @PostMapping(value = "/reactions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReactionService.Tally>> react(@RequestBody @Validated final ReactionInput input) {
        return ResponseEntity.ok(reactionService.toggle(input.key(), input.kind()));
    }

    @GetMapping(value = "/online", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<List<OnlineFriend>> onlineFriends() {
        Long me = userService.findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
        return ResponseEntity.ok(accountRepository.findFriends(me).stream()
                .filter(friend -> presence.isOnline(friend.getEmail()))
                .map(friend -> new OnlineFriend(friend.getAccountId(), presence.activityOf(friend.getEmail()).orElse(null)))
                .toList());
    }
}
