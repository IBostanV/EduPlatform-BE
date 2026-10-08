package com.play.quiz.feed;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.play.quiz.conquest.ConquestService;
import com.play.quiz.domain.Account;
import com.play.quiz.domain.Category;
import com.play.quiz.enums.UserRole;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.duel.DuelService;
import com.play.quiz.group.GroupService;
import com.play.quiz.record.UserSummary;
import com.play.quiz.social.ChallengeService;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.KnowledgeBaseRepository;
import com.play.quiz.repository.QuestionRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import com.play.quiz.trophy.EarnedTrophyRepository;
import com.play.quiz.trophy.TrophyCatalog;
import com.play.quiz.trophy.TrophyDefinition;
import com.play.quiz.trophy.TrophyService;
import com.play.quiz.util.ServerText;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The notifications (what happened to you) and the news (what happened around you).
 *
 * <p>Almost none of it is stored as a notification: every line is worked out from what the site
 * already keeps — the trophies earned, the wiki, the questions, the conquest attempts — and from
 * the clock, which is what the conquest rounds are made of. Nothing has to fire at the right
 * moment, so nothing is missed while a server is down. What is kept is only what could not be
 * found later: when the player last read their notifications, when somebody reached a level
 * ({@link LevelUp}), and what people post on the News page ({@link NewsPost}).
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class FeedService {

    /** How far back both lists reach. */
    static final int WINDOW_DAYS = 30;
    static final int LIMIT = 60;
    static final int MAX_TITLE = 200;
    static final int MAX_CONTENT = 4000;

    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final AccountRepository accountRepository;
    private final TrophyService trophyService;
    private final TrophyCatalog trophyCatalog;
    private final EarnedTrophyRepository earnedTrophyRepository;
    private final KnowledgeBaseRepository knowledgeBaseRepository;
    private final QuestionRepository questionRepository;
    private final ConquestService conquestService;
    private final LevelUpRepository levelUpRepository;
    private final NewsPostRepository newsPostRepository;
    private final WorldNews worldNews;
    private final ChallengeService challengeService;
    private final GroupService groupService;
    private final DuelService duelService;
    private final Clock clock;

    /** The signed-in player's notifications, newest first, with how many are unread. */
    @Transactional
    public FeedItem.Notifications notifications() {
        Account player = currentAccount();
        Long accountId = player.getAccountId();
        LocalDateTime since = LocalDateTime.now().minusDays(WINDOW_DAYS);

        // Trophies are recorded when the shelf is read. Reading it here too means a trophy won
        // by a quiz is told about without first having to open the trophies page.
        trophyService.shelf();

        List<FeedItem> items = new ArrayList<>();

        conquestService.roundsOpenedSince(toConquestClock(since)).forEach(round -> items.add(FeedItem.builder()
                .key("CONQUEST_ROUND-" + round.round())
                .type(FeedItem.Type.CONQUEST_ROUND)
                .at(fromConquestClock(round.at()))
                .count(round.round())
                .names(round.countries().stream().map(Category::getName).toList())
                .build()));

        conquestService.changesSince(toConquestClock(since)).stream()
                .filter(change -> Objects.nonNull(change.previous())
                        && accountId.equals(change.previous().getAccountId()))
                .forEach(change -> items.add(FeedItem.builder()
                        .key("CONQUEST_LOST-" + change.round() + "-" + change.country().getCatId())
                        .type(FeedItem.Type.CONQUEST_LOST)
                        .at(fromConquestClock(change.at()))
                        .refId(change.country().getCatId())
                        .name(change.country().getName())
                        .user(UserSummary.of(change.holder()))
                        .build()));

        Map<String, TrophyDefinition> trophies = trophyCatalog.all().stream()
                .collect(Collectors.toMap(TrophyDefinition::code, Function.identity(), (first, second) -> first));
        earnedTrophyRepository.findByAccountId(accountId).stream()
                .filter(earned -> earned.getEarnedDate().isAfter(since))
                .filter(earned -> trophies.containsKey(earned.getCode()))
                .forEach(earned -> items.add(FeedItem.builder()
                        .key("TROPHY-" + earned.getCode())
                        .type(FeedItem.Type.TROPHY)
                        .at(earned.getEarnedDate())
                        .title(trophies.get(earned.getCode()).title())
                        .build()));

        List<Long> favorites = accountRepository.findFavoriteCategoryTreeIds(accountId).stream()
                .map(Number::longValue)
                .toList();
        if (!favorites.isEmpty()) {
            knowledgeBaseRepository.findPublishedSince(favorites, since).forEach(article -> items.add(FeedItem.builder()
                    .key("WIKI_ARTICLE-" + article.getId())
                    .type(FeedItem.Type.WIKI_ARTICLE)
                    .at(article.getCreatedDate())
                    .refId(article.getId())
                    .title(article.getTitle())
                    .name(article.getCategory().getName())
                    .build()));
        }

        items.addAll(challengeService.notificationsFor(accountId, since));
        items.addAll(groupService.requestNotificationsFor(accountId, since));
        items.addAll(groupService.approvalNotificationsFor(accountId, since));
        items.addAll(groupService.commentNotificationsFor(accountId, since));
        items.addAll(duelService.notificationsFor(accountId, since));

        // Nothing read yet counts everything: a new account's first look shows the lot as new.
        LocalDateTime readAt = accountRepository.findNotificationsReadAt(accountId);
        List<FeedItem> newest = newestFirst(items);
        long unread = newest.stream().filter(item -> Objects.isNull(readAt) || item.at().isAfter(readAt)).count();
        log.debug("Notifications for account {}: {} found, {} shown, {} unread (read at {})",
                accountId, items.size(), newest.size(), unread, readAt);

        return new FeedItem.Notifications(unread, newest);
    }

    /** Everything up to now has been seen. */
    @Transactional
    public void markNotificationsRead() {
        Long accountId = currentAccount().getAccountId();
        accountRepository.setNotificationsReadAt(accountId, LocalDateTime.now());
        log.info("Account {} marked notifications read", accountId);
    }

    /**
     * The news, newest first, without the kinds the player switched off. A guest gets it too,
     * minus the friends.
     */
    @Transactional(readOnly = true)
    public List<FeedItem> news() {
        LocalDateTime since = LocalDateTime.now().minusDays(WINDOW_DAYS);
        List<FeedItem> items = new ArrayList<>();

        newsPostRepository.findByPatchTrueAndCreatedDateAfter(since).stream()
                .map(FeedService::patchItem)
                .forEach(items::add);

        items.addAll(questionsAdded(since));
        currentAccountId().ifPresent(accountId -> {
            items.addAll(friendsNews(accountId, since));
            items.addAll(friendPosts(accountId, since));
        });
        worldNews.headlines().stream().filter(headline -> headline.at().isAfter(since)).forEach(items::add);

        Set<FeedItem.Type> hidden = currentAccountId().map(this::hiddenNewsOf).orElse(Set.of());
        items.removeIf(item -> hidden.contains(item.type()));
        log.debug("News: {} items after hiding {}", items.size(), hidden);
        return newestFirst(items);
    }

    /** The kinds of news the signed-in player has switched off. */
    @Transactional(readOnly = true)
    public Set<FeedItem.Type> hiddenNews() {
        return hiddenNewsOf(currentAccount().getAccountId());
    }

    /** Replaces the switched-off kinds. Only news kinds can be switched off; anything else is refused. */
    @Transactional
    public Set<FeedItem.Type> hideNews(final Set<FeedItem.Type> hidden) {
        Set<FeedItem.Type> kinds = Objects.isNull(hidden) ? Set.of() : hidden;
        if (!FeedItem.Type.NEWS.containsAll(kinds)) {
            throw new IllegalArgumentException(ServerText.t("err_only_news_hidden", "Only news can be hidden: {{type}}", "type", FeedItem.Type.NEWS));
        }
        String stored = kinds.stream().map(Enum::name).sorted().collect(Collectors.joining(","));
        Long accountId = currentAccount().getAccountId();
        accountRepository.setHiddenNews(accountId, stored.isEmpty() ? null : stored);
        log.info("Account {} now hides news {}", accountId, kinds);
        return hiddenNews();
    }

    // A name that is no longer a kind (one renamed or removed since) is simply not hidden any more.
    private Set<FeedItem.Type> hiddenNewsOf(final Long accountId) {
        String stored = accountRepository.findHiddenNews(accountId);
        if (Objects.isNull(stored) || stored.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(stored.split(","))
                .map(String::strip)
                .flatMap(name -> FeedItem.Type.NEWS.stream().filter(type -> type.name().equals(name)))
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Anyone signed in can post. An admin's post is a patch note, in everyone's news; anyone
     * else's is a friend post, in their own and their friends' friends block. Shown as plain text.
     */
    @Transactional
    public FeedItem post(final NewsInput input) {
        if (Objects.isNull(input) || Objects.isNull(input.title()) || input.title().isBlank()) {
            throw new IllegalArgumentException(ServerText.t("err_post_needs_title", "A post needs a title"));
        }
        String title = input.title().strip();
        String content = Optional.ofNullable(input.content()).map(String::strip)
                .filter(text -> !text.isEmpty())
                .orElse(null);
        // Q_NEWS.TITLE and CONTENT; longer would fail in the database instead.
        if (title.length() > MAX_TITLE || (Objects.nonNull(content) && content.length() > MAX_CONTENT)) {
            throw new IllegalArgumentException(ServerText.t("err_post_too_long", "A post can have at most {{title}} characters of title and {{text}} of text",
                    "title", MAX_TITLE, "text", MAX_CONTENT));
        }
        Account author = currentAccount();
        NewsPost post = newsPostRepository.save(NewsPost.builder()
                .title(title)
                .content(content)
                .createdBy(author.getAccountId())
                .createdDate(LocalDateTime.now())
                .patch(isAdmin())
                .build());
        log.info("Account {} posted news {} (patch: {})", author.getAccountId(), post.getNewsId(), post.isPatch());

        return post.isPatch() ? patchItem(post) : friendPostItem(post, author, true);
    }

    /** Its author can take a post down, and an admin can take down any. */
    @Transactional
    public void delete(final Long newsId) {
        NewsPost post = newsPostRepository.findById(newsId)
                .orElseThrow(() -> new RecordNotFoundException("No post with id: " + newsId));
        if (!isAdmin() && !Objects.equals(post.getCreatedBy(), currentAccount().getAccountId())) {
            throw new AccessDeniedException(ServerText.t("err_delete_post_denied", "Only its author or an admin can delete post {{id}}", "id", newsId));
        }
        newsPostRepository.delete(post);
        log.info("News post {} by account {} deleted", newsId, post.getCreatedBy());
    }

    public record NewsInput(String title, String content) {}

    /** One line per category per day it got new questions, however many arrived. */
    private List<FeedItem> questionsAdded(final LocalDateTime since) {
        record Day(Long categoryId, LocalDate day) {}

        Map<Day, List<QuestionRepository.AddedQuestion>> byDay = questionRepository.findAddedSince(since).stream()
                .collect(Collectors.groupingBy(added ->
                        new Day(added.getCategoryId(), added.getCreatedDate().toLocalDate())));

        return byDay.entrySet().stream()
                .map(entry -> {
                    List<QuestionRepository.AddedQuestion> added = entry.getValue();
                    return FeedItem.builder()
                            .key("QUESTIONS_ADDED-" + entry.getKey().categoryId() + "-" + entry.getKey().day())
                            .type(FeedItem.Type.QUESTIONS_ADDED)
                            .at(added.stream().map(QuestionRepository.AddedQuestion::getCreatedDate)
                                    .max(Comparator.naturalOrder()).orElseThrow())
                            .refId(entry.getKey().categoryId())
                            .name(added.get(0).getCategoryName())
                            .count((long) added.size())
                            .build();
                })
                .toList();
    }

    /**
     * Friends who took a country, and friends who levelled up.
     *
     * <p>Levels would flood the news — a busy friend passes several in an evening — so a day's
     * level-ups among all friends are one line, each friend in it once at the highest level they
     * reached that day.
     */
    private List<FeedItem> friendsNews(final Long accountId, final LocalDateTime since) {
        Map<Long, Account> friends = accountRepository.findFriends(accountId).stream()
                .collect(Collectors.toMap(Account::getAccountId, Function.identity(), (first, second) -> first));
        if (friends.isEmpty()) {
            return List.of();
        }

        List<FeedItem> items = new ArrayList<>();

        conquestService.changesSince(toConquestClock(since)).stream()
                .filter(change -> friends.containsKey(change.holder().getAccountId()))
                .forEach(change -> items.add(FeedItem.builder()
                        .key("FRIEND_CONQUEST-" + change.round() + "-" + change.country().getCatId())
                        .type(FeedItem.Type.FRIEND_CONQUEST)
                        .at(fromConquestClock(change.at()))
                        .refId(change.country().getCatId())
                        .name(change.country().getName())
                        .user(UserSummary.of(change.holder()))
                        .build()));

        Map<LocalDate, List<LevelUp>> byDay = levelUpRepository.findFriendsSince(accountId, since).stream()
                .collect(Collectors.groupingBy(levelUp -> levelUp.getReachedDate().toLocalDate(), TreeMap::new,
                        Collectors.toList()));
        byDay.forEach((day, levelUps) -> {
            Map<Long, Integer> highest = levelUps.stream()
                    .collect(Collectors.toMap(LevelUp::getAccountId, LevelUp::getLevel, Math::max));
            items.add(FeedItem.builder()
                    .key("FRIEND_LEVELS-" + day)
                    .type(FeedItem.Type.FRIEND_LEVELS)
                    .at(levelUps.stream().map(LevelUp::getReachedDate).max(Comparator.naturalOrder()).orElseThrow())
                    .levels(highest.entrySet().stream()
                            .filter(entry -> friends.containsKey(entry.getKey()))
                            .map(entry -> new FeedItem.FriendLevel(UserSummary.of(friends.get(entry.getKey())),
                                    entry.getValue()))
                            .sorted(Comparator.comparingInt(FeedItem.FriendLevel::level).reversed())
                            .toList())
                    .build());
        });

        return items;
    }

    /** What the player and their friends posted: the player's own too, so they see it went out. */
    private List<FeedItem> friendPosts(final Long accountId, final LocalDateTime since) {
        Map<Long, Account> authors = Stream.concat(
                        accountRepository.findFriends(accountId).stream(),
                        accountRepository.findById(accountId).stream())
                .collect(Collectors.toMap(Account::getAccountId, Function.identity(), (first, second) -> first));

        return newsPostRepository.findByPatchFalseAndCreatedByInAndCreatedDateAfter(authors.keySet(), since).stream()
                .map(post -> friendPostItem(post, authors.get(post.getCreatedBy()),
                        Objects.equals(post.getCreatedBy(), accountId)))
                .toList();
    }

    private static FeedItem friendPostItem(final NewsPost post, final Account author, final boolean own) {
        return FeedItem.builder()
                .key("FRIEND_POST-" + post.getNewsId())
                .type(FeedItem.Type.FRIEND_POST)
                .at(post.getCreatedDate())
                .refId(post.getNewsId())
                .title(post.getTitle())
                .text(post.getContent())
                .user(UserSummary.of(author))
                .own(own)
                .build();
    }

    private boolean isAdmin() {
        return authenticationFacade.getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> UserRole.ROLE_ADMIN.name().equals(authority.getAuthority()));
    }

    private static FeedItem patchItem(final NewsPost post) {
        return FeedItem.builder()
                .key("PATCH-" + post.getNewsId())
                .type(FeedItem.Type.PATCH)
                .at(post.getCreatedDate())
                .refId(post.getNewsId())
                .title(post.getTitle())
                .text(post.getContent())
                .build();
    }

    private static List<FeedItem> newestFirst(final List<FeedItem> items) {
        return items.stream()
                .sorted(Comparator.comparing(FeedItem::at).reversed())
                .limit(LIMIT)
                .toList();
    }

    // The conquest rounds run on the UTC clock bean; everything else here is on the server's own.
    private LocalDateTime toConquestClock(final LocalDateTime local) {
        return local.atZone(ZoneId.systemDefault()).withZoneSameInstant(clock.getZone()).toLocalDateTime();
    }

    private LocalDateTime fromConquestClock(final LocalDateTime conquest) {
        return conquest.atZone(clock.getZone()).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

    private Account currentAccount() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername());
    }

    // The news reads signed out too, so there may be nobody to find friends for.
    private Optional<Long> currentAccountId() {
        try {
            return Optional.of(currentAccount().getAccountId());
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }
}
