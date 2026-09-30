package com.play.quiz.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

class FeedbackRateLimiterTest {

    // A clock the test moves forward by hand.
    private static final class MovingClock extends Clock {
        private Instant now = Instant.parse("2026-09-19T12:00:00Z");

        void advance(java.time.Duration by) {
            now = now.plus(by);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(final ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return now;
        }
    }

    @Test
    void given_a_sender_at_the_cap_when_sending_again_then_refuse_until_the_window_passes() {
        MovingClock clock = new MovingClock();
        FeedbackRateLimiter limiter = new FeedbackRateLimiter(clock);

        for (int message = 0; message < FeedbackRateLimiter.MAX_MESSAGES; message++) {
            assertTrue(limiter.tryAcquire("10.0.0.1"));
        }
        assertFalse(limiter.tryAcquire("10.0.0.1"));
        // Someone else is counted on their own.
        assertTrue(limiter.tryAcquire("10.0.0.2"));

        clock.advance(FeedbackRateLimiter.WINDOW.plusSeconds(1));
        assertTrue(limiter.tryAcquire("10.0.0.1"));
    }
}
