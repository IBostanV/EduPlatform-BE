package com.play.quiz.record;

import com.play.quiz.cosmetic.Cosmetic;
import com.play.quiz.domain.Account;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

// What other users may see of an account in pickers (friends, chat groups): an id to act on and a
// name to show. No email, birthday or roles. The photo comes only where it is shown (the friends
// list): it travels inside the response, so the longer lists are left without it.
//
// frame and nameColor are what they wear (com.play.quiz.cosmetic): the frame's code, drawn by the
// browser, and the name's colour itself. Null for none.
public record UserSummary(Long id, String displayName, byte[] photo, String frame, String nameColor) {

    // Username, else "Name Surname", else "Player #id": registration only asks for an email,
    // so many accounts have neither, and the email itself is not shown to other users.
    public static UserSummary of(final Account account) {
        return new UserSummary(account.getAccountId(), displayNameOf(account), null,
                account.getEquippedFrame(), Cosmetic.colorOf(account.getEquippedNameColor()));
    }

    /** For the one list that shows faces: the same summary, with the account's picture. */
    public static UserSummary withPhoto(final Account account) {
        return new UserSummary(account.getAccountId(), displayNameOf(account), account.getAvatar(),
                account.getEquippedFrame(), Cosmetic.colorOf(account.getEquippedNameColor()));
    }

    private static String displayNameOf(final Account account) {
        if (StringUtils.hasText(account.getUsername())) {
            return account.getUsername().trim();
        }
        String fullName = Stream.of(account.getName(), account.getSurname())
                .filter(StringUtils::hasText)
                .map(String::trim)
                .collect(Collectors.joining(" "));
        return fullName.isEmpty() ? "Player #" + account.getAccountId() : fullName;
    }
}
