package com.play.quiz.feed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.play.quiz.conquest.ConquestService;
import com.play.quiz.domain.Account;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.QuestionRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

// What players post on the News page: an admin's is a patch note for everyone, anyone else's goes
// to their friends' friends block, and only its author or an admin can take it down.
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FriendPostsTest {

    private static final long ME = 7L;
    private static final long FRIEND = 8L;

    @Mock private UserService userService;
    @Mock private AuthenticationFacade authenticationFacade;
    @Mock private AccountRepository accountRepository;
    @Mock private QuestionRepository questionRepository;
    @Mock private NewsPostRepository newsPostRepository;
    @Mock private WorldNews worldNews;
    @Mock private ConquestService conquestService;
    @Mock private LevelUpRepository levelUpRepository;
    @Mock private Clock clock;
    @InjectMocks private FeedService feedService;

    private final Account me = Account.builder().accountId(ME).username("me").build();
    private final Account friend = Account.builder().accountId(FRIEND).username("pal").build();

    @BeforeEach
    void init() {
        when(authenticationFacade.getPrincipal()).thenReturn(new User("me@playquiz.io", "x", List.of()));
        when(userService.findByEmail("me@playquiz.io")).thenReturn(me);
        signedInAs("ROLE_USER");
        when(newsPostRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountRepository.findFriends(ME)).thenReturn(List.of(friend));
        when(accountRepository.findById(ME)).thenReturn(Optional.of(me));
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    }

    private void signedInAs(final String role) {
        when(authenticationFacade.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(
                "me@playquiz.io", null, List.of(new SimpleGrantedAuthority(role))));
    }

    private static NewsPost post(final long id, final long author) {
        return NewsPost.builder().newsId(id).title("Hello").createdBy(author)
                .createdDate(LocalDateTime.now().minusHours(1)).build();
    }

    @Test
    void given_a_player_posts_then_it_is_a_friend_post_of_their_own() {
        FeedItem item = feedService.post(new FeedService.NewsInput(" Hello ", ""));

        ArgumentCaptor<NewsPost> saved = ArgumentCaptor.forClass(NewsPost.class);
        verify(newsPostRepository).save(saved.capture());
        assertFalse(saved.getValue().isPatch());
        assertEquals(FeedItem.Type.FRIEND_POST, item.type());
        assertEquals("Hello", item.title());
        assertEquals(ME, item.user().id());
        assertTrue(item.own());
    }

    @Test
    void given_an_admin_posts_then_it_is_a_patch_note() {
        signedInAs("ROLE_ADMIN");

        FeedItem item = feedService.post(new FeedService.NewsInput("Patch 1.3", "Fixes"));

        assertEquals(FeedItem.Type.PATCH, item.type());
    }

    @Test
    void given_friends_posts_then_they_are_in_the_news_with_their_author() {
        when(newsPostRepository.findByPatchFalseAndCreatedByInAndCreatedDateAfter(eq(Set.of(ME, FRIEND)), any()))
                .thenReturn(List.of(post(1L, FRIEND), post(2L, ME)));

        List<FeedItem> posts = feedService.news().stream()
                .filter(item -> item.type() == FeedItem.Type.FRIEND_POST)
                .toList();

        assertEquals(2, posts.size());
        FeedItem fromFriend = posts.stream().filter(item -> item.refId() == 1L).findFirst().orElseThrow();
        assertEquals("pal", fromFriend.user().displayName());
        assertFalse(fromFriend.own());
    }

    @Test
    void given_someone_elses_post_then_a_player_cannot_delete_it() {
        when(newsPostRepository.findById(1L)).thenReturn(Optional.of(post(1L, FRIEND)));

        assertThrows(AccessDeniedException.class, () -> feedService.delete(1L));
        verify(newsPostRepository, never()).delete(any());
    }

    @Test
    void given_their_own_post_then_a_player_can_delete_it() {
        NewsPost own = post(2L, ME);
        when(newsPostRepository.findById(2L)).thenReturn(Optional.of(own));

        feedService.delete(2L);

        verify(newsPostRepository).delete(own);
    }

    @Test
    void given_an_overlong_title_then_refuse_it() {
        String title = "x".repeat(FeedService.MAX_TITLE + 1);

        assertThrows(IllegalArgumentException.class, () -> feedService.post(new FeedService.NewsInput(title, null)));
        verify(newsPostRepository, never()).save(any());
    }
}
