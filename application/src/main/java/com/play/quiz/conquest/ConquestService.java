package com.play.quiz.conquest;

import java.time.LocalDateTime;
import java.util.List;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Category;

public interface ConquestService {

    /** The map as it stands for the signed-in player: the round, and every country with its holder. */
    ConquestState getState();

    /**
     * Records a finished quiz run as a go at conquering a country.
     *
     * @param countryId the country's category
     * @param historyId the run, which already carries its own score and time
     * @return the map as it stands once the attempt is in
     */
    ConquestState recordAttempt(Long countryId, Long historyId);

    /** Plays for one of the reader's chat groups from now on, or on their own with null. */
    ConquestState setTeam(Long groupId);

    /**
     * The rounds that closed after {@code since}, and in each one the countries that changed
     * hands. A conqueror who beat their own record has not changed hands and is not listed.
     */
    List<ConquestChange> changesSince(LocalDateTime since);

    /** The countries each round that opened after {@code since} put up for the taking. */
    List<ConquestRound> roundsOpenedSince(LocalDateTime since);

    /**
     * A country taken in a round. {@code previous} is null when nobody held it before.
     * {@code at} is when the round closed, on the clock this service keeps (UTC).
     */
    record ConquestChange(long round, LocalDateTime at, Category country, Account holder, Account previous) {}

    /** {@code at} is when the round opened, on the clock this service keeps (UTC). */
    record ConquestRound(long round, LocalDateTime at, List<Category> countries) {}
}
