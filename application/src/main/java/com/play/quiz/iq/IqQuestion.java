package com.play.quiz.iq;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A question as the browser gets it: what kind it is, what to draw, and how many options there
 * are. Which option is right is not in here — the server keeps that until the answer is in.
 *
 * @param number    which question this is, counting from one
 * @param total     how many there are at most; the test can stop sooner
 * @param seconds   how long this question is allowed, timed by the server
 */
public record IqQuestion(String type, JsonNode payload, int options, int number, int total, int seconds) {
}
