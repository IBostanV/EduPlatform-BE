package com.play.quiz.group;

import java.util.List;

import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Groups players create and post in. All of it needs an account, which the security chain's
 * catch-all rule already asks for; GroupService decides who may read, post and manage.
 */
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_GROUPS)
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    public record PostInput(String content) {}

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupService.Overview> overview() {
        return ResponseEntity.ok(groupService.overview());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupService.GroupCard> create(@RequestBody final GroupService.GroupInput input) {
        return ResponseEntity.ok(groupService.create(input));
    }

    @GetMapping(value = "/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupService.GroupPage> page(@PathVariable final Long groupId) {
        return ResponseEntity.ok(groupService.page(groupId));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> delete(@PathVariable final Long groupId) {
        groupService.delete(groupId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{groupId}/join", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupService.GroupPage> join(@PathVariable final Long groupId) {
        return ResponseEntity.ok(groupService.join(groupId));
    }

    @PostMapping("/{groupId}/leave")
    public ResponseEntity<Void> leave(@PathVariable final Long groupId) {
        groupService.leave(groupId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{groupId}/members/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupService.GroupPage> approve(@PathVariable final Long groupId, @PathVariable final Long accountId) {
        return ResponseEntity.ok(groupService.approve(groupId, accountId));
    }

    @DeleteMapping(value = "/{groupId}/members/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupService.GroupPage> remove(@PathVariable final Long groupId, @PathVariable final Long accountId) {
        return ResponseEntity.ok(groupService.remove(groupId, accountId));
    }

    @GetMapping(value = "/{groupId}/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GroupService.PostView>> posts(@PathVariable final Long groupId) {
        return ResponseEntity.ok(groupService.posts(groupId));
    }

    @PostMapping(value = "/{groupId}/posts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupService.PostView> post(@PathVariable final Long groupId, @RequestBody final PostInput input) {
        return ResponseEntity.ok(groupService.post(groupId, input.content()));
    }

    @PostMapping(value = "/posts/{postId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupService.CommentView> comment(@PathVariable final Long postId, @RequestBody final PostInput input) {
        return ResponseEntity.ok(groupService.comment(postId, input.content()));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable final Long commentId) {
        groupService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable final Long postId) {
        groupService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
