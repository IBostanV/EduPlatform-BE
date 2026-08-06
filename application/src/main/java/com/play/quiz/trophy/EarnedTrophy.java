package com.play.quiz.trophy;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A trophy somebody has earned, and when.
 *
 * <p>The one thing about a trophy worth storing: everything else is worked out from what the
 * player has done. Keeping the moment means a trophy earned at a hundred quizzes stays earned if
 * a run is ever deleted, and that the shelf can say when each one arrived.
 *
 * <p>Written by the conditional insert in the repository, so there is nothing to set here.
 */
@Entity
@Table(name = "Q_USER_TROPHY")
@Getter
@NoArgsConstructor
public class EarnedTrophy {

    @Id
    @Column(name = "USER_TROPHY_ID")
    private Long userTrophyId;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "EARNED_DATE")
    private LocalDateTime earnedDate;
}
