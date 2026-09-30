package com.play.quiz.iq;

import java.time.LocalDateTime;

/**
 * What a finished test came to.
 *
 * <p>The range is not decoration: a test of this length measures to about ±5 points, and a score
 * quoted without it invites people to read a difference of three points as a difference in
 * ability. {@code normed} says where the scale came from — the app's own takers once there are
 * enough of them, the model's assumption about the population until then.
 */
public record IqResult(int iq,
                       int low,
                       int high,
                       int percentile,
                       double theta,
                       double standardError,
                       boolean normed,
                       int items,
                       int attemptNo,
                       LocalDateTime finishedDate) {
}
