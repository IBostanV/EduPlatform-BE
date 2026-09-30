package com.play.quiz.feed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.play.quiz.domain.Account;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.QuestionRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.User;

// The kinds of news a player switched off: stored on the account, left out of their news.
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class HiddenNewsTest {

    private static final long ACCOUNT_ID = 7L;

    @Mock private UserService userService;
    @Mock private AuthenticationFacade authenticationFacade;
    @Mock private AccountRepository accountRepository;
    @Mock private QuestionRepository questionRepository;
    @Mock private NewsPostRepository newsPostRepository;
    @Mock private WorldNews worldNews;
    @InjectMocks private FeedService feedService;

    @BeforeEach
    void init() {
        when(authenticationFacade.getPrincipal()).thenReturn(new User("me@playquiz.io", "x", List.of()));
        when(userService.findByEmail("me@playquiz.io")).thenReturn(Account.builder().accountId(ACCOUNT_ID).build());
        when(newsPostRepository.findByPatchTrueAndCreatedDateAfter(any())).thenReturn(List.of(NewsPost.builder()
                .newsId(1L).title("Patch 1.2").createdDate(LocalDateTime.now().minusHours(1)).build()));
        when(worldNews.headlines()).thenReturn(List.of(FeedItem.builder()
                .key("WORLD-x").type(FeedItem.Type.WORLD).at(LocalDateTime.now()).title("Headline").build()));
    }

    @Test
    void given_kinds_to_hide_then_store_them_as_sorted_names() {
        feedService.hideNews(Set.of(FeedItem.Type.WORLD, FeedItem.Type.PATCH));

        verify(accountRepository).setHiddenNews(ACCOUNT_ID, "PATCH,WORLD");
    }

    @Test
    void given_nothing_to_hide_then_clear_the_column() {
        feedService.hideNews(Set.of());

        verify(accountRepository).setHiddenNews(ACCOUNT_ID, null);
    }

    // Notifications are not news: they cannot be switched off here.
    @Test
    void given_a_kind_that_is_not_news_then_refuse_it() {
        assertThrows(IllegalArgumentException.class, () -> feedService.hideNews(Set.of(FeedItem.Type.TROPHY)));
        verify(accountRepository, never()).setHiddenNews(anyLong(), any());
    }

    // A kind renamed or removed since it was stored is ignored rather than breaking the read.
    @Test
    void given_a_stored_list_then_read_back_only_the_kinds_that_exist() {
        when(accountRepository.findHiddenNews(ACCOUNT_ID)).thenReturn("WORLD, GONE_KIND");

        assertEquals(Set.of(FeedItem.Type.WORLD), feedService.hiddenNews());
    }

    @Test
    void given_hidden_world_news_then_leave_it_out() {
        when(accountRepository.findHiddenNews(ACCOUNT_ID)).thenReturn("WORLD");

        assertEquals(List.of(FeedItem.Type.PATCH), feedService.news().stream().map(FeedItem::type).toList());
    }

    @Test
    void given_nothing_hidden_then_show_every_kind_newest_first() {
        assertEquals(List.of(FeedItem.Type.WORLD, FeedItem.Type.PATCH),
                feedService.news().stream().map(FeedItem::type).toList());
    }
}
