package com.play.quiz.group;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.play.quiz.domain.Account;
import com.play.quiz.enums.UserRole;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.feed.FeedItem;
import com.play.quiz.record.UserSummary;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import com.play.quiz.util.ServerText;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Groups players make and post in. Anyone signed in may create one and becomes its owner. A public
 * group is read and joined by anyone; a private one shows outsiders its card (name, description,
 * owner, size) and nothing else, and joining it is a request its owner approves or turns down.
 * Only members post and comment. A post or a comment comes down by its author, the group's owner
 * or an admin; the group itself by its owner or an admin.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class GroupService {

    // The columns' sizes; longer would fail in the database instead.
    static final int MAX_NAME = 80;
    static final int MAX_DESCRIPTION = 500;
    static final int MAX_POST = 4000;
    static final int MAX_COMMENT = 1000;
    // ponytail: the newest posts only, no paging; add a "before" cursor when groups get busy.
    static final int POSTS_SHOWN = 100;

    /** Where the reader stands with a group; null when they are not in it at all. */
    public enum Role { OWNER, MEMBER, PENDING }

    public record GroupCard(Long id, String name, String description, boolean privateGroup, long members,
                            UserSummary owner, Role role) {}

    /** A group as its page shows it. Members only for those who may read it; requests only for the owner. */
    public record GroupPage(GroupCard group, boolean canRead, List<UserSummary> members, List<UserSummary> pending) {}

    /** {@code key} is the post's reaction key, the way news lines have theirs. Comments oldest first. */
    public record PostView(Long id, String key, UserSummary author, String content, LocalDateTime at,
                           boolean canDelete, List<CommentView> comments) {}

    public record CommentView(Long id, UserSummary author, String content, LocalDateTime at, boolean canDelete) {}

    public record GroupInput(String name, String description, boolean privateGroup) {}

    /** The reader's own groups (owned, joined, asked to join), and every other group to discover. */
    public record Overview(List<GroupCard> mine, List<GroupCard> others) {}

    public static String postKey(final Long postId) {
        return "GROUP_POST-" + postId;
    }

    private final SocialGroupRepository groupRepository;
    private final GroupMemberRepository memberRepository;
    private final GroupPostRepository postRepository;
    private final GroupPostCommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    @Transactional(readOnly = true)
    public Overview overview() {
        Long me = currentAccountId();
        List<GroupCard> cards = cards(groupRepository.findAllByOrderByCreatedDateDesc(), me);
        return new Overview(
                cards.stream().filter(card -> Objects.nonNull(card.role())).toList(),
                cards.stream().filter(card -> Objects.isNull(card.role())).toList());
    }

    @Transactional(readOnly = true)
    public GroupPage page(final Long groupId) {
        Long me = currentAccountId();
        SocialGroup group = group(groupId);
        GroupCard card = cards(List.of(group), me).getFirst();
        boolean canRead = canRead(group, card.role());

        List<GroupMember> rows = memberRepository.findByGroupIdOrderByJoinedDate(groupId);
        Map<Long, Account> accounts = accounts(rows.stream().map(GroupMember::getAccountId).toList());
        Function<GroupMember.Status, List<UserSummary>> with = status -> rows.stream()
                .filter(row -> row.getStatus() == status)
                .map(row -> accounts.get(row.getAccountId()))
                .filter(Objects::nonNull)
                .map(UserSummary::of)
                .toList();

        return new GroupPage(card, canRead,
                canRead ? with.apply(GroupMember.Status.MEMBER) : List.of(),
                card.role() == Role.OWNER ? with.apply(GroupMember.Status.PENDING) : List.of());
    }

    @Transactional
    public GroupCard create(final GroupInput input) {
        String name = Optional.ofNullable(input).map(GroupInput::name).map(String::strip).orElse("");
        if (name.isEmpty()) {
            throw new IllegalArgumentException(ServerText.t("err_group_needs_name", "A group needs a name"));
        }
        String description = Optional.ofNullable(input.description()).map(String::strip)
                .filter(text -> !text.isEmpty())
                .orElse(null);
        if (name.length() > MAX_NAME || (Objects.nonNull(description) && description.length() > MAX_DESCRIPTION)) {
            throw new IllegalArgumentException(ServerText.t("err_group_too_long",
                    "A group can have at most {{name}} characters of name and {{text}} of description",
                    "name", MAX_NAME, "text", MAX_DESCRIPTION));
        }
        Long me = currentAccountId();
        LocalDateTime now = LocalDateTime.now();
        SocialGroup group = groupRepository.save(SocialGroup.builder()
                .name(name)
                .description(description)
                .privateGroup(input.privateGroup())
                .ownerId(me)
                .createdDate(now)
                .build());
        memberRepository.save(GroupMember.builder()
                .groupId(group.getGroupId())
                .accountId(me)
                .status(GroupMember.Status.MEMBER)
                .joinedDate(now)
                .build());
        log.info("Account {} created group {} (private: {})", me, group.getGroupId(), group.isPrivateGroup());
        return cards(List.of(group), me).getFirst();
    }

    @Transactional
    public void delete(final Long groupId) {
        SocialGroup group = group(groupId);
        if (!isAdmin() && !Objects.equals(group.getOwnerId(), currentAccountId())) {
            throw new AccessDeniedException(ServerText.t("err_group_owner_only", "Only the group's owner can do that"));
        }
        // Members and posts go with it (ON DELETE CASCADE).
        groupRepository.delete(group);
        log.info("Group {} deleted", groupId);
    }

    /** Joins a public group at once; asks to join a private one. Joining twice changes nothing. */
    @Transactional
    public GroupPage join(final Long groupId) {
        Long me = currentAccountId();
        SocialGroup group = group(groupId);
        if (memberRepository.findByGroupIdAndAccountId(groupId, me).isEmpty()) {
            GroupMember.Status status = group.isPrivateGroup() ? GroupMember.Status.PENDING : GroupMember.Status.MEMBER;
            memberRepository.save(GroupMember.builder()
                    .groupId(groupId)
                    .accountId(me)
                    .status(status)
                    .joinedDate(LocalDateTime.now())
                    .build());
            log.info("Account {} {} group {}", me, status == GroupMember.Status.PENDING ? "asked to join" : "joined", groupId);
        }
        return page(groupId);
    }

    /** Leaves the group, or takes back a request to join it. The owner cannot leave: they delete it. */
    @Transactional
    public void leave(final Long groupId) {
        Long me = currentAccountId();
        if (Objects.equals(group(groupId).getOwnerId(), me)) {
            throw new IllegalArgumentException(ServerText.t("err_group_owner_leave", "The owner cannot leave the group; delete it instead"));
        }
        memberRepository.findByGroupIdAndAccountId(groupId, me).ifPresent(memberRepository::delete);
        log.info("Account {} left group {}", me, groupId);
    }

    /** The owner lets in someone who asked to join. */
    @Transactional
    public GroupPage approve(final Long groupId, final Long accountId) {
        requireOwner(group(groupId));
        GroupMember row = memberRepository.findByGroupIdAndAccountId(groupId, accountId)
                .filter(found -> found.getStatus() == GroupMember.Status.PENDING)
                .orElseThrow(() -> new RecordNotFoundException("No request from account " + accountId + " to join group " + groupId));
        row.setStatus(GroupMember.Status.MEMBER);
        row.setJoinedDate(LocalDateTime.now());
        log.info("Account {} let into group {}", accountId, groupId);
        return page(groupId);
    }

    /** The owner removes a member, or turns down a request to join. */
    @Transactional
    public GroupPage remove(final Long groupId, final Long accountId) {
        SocialGroup group = group(groupId);
        requireOwner(group);
        if (Objects.equals(group.getOwnerId(), accountId)) {
            throw new IllegalArgumentException(ServerText.t("err_group_owner_leave", "The owner cannot leave the group; delete it instead"));
        }
        memberRepository.findByGroupIdAndAccountId(groupId, accountId).ifPresent(memberRepository::delete);
        log.info("Account {} removed from group {}", accountId, groupId);
        return page(groupId);
    }

    @Transactional(readOnly = true)
    public List<PostView> posts(final Long groupId) {
        Long me = currentAccountId();
        SocialGroup group = group(groupId);
        if (!canRead(group, roleOf(group, me))) {
            throw new AccessDeniedException(ServerText.t("err_group_private", "Only members can read this group"));
        }
        List<GroupPost> posts = postRepository.findByGroupIdOrderByCreatedDateDesc(groupId, PageRequest.of(0, POSTS_SHOWN));
        // ponytail: every comment of every shown post, no paging; fold long threads when they appear.
        Map<Long, List<GroupPostComment>> comments = commentRepository
                .findByPostIdInOrderByCreatedDate(posts.stream().map(GroupPost::getPostId).toList()).stream()
                .collect(Collectors.groupingBy(GroupPostComment::getPostId));
        Map<Long, Account> authors = accounts(Stream.concat(
                posts.stream().map(GroupPost::getAccountId),
                comments.values().stream().flatMap(List::stream).map(GroupPostComment::getAccountId)).toList());
        boolean moderator = isAdmin() || Objects.equals(group.getOwnerId(), me);
        return posts.stream()
                .map(post -> view(post, authors.get(post.getAccountId()), moderator || Objects.equals(post.getAccountId(), me),
                        comments.getOrDefault(post.getPostId(), List.of()).stream()
                                .map(comment -> commentView(comment, authors.get(comment.getAccountId()),
                                        moderator || Objects.equals(comment.getAccountId(), me)))
                                .toList()))
                .toList();
    }

    @Transactional
    public PostView post(final Long groupId, final String content) {
        Long me = currentAccountId();
        SocialGroup group = group(groupId);
        Role role = roleOf(group, me);
        if (role != Role.OWNER && role != Role.MEMBER) {
            throw new AccessDeniedException(ServerText.t("err_group_members_post", "Only members can post in this group"));
        }
        String text = Optional.ofNullable(content).map(String::strip).orElse("");
        if (text.isEmpty() || text.length() > MAX_POST) {
            throw new IllegalArgumentException(ServerText.t("err_group_post_length",
                    "A post needs some text, at most {{max}} characters", "max", MAX_POST));
        }
        GroupPost post = postRepository.save(GroupPost.builder()
                .groupId(groupId)
                .accountId(me)
                .content(text)
                .createdDate(LocalDateTime.now())
                .build());
        log.info("Account {} posted {} in group {}", me, post.getPostId(), groupId);
        return view(post, accountRepository.findById(me).orElse(null), true, List.of());
    }

    @Transactional
    public CommentView comment(final Long postId, final String content) {
        Long me = currentAccountId();
        GroupPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RecordNotFoundException("No group post with id: " + postId));
        Role role = roleOf(group(post.getGroupId()), me);
        if (role != Role.OWNER && role != Role.MEMBER) {
            throw new AccessDeniedException(ServerText.t("err_group_members_comment", "Only members can comment in this group"));
        }
        String text = Optional.ofNullable(content).map(String::strip).orElse("");
        if (text.isEmpty() || text.length() > MAX_COMMENT) {
            throw new IllegalArgumentException(ServerText.t("err_group_comment_length",
                    "A comment needs some text, at most {{max}} characters", "max", MAX_COMMENT));
        }
        GroupPostComment comment = commentRepository.save(GroupPostComment.builder()
                .postId(postId)
                .accountId(me)
                .content(text)
                .createdDate(LocalDateTime.now())
                .build());
        log.info("Account {} commented {} on group post {}", me, comment.getCommentId(), postId);
        return commentView(comment, accountRepository.findById(me).orElse(null), true);
    }

    @Transactional
    public void deleteComment(final Long commentId) {
        GroupPostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RecordNotFoundException("No comment with id: " + commentId));
        Long me = currentAccountId();
        boolean allowed = isAdmin() || Objects.equals(comment.getAccountId(), me) || postRepository.findById(comment.getPostId())
                .map(post -> Objects.equals(group(post.getGroupId()).getOwnerId(), me))
                .orElse(false);
        if (!allowed) {
            throw new AccessDeniedException(ServerText.t("err_group_comment_delete", "Only its author, the group's owner or an admin can delete this comment"));
        }
        commentRepository.delete(comment);
        log.info("Group post comment {} deleted", commentId);
    }

    @Transactional
    public void deletePost(final Long postId) {
        GroupPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RecordNotFoundException("No group post with id: " + postId));
        Long me = currentAccountId();
        boolean allowed = isAdmin() || Objects.equals(post.getAccountId(), me)
                || Objects.equals(group(post.getGroupId()).getOwnerId(), me);
        if (!allowed) {
            throw new AccessDeniedException(ServerText.t("err_group_post_delete", "Only its author, the group's owner or an admin can delete this post"));
        }
        postRepository.delete(post);
        log.info("Group post {} deleted", postId);
    }

    /**
     * For the owner's notifications: who asked to join their groups since {@code since} and is
     * still waiting. Derived like the rest of them, so a request answered (or taken back) is gone.
     */
    @Transactional(readOnly = true)
    public List<FeedItem> requestNotificationsFor(final Long ownerId, final LocalDateTime since) {
        Map<Long, SocialGroup> owned = groupRepository.findByOwnerId(ownerId).stream()
                .collect(Collectors.toMap(SocialGroup::getGroupId, Function.identity()));
        if (owned.isEmpty()) return List.of();
        List<GroupMember> waiting = memberRepository.findByGroupIdIn(owned.keySet()).stream()
                .filter(row -> row.getStatus() == GroupMember.Status.PENDING)
                .filter(row -> row.getJoinedDate().isAfter(since))
                .toList();
        Map<Long, Account> askers = accounts(waiting.stream().map(GroupMember::getAccountId).toList());
        return waiting.stream()
                .filter(row -> askers.containsKey(row.getAccountId()))
                .map(row -> FeedItem.builder()
                        .key("GROUP_REQUEST-" + row.getGroupId() + "-" + row.getAccountId())
                        .type(FeedItem.Type.GROUP_REQUEST)
                        .at(row.getJoinedDate())
                        .refId(row.getGroupId())
                        .name(owned.get(row.getGroupId()).getName())
                        .user(UserSummary.of(askers.get(row.getAccountId())))
                        .build())
                .toList();
    }

    /**
     * For the player's notifications: the private groups they were let into since {@code since}.
     * A private group is only ever joined by a request its owner approved, and a group cannot
     * change from public to private, so a member of one who is not its owner was let in, at the
     * time approve() stamped on the row.
     */
    @Transactional(readOnly = true)
    public List<FeedItem> approvalNotificationsFor(final Long accountId, final LocalDateTime since) {
        Map<Long, GroupMember> joined = memberRepository.findByAccountId(accountId).stream()
                .filter(row -> row.getStatus() == GroupMember.Status.MEMBER)
                .filter(row -> row.getJoinedDate().isAfter(since))
                .collect(Collectors.toMap(GroupMember::getGroupId, Function.identity()));
        if (joined.isEmpty()) return List.of();
        List<SocialGroup> groups = groupRepository.findAllById(joined.keySet()).stream()
                .filter(SocialGroup::isPrivateGroup)
                .filter(group -> !Objects.equals(group.getOwnerId(), accountId))
                .toList();
        Map<Long, Account> owners = accounts(groups.stream().map(SocialGroup::getOwnerId).toList());
        return groups.stream()
                .map(group -> FeedItem.builder()
                        .key("GROUP_APPROVED-" + group.getGroupId())
                        .type(FeedItem.Type.GROUP_APPROVED)
                        .at(joined.get(group.getGroupId()).getJoinedDate())
                        .refId(group.getGroupId())
                        .name(group.getName())
                        .user(Optional.ofNullable(owners.get(group.getOwnerId())).map(UserSummary::of).orElse(null))
                        .build())
                .toList();
    }

    /**
     * For the player's notifications: other people's comments on posts they wrote, since
     * {@code since}. One line per post, timed by its newest comment, naming who commented last and
     * how many others did, so a busy thread is one line rather than a flood.
     */
    @Transactional(readOnly = true)
    public List<FeedItem> commentNotificationsFor(final Long accountId, final LocalDateTime since) {
        Map<Long, GroupPost> mine = postRepository.findByAccountId(accountId).stream()
                .collect(Collectors.toMap(GroupPost::getPostId, Function.identity()));
        if (mine.isEmpty()) return List.of();
        Map<Long, List<GroupPostComment>> byPost = commentRepository.findByPostIdInOrderByCreatedDate(mine.keySet()).stream()
                .filter(comment -> !Objects.equals(comment.getAccountId(), accountId))
                .filter(comment -> comment.getCreatedDate().isAfter(since))
                .collect(Collectors.groupingBy(GroupPostComment::getPostId));
        if (byPost.isEmpty()) return List.of();

        Map<Long, SocialGroup> groups = groupRepository.findAllById(byPost.keySet().stream()
                        .map(postId -> mine.get(postId).getGroupId()).distinct().toList()).stream()
                .collect(Collectors.toMap(SocialGroup::getGroupId, Function.identity()));
        Map<Long, Account> commenters = accounts(byPost.values().stream().flatMap(List::stream)
                .map(GroupPostComment::getAccountId).toList());

        return byPost.entrySet().stream()
                .filter(entry -> groups.containsKey(mine.get(entry.getKey()).getGroupId()))
                .map(entry -> {
                    GroupPost post = mine.get(entry.getKey());
                    GroupPostComment last = entry.getValue().getLast();
                    long others = entry.getValue().stream().map(GroupPostComment::getAccountId).distinct().count() - 1;
                    SocialGroup group = groups.get(post.getGroupId());
                    return FeedItem.builder()
                            .key("GROUP_COMMENT-" + post.getPostId())
                            .type(FeedItem.Type.GROUP_COMMENT)
                            .at(last.getCreatedDate())
                            .refId(group.getGroupId())
                            .name(group.getName())
                            .text(post.getContent().length() > 80 ? post.getContent().substring(0, 80) + "…" : post.getContent())
                            .user(Optional.ofNullable(commenters.get(last.getAccountId())).map(UserSummary::of).orElse(null))
                            .count(others)
                            .build();
                })
                .toList();
    }

    /** The groups a player is in, as {@code viewerId} may see them: private ones only to fellow members. */
    @Transactional(readOnly = true)
    public List<GroupCard> groupsOf(final Long accountId, final Long viewerId) {
        List<Long> ids = memberRepository.findByAccountId(accountId).stream()
                .filter(row -> row.getStatus() == GroupMember.Status.MEMBER)
                .map(GroupMember::getGroupId)
                .toList();
        return cards(groupRepository.findAllById(ids), viewerId).stream()
                .filter(card -> !card.privateGroup() || card.role() == Role.OWNER || card.role() == Role.MEMBER)
                .sorted(Comparator.comparing(GroupCard::name, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    /** Of these group posts, the ones {@code viewerId} may read, each with its group. */
    @Transactional(readOnly = true)
    public Map<GroupPost, SocialGroup> readablePosts(final Collection<Long> postIds, final Long viewerId) {
        if (postIds.isEmpty()) return Map.of();
        List<GroupPost> posts = postRepository.findByPostIdIn(postIds);
        Map<Long, SocialGroup> groups = groupRepository.findAllById(posts.stream().map(GroupPost::getGroupId).toList())
                .stream().collect(Collectors.toMap(SocialGroup::getGroupId, Function.identity()));
        Map<Long, GroupMember> mine = memberRepository.findByAccountId(viewerId).stream()
                .collect(Collectors.toMap(GroupMember::getGroupId, Function.identity()));
        return posts.stream()
                .filter(post -> groups.containsKey(post.getGroupId()))
                .filter(post -> {
                    SocialGroup group = groups.get(post.getGroupId());
                    GroupMember row = mine.get(group.getGroupId());
                    return !group.isPrivateGroup() || (Objects.nonNull(row) && row.getStatus() == GroupMember.Status.MEMBER);
                })
                .collect(Collectors.toMap(Function.identity(), post -> groups.get(post.getGroupId())));
    }

    // Each group with its member count, its owner and where the reader stands: two queries for the lot.
    private List<GroupCard> cards(final List<SocialGroup> groups, final Long me) {
        if (groups.isEmpty()) return List.of();
        Map<Long, List<GroupMember>> rows = memberRepository.findByGroupIdIn(groups.stream().map(SocialGroup::getGroupId).toList())
                .stream().collect(Collectors.groupingBy(GroupMember::getGroupId));
        Map<Long, Account> owners = accounts(groups.stream().map(SocialGroup::getOwnerId).toList());
        return groups.stream().map(group -> {
            List<GroupMember> in = rows.getOrDefault(group.getGroupId(), List.of());
            Role role = Objects.equals(group.getOwnerId(), me) ? Role.OWNER : in.stream()
                    .filter(row -> Objects.equals(row.getAccountId(), me))
                    .findFirst()
                    .map(row -> row.getStatus() == GroupMember.Status.MEMBER ? Role.MEMBER : Role.PENDING)
                    .orElse(null);
            long members = in.stream().filter(row -> row.getStatus() == GroupMember.Status.MEMBER).count();
            Account owner = owners.get(group.getOwnerId());
            return new GroupCard(group.getGroupId(), group.getName(), group.getDescription(), group.isPrivateGroup(),
                    members, Objects.isNull(owner) ? null : UserSummary.of(owner), role);
        }).toList();
    }

    private Role roleOf(final SocialGroup group, final Long me) {
        if (Objects.equals(group.getOwnerId(), me)) return Role.OWNER;
        return memberRepository.findByGroupIdAndAccountId(group.getGroupId(), me)
                .map(row -> row.getStatus() == GroupMember.Status.MEMBER ? Role.MEMBER : Role.PENDING)
                .orElse(null);
    }

    private static boolean canRead(final SocialGroup group, final Role role) {
        return !group.isPrivateGroup() || role == Role.OWNER || role == Role.MEMBER;
    }

    private static PostView view(final GroupPost post, final Account author, final boolean canDelete,
                                 final List<CommentView> comments) {
        return new PostView(post.getPostId(), postKey(post.getPostId()),
                Objects.isNull(author) ? null : UserSummary.of(author),
                post.getContent(), post.getCreatedDate(), canDelete, comments);
    }

    private static CommentView commentView(final GroupPostComment comment, final Account author, final boolean canDelete) {
        return new CommentView(comment.getCommentId(), Objects.isNull(author) ? null : UserSummary.of(author),
                comment.getContent(), comment.getCreatedDate(), canDelete);
    }

    private void requireOwner(final SocialGroup group) {
        if (!Objects.equals(group.getOwnerId(), currentAccountId())) {
            throw new AccessDeniedException(ServerText.t("err_group_owner_only", "Only the group's owner can do that"));
        }
    }

    private SocialGroup group(final Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RecordNotFoundException("No group with id: " + groupId));
    }

    private Map<Long, Account> accounts(final Collection<Long> ids) {
        return accountRepository.findAllById(ids.stream().distinct().toList()).stream()
                .collect(Collectors.toMap(Account::getAccountId, Function.identity()));
    }

    private Long currentAccountId() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
    }

    private boolean isAdmin() {
        return authenticationFacade.getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> UserRole.ROLE_ADMIN.name().equals(authority.getAuthority()));
    }
}
