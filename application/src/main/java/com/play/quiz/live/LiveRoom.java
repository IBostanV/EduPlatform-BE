package com.play.quiz.live;

import java.time.Instant;
import java.util.ArrayList;
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

    /** PAIRS is not a quiz: 2 to 4 players take turns turning two cards over, looking for pairs. */
    enum Mode { DUEL, ROOM, PAIRS }

    /** TURN and MISMATCH are the pairs game's: a player picking, and a wrong pair on show before it turns back. */
    enum Phase { LOBBY, COUNTDOWN, QUESTION, REVEAL, TURN, MISMATCH, FINISHED, CLOSED }

    static final class Player {
        final Long id;
        final String email;
        final UserSummary user;
        int score;
        int correct;
        Boolean lastCorrect;
        int lastPoints;
        boolean left;
        /** Coins won at the end of a game of pairs. */
        int coins;

        Player(final Long id, final String email, final UserSummary user) {
            this.id = id;
            this.email = email;
            this.user = user;
        }
    }

    final String code;
    final Mode mode;
    /** Changes only when a rematch is opened after the host left the match. */
    Long hostId;
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

    // The pairs game. questionCount is its number of pairs, seconds the time a turn has, and index
    // counts the turns, so a timer from a turn that is over does nothing.
    /** The face on every card, two of each, in table order. */
    int[] faces = new int[0];
    /** Who took each card; null while it is still on the table. */
    Long[] takenBy = new Long[0];
    /** The cards face up this turn: none, one, or the two of a wrong pair on show. */
    final List<Integer> turned = new ArrayList<>();
    Long turn;

    ScheduledFuture<?> timer;

    LiveRoom(final String code, final Mode mode, final Long hostId, final int questionCount, final int seconds) {
        this.code = code;
        this.mode = mode;
        this.hostId = hostId;
        this.questionCount = questionCount;
        this.seconds = seconds;
        this.maxPlayers = switch (mode) {
            case DUEL -> 2;
            case PAIRS -> 4;
            case ROOM -> 20;
        };
    }

    List<Player> active() {
        return players.values().stream().filter(player -> !player.left).toList();
    }

    boolean over() {
        return phase == Phase.FINISHED || phase == Phase.CLOSED;
    }
}
