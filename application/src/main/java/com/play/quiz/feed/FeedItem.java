package com.play.quiz.feed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.play.quiz.record.UserSummary;
import lombok.Builder;

/**
 * One line in the notifications or the news. The server sends what happened, not the sentence:
 * the wording lives with the browser's translations, the way the daily tasks do it.
 *
 * <p>Which fields a type fills:
 * <ul>
 *   <li>{@code CONQUEST_ROUND} — {@code count} the round, {@code names} the countries it opened</li>
 *   <li>{@code CONQUEST_LOST} — {@code name} the country, {@code user} who took it</li>
 *   <li>{@code TROPHY} — {@code title} the trophy</li>
 *   <li>{@code WIKI_ARTICLE} — {@code refId} the article, {@code title}, {@code name} its category</li>
 *   <li>{@code PATCH} — {@code refId} the post, {@code title}, {@code text}</li>
 *   <li>{@code QUESTIONS_ADDED} — {@code refId} the category, {@code name}, {@code count}</li>
 *   <li>{@code FRIEND_LEVELS} — {@code levels}: one day's level-ups among friends, in one line</li>
 *   <li>{@code FRIEND_CONQUEST} — {@code user} the friend, {@code name} the country</li>
 *   <li>{@code FRIEND_POST} — {@code refId} the post, {@code title}, {@code text}, {@code user} its
 *       author (a friend, or the reader), {@code own} whether the reader wrote it</li>
 *   <li>{@code WORLD} — {@code title} the headline, {@code url}, {@code text} the source, {@code name} the category</li>
 * </ul>
 *
 * <p>{@code key} is unique within a list, for the browser to tell the lines apart.
 */
@Builder
public record FeedItem(String key,
                       Type type,
                       LocalDateTime at,
                       String title,
                       String text,
                       String url,
                       Long refId,
                       String name,
                       Long count,
                       UserSummary user,
                       List<String> names,
                       List<FriendLevel> levels,
                       Boolean own) {

    public enum Type {
        CONQUEST_ROUND, CONQUEST_LOST, TROPHY, WIKI_ARTICLE,
        PATCH, QUESTIONS_ADDED, FRIEND_LEVELS, FRIEND_CONQUEST, FRIEND_POST, WORLD,
        // A friend's "beat my score", and the answer to one the reader sent.
        CHALLENGE, CHALLENGE_DONE;

        /** The kinds that make up the news, and so the ones a player can switch off. */
        public static final Set<Type> NEWS = Set.of(PATCH, QUESTIONS_ADDED, FRIEND_LEVELS, FRIEND_CONQUEST,
                FRIEND_POST, WORLD);
    }

    public record FriendLevel(UserSummary user, int level) {}

    /** The notifications, and how many of them came after the player last opened the list. */
    public record Notifications(long unread, List<FeedItem> items) {}
}
