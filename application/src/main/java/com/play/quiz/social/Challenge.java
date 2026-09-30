package com.play.quiz.social;

import java.util.Objects;

import com.play.quiz.domain.helpers.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

/**
 * "Beat my score": a player sends a quiz they finished to a friend, who plays the very same
 * questions. Only who challenged whom on what is kept — both scores are the players' own runs of
 * that quiz, read from their history, so there is no result here to go out of step with them.
 */
@Entity
@Table(name = "Q_CHALLENGE")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Challenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "challenge_generator")
    @SequenceGenerator(name = "challenge_generator", sequenceName = "challenge_seq", allocationSize = 1)
    @Column(name = "CHALLENGE_ID")
    private Long challengeId;

    @Column(name = "QUIZ_ID")
    private Long quizId;

    @Column(name = "CHALLENGER_ID")
    private Long challengerId;

    // The run the challenge was sent from: the score to beat.
    @Column(name = "CHALLENGER_HISTORY_ID")
    private Long challengerHistoryId;

    @Column(name = "OPPONENT_ID")
    private Long opponentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Challenge that = (Challenge) o;
        return challengeId != null && Objects.equals(challengeId, that.challengeId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Long getId() {
        return challengeId;
    }
}
