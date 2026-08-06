package com.play.quiz.live;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import com.play.quiz.dto.AnswerDto;
import com.play.quiz.dto.QuestionDto;
import com.play.quiz.record.MiniGameResult;
import com.play.quiz.record.UserSummary;

/**
 * One live match as it stands: who is in, which question is up, and what everyone scored. Held in
 * memory only, for as long as the match lasts; every change to it is made under its own lock by
 * {@link LiveService}, which pushes the result to the players.
 */
final class LiveRoom {

    enum Mode { DUEL, ROOM }

    enum Phase { LOBBY, COUNTDOWN, QUESTION, REVEAL, FINISHED, CLOSED }

    static final class Player {
        final Long id;
        final String email;
        final UserSummary user;
        int score;
        int correct;
        Boolean lastCorrect;
        int lastPoints;
        boolean left;

        Player(final Long id, final String email, final UserSummary user) {
            this.id = id;
            this.email = email;
            this.user = user;
        }
    }

    final String code;
    final Mode mode;
    final Long hostId;
    final int questionCount;
    final int seconds;
    final int maxPlayers;

    Phase phase = Phase.LOBBY;
    int index = -1;
    List<QuestionDto> questions = List.of();
    /** When the current phase runs out, for the players' clocks. */
    Instant phaseEndsAt;
    Instant questionStartedAt;
    Instant lastActivity = Instant.now();

    final Map<Long, Player> players = new LinkedHashMap<>();
    /** Invited and not in yet: a duel's opponent, or friends and group members asked to a room. */
    final Map<Long, UserSummary> invited = new LinkedHashMap<>();
    final Set<Long> declined = new LinkedHashSet<>();
    /** This question's answers, by player. */
    final Map<Long, AnswerDto> answers = new HashMap<>();
    MiniGameResult reveal;

    ScheduledFuture<?> timer;

    LiveRoom(final String code, final Mode mode, final Long hostId, final int questionCount, final int seconds) {
        this.code = code;
        this.mode = mode;
        this.hostId = hostId;
        this.questionCount = questionCount;
        this.seconds = seconds;
        this.maxPlayers = mode == Mode.DUEL ? 2 : 20;
    }

    List<Player> active() {
        return players.values().stream().filter(player -> !player.left).toList();
    }

    boolean over() {
        return phase == Phase.FINISHED || phase == Phase.CLOSED;
    }
}
