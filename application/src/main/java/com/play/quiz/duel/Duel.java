package com.play.quiz.duel;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A turn-based duel between two friends. Only who plays whom is stored; the rounds' quizzes are in
 * {@link DuelRound}, and everything else (whose turn, the score, the winner) is read off the runs.
 */
@Entity
@Table(name = "Q_DUEL")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Duel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "duel_generator")
    @SequenceGenerator(name = "duel_generator", sequenceName = "duel_seq", allocationSize = 1)
    @Column(name = "DUEL_ID")
    private Long duelId;

    @Column(name = "CHALLENGER_ID")
    private Long challengerId;

    @Column(name = "OPPONENT_ID")
    private Long opponentId;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
}
