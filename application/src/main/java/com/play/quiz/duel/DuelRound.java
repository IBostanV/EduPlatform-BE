package com.play.quiz.duel;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** A round's questions: a stored quiz, set when the round's first player starts it. */
@Entity
@Table(name = "Q_DUEL_ROUND")
@IdClass(DuelRound.Key.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DuelRound {

    @Id
    @Column(name = "DUEL_ID")
    private Long duelId;

    @Id
    @Column(name = "ROUND_NO")
    private Integer roundNo;

    @Column(name = "QUIZ_ID")
    private Long quizId;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key implements Serializable {
        private Long duelId;
        private Integer roundNo;
    }
}
