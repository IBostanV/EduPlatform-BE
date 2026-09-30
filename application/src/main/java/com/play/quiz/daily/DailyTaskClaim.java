package com.play.quiz.daily;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * What a player has already been paid for today, one row per task.
 *
 * <p>The row is the payment: progress itself is worked out from the day's quiz runs, so this
 * table exists only to keep a task from paying twice when the list is read again.
 *
 * <p>The account is a plain id rather than a relation — nothing here is ever read through the
 * player — and rows are written by the conditional insert in the repository, which is why there
 * is nothing to set on this class.
 */
@Entity
@Table(name = "Q_DAILY_TASK")
@Getter
@NoArgsConstructor
public class DailyTaskClaim {

    @Id
    @Column(name = "TASK_ID")
    private Long taskId;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "TASK_CODE")
    private String taskCode;

    @Column(name = "TASK_DAY")
    private LocalDate taskDay;

    @Column(name = "EXPERIENCE")
    private int experience;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
}
