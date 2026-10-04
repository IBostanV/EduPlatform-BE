package com.play.quiz.profile;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Category;
import com.play.quiz.enums.ProfileVisibility;
import com.play.quiz.feed.NewsPost;
import com.play.quiz.feed.NewsPostRepository;
import com.play.quiz.group.GroupPost;
import com.play.quiz.group.GroupService;
import com.play.quiz.group.SocialGroup;
import com.play.quiz.record.QuizHistoryEntry;
import com.play.quiz.record.UserSummary;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.KnowledgeBaseRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import com.play.quiz.social.Reaction;
import com.play.quiz.social.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * What a player has been doing, for their profile page: quizzes with their scores, the posts they
 * liked, what they wrote (news posts and wiki articles), their friends and their groups.
 *
 * <p>The player chooses who sees it ({@link ProfileVisibility}); they always see their own. Inside
 * it, other people's things stay as private as they are elsewhere: a liked friend post shows only
 * to someone who could read it on the News page, a liked group post or a private group only to
 * that group's members.
 */
@Service
@RequiredArgsConstructor
public class ProfileActivityService {

    // ponytail: recent activity only, no paging; page these when profiles get long.
    static final int SHOWN = 20;

    public record ProfileActivity(ProfileVisibility visibility,
                                  boolean visible,
                                  List<QuizHistoryEntry> history,
                                  List<LikedPost> likes,
                                  List<WrittenPost> posts,
                                  List<WrittenArticle> articles,
                                  List<UserSummary> friends,
                                  List<GroupService.GroupCard> groups) {}

    /** A post the player reacted to: {@code type} FRIEND_POST or GROUP_POST, with its group if any. */
    public record LikedPost(Reaction.Kind kind, String type, Long id, String title, String text,
                            UserSummary author, Long groupId, String groupName, LocalDateTime at) {}

    public record WrittenPost(Long id, String title, String text, boolean patch, LocalDateTime at) {}

    public record WrittenArticle(Long id, String title, String category, LocalDateTime at) {}

    private static final String FRIEND_POST = "FRIEND_POST-";
    private static final String GROUP_POST = "GROUP_POST-";

    private final AccountRepository accountRepository;
    private final UserQuizHistoryRepository historyRepository;
    private final ReactionRepository reactionRepository;
    private final NewsPostRepository newsPostRepository;
    private final KnowledgeBaseRepository knowledgeBaseRepository;
    private final GroupService groupService;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    @Transactional(readOnly = true)
    public ProfileActivity activity(final Long accountId) {
        Account owner = userService.getProfileAccount(accountId);
        Long viewer = userService.findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
        ProfileVisibility visibility = Optional.ofNullable(owner.getProfileVisibility()).orElse(ProfileVisibility.FRIENDS);
        List<Account> friends = accountRepository.findFriends(accountId);

        if (!mayView(visibility, accountId, viewer, friends)) {
            return new ProfileActivity(visibility, false, List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
        }
        return new ProfileActivity(visibility, true,
                historyRepository.findHistoryOf(accountId,
                                PageRequest.of(0, SHOWN, Sort.by(Sort.Direction.DESC, "completedDate")))
                        .getContent().stream().map(QuizHistoryEntry::of).toList(),
                likes(accountId, viewer),
                newsPostRepository.findByCreatedByOrderByCreatedDateDesc(accountId, PageRequest.of(0, SHOWN)).stream()
                        .map(post -> new WrittenPost(post.getNewsId(), post.getTitle(), post.getContent(),
                                post.isPatch(), post.getCreatedDate()))
                        .toList(),
                knowledgeBaseRepository.findPublishedBy(accountId).stream()
                        .limit(SHOWN)
                        .map(article -> new WrittenArticle(article.getId(), article.getTitle(),
                                Optional.ofNullable(article.getCategory()).map(Category::getName).orElse(null),
                                article.getCreatedDate()))
                        .toList(),
                friends.stream().map(UserSummary::of)
                        .sorted(Comparator.comparing(UserSummary::displayName, String.CASE_INSENSITIVE_ORDER))
                        .toList(),
                groupService.groupsOf(accountId, viewer));
    }

    static boolean mayView(final ProfileVisibility visibility, final Long owner, final Long viewer,
                           final List<Account> ownersFriends) {
        if (Objects.equals(owner, viewer)) return true;
        return switch (visibility) {
            case PUBLIC -> true;
            case FRIENDS -> ownersFriends.stream().anyMatch(friend -> Objects.equals(friend.getAccountId(), viewer));
            case PRIVATE -> false;
        };
    }

    // The posts they reacted to, newest reaction first, each once (with the first kind found).
    private List<LikedPost> likes(final Long accountId, final Long viewer) {
        List<Reaction> reactions = reactionRepository.findByAccountIdOrderByCreatedDateDesc(accountId).stream()
                .filter(reaction -> reaction.getItemKey().startsWith(FRIEND_POST) || reaction.getItemKey().startsWith(GROUP_POST))
                .collect(Collectors.toMap(Reaction::getItemKey, Function.identity(), (first, other) -> first,
                        java.util.LinkedHashMap::new))
                .values().stream().toList();

        Map<Long, NewsPost> newsPosts = newsPostRepository.findAllById(idsOf(reactions, FRIEND_POST)).stream()
                .filter(post -> mayReadNews(post, viewer))
                .collect(Collectors.toMap(NewsPost::getNewsId, Function.identity()));
        Map<Long, Map.Entry<GroupPost, SocialGroup>> groupPosts = groupService
                .readablePosts(idsOf(reactions, GROUP_POST), viewer).entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().getPostId(), Function.identity()));

