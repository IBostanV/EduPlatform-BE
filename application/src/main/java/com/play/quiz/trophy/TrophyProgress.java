package com.play.quiz.trophy;

import java.time.LocalDateTime;

/**
 * A trophy as one player sees it.
 *
 * <p>A secret one that has not been earned comes with no title and no description: the whole
 * point of it is that it is found rather than worked toward. Everything else shows what it takes
 * and how far along they are.
 */
public record TrophyProgress(String code,
                             TrophyGroup group,
                             String title,
                             String description,
                             String icon,
                             boolean secret,
                             Long categoryId,
                             long progress,
                             long target,
                             boolean earned,
                             LocalDateTime earnedDate,
                             boolean preferred) {
}
