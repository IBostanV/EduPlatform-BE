package com.play.quiz.social;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Who is on the site right now: a player with the site open holds the app's one socket, so being
 * connected is being online. Counted per session, as the same player may have two tabs open.
 * Also what they are doing, where that is something a friend could join (a live match).
 *
 * <p>ponytail: in memory, so it is this server's players only; with more than one instance behind
 * a balancer it would move to a shared store (or the broker's own user registry).
 */
@Log4j2
@Component
public class Presence {

    /** What a player is busy with, told to their friends. */
    public enum Activity { DUEL, ROOM }

    private final Map<String, Integer> sessions = new ConcurrentHashMap<>();
    private final Map<String, Activity> activities = new ConcurrentHashMap<>();

    @EventListener
    public void connected(final SessionConnectedEvent event) {
        name(event.getUser()).ifPresent(email ->
                log.debug("Socket connected for {}: {} session(s)", email, sessions.merge(email, 1, Integer::sum)));
    }

    @EventListener
    public void disconnected(final SessionDisconnectEvent event) {
        name(event.getUser()).ifPresent(email -> log.debug("Socket disconnected for {}: {} session(s) left", email,
                Optional.ofNullable(sessions.computeIfPresent(email, (key, count) -> count > 1 ? count - 1 : null)).orElse(0)));
    }

    public boolean isOnline(final String email) {
        return sessions.containsKey(email);
    }

    public Optional<Activity> activityOf(final String email) {
        return Optional.ofNullable(activities.get(email));
    }

    public void setActivity(final String email, final Activity activity) {
        if (activity == null) activities.remove(email);
        else activities.put(email, activity);
    }

    private static Optional<String> name(final Principal user) {
        return Optional.ofNullable(user).map(Principal::getName);
    }
}
