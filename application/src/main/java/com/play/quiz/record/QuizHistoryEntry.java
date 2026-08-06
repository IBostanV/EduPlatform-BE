package com.play.quiz.record;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import com.play.quiz.domain.Quiz;
import com.play.quiz.domain.UserQuizHistory;

/**
 * One finished run as the profile's history lists it. A quiz has no name of its own, so it is
 * named by the category it came from and the type it was played as.
 *
 * <p>The answers are not here: the list would carry every question of every run the player has
 * ever taken. Opening a row reads them from /user-history/history/{historyId}, which is what the
 * result page reads too.
 *
 * <p>rightAnswers and totalAnswers are null on runs recorded before the score was kept.
 *
 * <p>quizId and categoryId are what "play it again" needs: a custom quiz is replayed by its id,
 * a categorized one by starting a new quiz on its category. An express run has neither to go on.
 */
public record QuizHistoryEntry(Long historyId,
                               String category,
                               String quizType,
                               boolean custom,
                               Integer rightAnswers,
                               Integer totalAnswers,
                               Double spentTime,
                               LocalDateTime completedAt,
                               Long quizId,
                               Long categoryId) {

    public static QuizHistoryEntry of(final UserQuizHistory history) {
        Quiz quiz = history.getQuiz();

        return new QuizHistoryEntry(
                history.getHistoryId(),
                Optional.ofNullable(quiz.getCategory()).map(category -> category.getName()).orElse(null),
                Optional.ofNullable(quiz.getType()).map(type -> type.getName()).orElse(null),
                quiz.isCustom(),
                history.getRightAnswers(),
                history.getTotalAnswers(),
                history.getSpentTime(),
                Objects.nonNull(history.getCompletedDate()) ? history.getCompletedDate() : history.getCreatedDate(),
                quiz.getQuizId(),
                Optional.ofNullable(quiz.getCategory()).map(category -> category.getCatId()).orElse(null));
    }
}