        Set<Long> authorIds = newsPosts.values().stream().map(NewsPost::getCreatedBy)
                .collect(Collectors.toCollection(java.util.HashSet::new));
        groupPosts.values().forEach(entry -> authorIds.add(entry.getKey().getAccountId()));
        Map<Long, UserSummary> authors = accountRepository.findAllById(authorIds).stream()
                .collect(Collectors.toMap(Account::getAccountId, UserSummary::of));

        return reactions.stream().map(reaction -> {
            Long id = idOf(reaction.getItemKey());
            if (reaction.getItemKey().startsWith(FRIEND_POST)) {
                NewsPost post = newsPosts.get(id);
                return Objects.isNull(post) ? null : new LikedPost(reaction.getKind(), "FRIEND_POST", id,
                        post.getTitle(), post.getContent(), authors.get(post.getCreatedBy()), null, null, post.getCreatedDate());
            }
            Map.Entry<GroupPost, SocialGroup> found = groupPosts.get(id);
            return Objects.isNull(found) ? null : new LikedPost(reaction.getKind(), "GROUP_POST", id, null,
                    found.getKey().getContent(), authors.get(found.getKey().getAccountId()),
                    found.getValue().getGroupId(), found.getValue().getName(), found.getKey().getCreatedDate());
        }).filter(Objects::nonNull).limit(SHOWN).toList();
    }

    // A patch note is everyone's; a friend post is its author's and their friends', as on the News page.
    private boolean mayReadNews(final NewsPost post, final Long viewer) {
        if (post.isPatch() || Objects.equals(post.getCreatedBy(), viewer)) return true;
        return accountRepository.findFriends(post.getCreatedBy()).stream()
                .anyMatch(friend -> Objects.equals(friend.getAccountId(), viewer));
    }

    private static List<Long> idsOf(final List<Reaction> reactions, final String prefix) {
        return reactions.stream()
                .filter(reaction -> reaction.getItemKey().startsWith(prefix))
                .map(reaction -> idOf(reaction.getItemKey()))
                .filter(Objects::nonNull)
                .toList();
    }

    private static Long idOf(final String key) {
        try {
            return Long.valueOf(key.substring(key.indexOf('-') + 1));
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
