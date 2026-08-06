package com.play.quiz.conquest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The conquest map as it stands: the round in hand, when it turns, and every country the game
 * knows about with its conqueror.
 *
 * <p>{@code openCountries} are the ones this round put up for the taking; the rest are listed so
 * the map can still show who holds them.
 *
 * <p>{@code closesAt} is when the round ends and its winners stand. Inside a round the game is
 * open every other day: {@code openUntil} is when today's open spell ends (while open), and
 * {@code nextOpenAt} when the next one starts (while shut).
 */
public record ConquestState(long round,
                            boolean open,
                            LocalDateTime opensAt,
                            LocalDateTime closesAt,
                            LocalDateTime nextRoundAt,
                            LocalDateTime openUntil,
                            LocalDateTime nextOpenAt,
                            List<ConquestCountry> countries,
                            List<TeamStanding> teams,
                            Team yourTeam) {

    /** A chat group playing as a team: a country counts for it when its holder plays for it. */
    public record Team(Long id, String name) {}

    public record TeamStanding(Team team, long countries) {}
}
