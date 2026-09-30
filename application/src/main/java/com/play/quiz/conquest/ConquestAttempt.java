package com.play.quiz.conquest;

import java.time.LocalDateTime;
import java.util.Objects;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Category;
import com.play.quiz.domain.helpers.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

/**
 * One finished go at conquering a country. The score is copied off the quiz run rather than
 * pointed at, so reading a leaderboard is one table.
 *
 * <p>There is no "who holds this country" row anywhere: the holder is the best attempt from a
 * closed round, which is what keeps a conqueror in place until somebody actually beats them.
 */
@Entity
@Table(name = "Q_CONQUEST_ATTEMPT")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ConquestAttempt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conquest_attempt_generator")
    @SequenceGenerator(name = "conquest_attempt_generator", sequenceName = "conquest_attempt_seq", allocationSize = 1)
    private Long attemptId;

    @Column(name = "ROUND_NO")
    private long roundNo;

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "CAT_ID")
    @ToString.Exclude
    private Category country;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    @ToString.Exclude
    private Account account;

    // The quiz run this was: one run counts once, which the unique key enforces.
    @Column(name = "HISTORY_ID")
    private Long historyId;

    @Column(name = "RIGHT_ANSWERS")
    private int rightAnswers;

    @Column(name = "TOTAL_ANSWERS")
    private int totalAnswers;

    @Column(name = "SPENT_TIME")
    private Double spentTime;

    // Set once, when this attempt is found holding its country after the round shut.
    @Column(name = "BONUS_PAID")
    private boolean bonusPaid;

    /** When it was taken; the earlier of two equal runs keeps the country. */
    public LocalDateTime takenAt() {
        return getCreatedDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ConquestAttempt that = (ConquestAttempt) o;
        return attemptId != null && Objects.equals(attemptId, that.attemptId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Long getId() {
        return this.attemptId;
    }
}
