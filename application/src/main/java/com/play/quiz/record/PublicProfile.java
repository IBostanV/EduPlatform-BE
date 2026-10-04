package com.play.quiz.record;

import java.util.List;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Category;
import com.play.quiz.domain.UserOccupation;
import com.play.quiz.iq.IqResult;
import com.play.quiz.trophy.TrophyCatalog.TrophyFace;

// Another player's profile page, read-only. What a player chose to show about themselves (names,
// photo, occupations, favourite categories, trophy) and what they earned (level, IQ result).
// Never the email, birthday or roles, as in UserSummary, and never the quiz history.
public record PublicProfile(Long id,
                            String displayName,
                            String username,
                            String name,
                            String surname,
                            byte[] avatar,
                            PlayerLevel playerLevel,
                            TrophyFace trophy,
                            List<String> occupations,
                            List<String> favoriteCategories,
                            IqResult iq,
                            String frame,
                            String nameColor) {

    public static PublicProfile of(final Account account, final TrophyFace trophy, final IqResult iq) {
        return new PublicProfile(account.getAccountId(),
                UserSummary.of(account).displayName(),
                account.getUsername(),
                account.getName(),
                account.getSurname(),
                account.getAvatar(),
                PlayerLevel.of(account.getExperience()),
                trophy,
                account.getOccupations().stream().map(UserOccupation::getName).sorted().toList(),
                account.getFavoriteCategories().stream().map(Category::getName).sorted().toList(),
                iq,
                account.getEquippedFrame(),
                com.play.quiz.cosmetic.Cosmetic.colorOf(account.getEquippedNameColor()));
    }
}
