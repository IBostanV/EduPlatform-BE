package com.play.quiz.social;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.play.quiz.record.UserSummary;
import com.play.quiz.social.ChallengeService.ChallengeView;
import com.play.quiz.social.ChallengeService.Score;
import org.junit.jupiter.api.Test;

class ChallengeOutcomeTest {

    private static final UserSummary ANA = new UserSummary(1L, "Ana", null, null, null);
    private static final UserSummary BOB = new UserSummary(2L, "Bob", null, null, null);

    private static ChallengeView challenge(final Score mine, final Score theirs) {
        return new ChallengeView(9L, 3L, "Capitals", ANA, BOB, mine, theirs, LocalDateTime.now());
    }

    private static Score score(final int right, final double seconds) {
        return new Score(right, 10, seconds, LocalDateTime.now());
    }

    @Test
    void given_no_answer_yet_then_no_outcome() {
        assertNull(challenge(score(7, 40), null).outcome());
    }

    @Test
    void given_more_right_answers_then_that_side_wins_whatever_the_time() {
        assertEquals("WON", challenge(score(8, 90), score(7, 20)).outcome());
        assertEquals("LOST", challenge(score(6, 20), score(7, 90)).outcome());
    }

    @Test
    void given_the_same_right_answers_then_the_faster_wins_and_the_exact_same_is_a_draw() {
        assertEquals("WON", challenge(score(7, 30), score(7, 31)).outcome());
        assertEquals("LOST", challenge(score(7, 31), score(7, 30)).outcome());
        assertEquals("DRAW", challenge(score(7, 30), score(7, 30)).outcome());
    }

    // A record only writes its components; the outcome is worked out, so it has to be asked for.
    @Test
    void given_a_challenge_then_the_outcome_is_in_its_json() throws Exception {
        String json = new ObjectMapper().registerModule(new JavaTimeModule())
                .writeValueAsString(challenge(score(8, 30), score(7, 30)));
        assertTrue(json.contains("\"outcome\":\"WON\""), json);
    }
}
