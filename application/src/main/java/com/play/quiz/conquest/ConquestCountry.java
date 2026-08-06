package com.play.quiz.conquest;

import com.play.quiz.domain.Category;
import com.play.quiz.record.UserSummary;

/**
 * One country on the conquest map: its quiz, who holds it, and whether it can be taken right now.
 *
 * <p>{@code code} is the category's natural id, the ISO3 the browser's map already knows its
 * shapes by, so the two sides need nothing else to agree on which country this is.
 *
 * <p>{@code categoryId} is what a quiz on this country is built from; null holder means nobody
 * has conquered it yet.
 */
public record ConquestCountry(Long categoryId,
                              String code,
                              String name,
                              boolean open,
                              UserSummary heldBy,
                              Integer rightAnswers,
                              Integer totalAnswers,
                              Double spentTime,
                              boolean attemptAllowed,
                              String attemptBlockedReason,
                              ConquestState.Team team) {

    public static ConquestCountry of(final Category country, final boolean open, final ConquestAttempt holder,
                                     final boolean attemptAllowed, final String blockedReason) {
        return new ConquestCountry(
                country.getCatId(),
                country.getNaturalId(),
                country.getName(),
                open,
                holder == null ? null : UserSummary.of(holder.getAccount()),
                holder == null ? null : holder.getRightAnswers(),
                holder == null ? null : holder.getTotalAnswers(),
                holder == null ? null : holder.getSpentTime(),
                attemptAllowed,
                blockedReason,
                null);
    }

    /** The same country, with the team its holder plays for. */
    public ConquestCountry withTeam(final ConquestState.Team holderTeam) {
        return new ConquestCountry(categoryId, code, name, open, heldBy, rightAnswers, totalAnswers, spentTime,
                attemptAllowed, attemptBlockedReason, holderTeam);
    }
}
