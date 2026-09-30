package com.play.quiz.feed;

import java.util.List;
import java.util.Set;

import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Notifications are the signed-in player's own; the news reads for everyone, and only admins
// write its patch notes (WebSecurity).
@RestController
@RequestMapping(RestEndpoint.CONTEXT_PATH + RestEndpoint.REQUEST_MAPPING_FEED)
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping(value = "/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeedItem.Notifications> getNotifications() {
        return ResponseEntity.ok(feedService.notifications());
    }

    @PostMapping("/notifications/read")
    public ResponseEntity<Void> markNotificationsRead() {
        feedService.markNotificationsRead();
        return ResponseEntity.noContent().build();
    }

    /** Without the kinds the player switched off. */
    @GetMapping(value = "/news", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FeedItem>> getNews() {
        return ResponseEntity.ok(feedService.news());
    }

    // Not under /news: writes there are the admins' patch notes (WebSecurity).
    @GetMapping(value = "/hidden-news", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<FeedItem.Type>> getHiddenNews() {
        return ResponseEntity.ok(feedService.hiddenNews());
    }

    @PutMapping(value = "/hidden-news", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<FeedItem.Type>> hideNews(@RequestBody final Set<FeedItem.Type> hidden) {
        return ResponseEntity.ok(feedService.hideNews(hidden));
    }

    @PostMapping(value = "/news", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeedItem> post(@RequestBody final FeedService.NewsInput input) {
        return ResponseEntity.ok(feedService.post(input));
    }

    @DeleteMapping("/news/{newsId}")
    public ResponseEntity<Void> delete(@PathVariable final Long newsId) {
        feedService.delete(newsId);
        return ResponseEntity.noContent().build();
    }
}
