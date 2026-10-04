package com.play.quiz.duel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * How a duel stands, worked out from the rounds' scores and the clock: nothing about it is stored.
 *
 * <p>Five rounds, first to three round wins. The challenger starts the odd rounds and the opponent
 * the even ones, and in each the starter plays first. A round goes to more right answers, then the
 * quicker run; a tie wins nobody the round. Whoever's turn it is has a day from the last move (or
 * the duel's start) to play it, or loses the duel.
 */
public final class DuelRules {

    public static final int ROUNDS = 5;
    public static final int TO_WIN = 3;
    public static final Duration TURN = Duration.ofHours(24);

    public enum Side { CHALLENGER, OPPONENT;

        public Side other() {
            return this == CHALLENGER ? OPPONENT : CHALLENGER;
        }
    }

    public record Score(int rightAnswers, int totalAnswers, Double spentTime, LocalDateTime at) {}

    /** A round's scores; either is null until that player has played it. */
    public record Round(Score challenger, Score opponent) {}

    /**
     * @param currentRound the round being played, 0 once the duel is over
     * @param turn         who has to play next; null once over
     * @param deadline     when the turn runs out; null once over
     * @param winner       null while on, and for a draw
     * @param forfeit      the duel ended because a turn ran out
     * @param endedAt      when it was decided; null while on
     */
    public record State(int challengerWins, int opponentWins, int currentRound, Side turn, LocalDateTime deadline,
                        boolean over, Side winner, boolean forfeit, LocalDateTime endedAt) {}

    private DuelRules() {
    }

    public static Side starter(final int roundNo) {
        return roundNo % 2 == 1 ? Side.CHALLENGER : Side.OPPONENT;
    }

    /** Above zero when the challenger's run wins the round, below when the opponent's does. */
    static int compare(final Score challenger, final Score opponent) {
        int byRight = Integer.compare(challenger.rightAnswers(), opponent.rightAnswers());
        if (byRight != 0) return byRight;
        return Double.compare(Optional.ofNullable(opponent.spentTime()).orElse(0.0),
                Optional.ofNullable(challenger.spentTime()).orElse(0.0));
    }

    public static State of(final LocalDateTime created, final Map<Integer, Round> rounds, final LocalDateTime now) {
        int challengerWins = 0;
        int opponentWins = 0;
        LocalDateTime lastMove = created;

        for (int no = 1; no <= ROUNDS; no++) {
            Round round = rounds.getOrDefault(no, new Round(null, null));
            lastMove = latest(lastMove, round);
            if (Objects.isNull(round.challenger()) || Objects.isNull(round.opponent())) {
                Side starter = starter(no);
                Score startersRun = starter == Side.CHALLENGER ? round.challenger() : round.opponent();
                Side turn = Objects.isNull(startersRun) ? starter : starter.other();
                LocalDateTime deadline = lastMove.plus(TURN);
                if (now.isAfter(deadline)) {
                    return new State(challengerWins, opponentWins, 0, null, null, true, turn.other(), true, deadline);
                }
                return new State(challengerWins, opponentWins, no, turn, deadline, false, null, false, null);
            }
            int result = compare(round.challenger(), round.opponent());
            if (result > 0) challengerWins++;
            if (result < 0) opponentWins++;
            if (challengerWins >= TO_WIN || opponentWins >= TO_WIN) break;
        }

        Side winner = challengerWins > opponentWins ? Side.CHALLENGER
                : opponentWins > challengerWins ? Side.OPPONENT : null;
        return new State(challengerWins, opponentWins, 0, null, null, true, winner, false, lastMove);
    }

    private static LocalDateTime latest(final LocalDateTime current, final Round round) {
        return Stream.of(round.challenger(), round.opponent())
                .filter(Objects::nonNull)
                .map(Score::at)
                .filter(Objects::nonNull)
                .reduce(current, (one, other) -> other.isAfter(one) ? other : one);
    }
}
