package com.play.quiz.security;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * Caps how many feedback messages one sender can send: sending is open to guests, so without a cap
 * anyone could fill the admins' list. A sliding window per sender key (their account when signed
 * in, else their address).
 *
 * ponytail: in memory, per instance; move it to a shared store (Redis, bucket4j) if the backend
 * ever runs on more than one node.
 */
@Component
public class FeedbackRateLimiter {

    static final int MAX_MESSAGES = 5;
    static final Duration WINDOW = Duration.ofMinutes(10);
    // Past this many senders tracked, idle ones are dropped, so a flood of addresses cannot grow
    // the map for good.
    private static final int CLEANUP_THRESHOLD = 10_000;

    private final Map<String, Deque<Instant>> sent = new ConcurrentHashMap<>();
    private final Clock clock;

    public FeedbackRateLimiter() {
        this(Clock.systemUTC());
    }

    FeedbackRateLimiter(final Clock clock) {
        this.clock = clock;
    }

    /** Records a message from this sender and says whether it is allowed. */
    public boolean tryAcquire(final String sender) {
        Instant now = clock.instant();
        Instant windowStart = now.minus(WINDOW);
        if (sent.size() > CLEANUP_THRESHOLD) {
            sent.values().removeIf(times -> {
                synchronized (times) {
                    return times.isEmpty() || times.peekLast().isBefore(windowStart);
                }
            });
        }

        Deque<Instant> times = sent.computeIfAbsent(sender, key -> new ArrayDeque<>());
        synchronized (times) {
            while (!times.isEmpty() && times.peekFirst().isBefore(windowStart)) {
                times.pollFirst();
            }
            if (times.size() >= MAX_MESSAGES) {
                return false;
            }
            times.addLast(now);
            return true;
        }
    }
}
