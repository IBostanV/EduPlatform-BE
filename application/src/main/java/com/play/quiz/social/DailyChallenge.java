package com.play.quiz.social;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The quiz everyone plays on a given day. Its runs are ordinary quiz history under this quiz id,
 * which is all the day's table needs: nothing else about the day is stored.
 */
@Entity
@Table(name = "Q_DAILY_CHALLENGE")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyChallenge {

    @Id
    @Column(name = "CHALLENGE_DAY")
    private LocalDate day;

    @Column(name = "QUIZ_ID")
    private Long quizId;
}
