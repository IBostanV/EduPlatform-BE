package com.play.quiz.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import com.play.quiz.domain.helpers.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

@Entity
@Table(name = "Q_USER_HISTORY")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserQuizHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "history_generator")
    @SequenceGenerator(name = "history_generator", sequenceName = "user_history_seq", allocationSize = 1)
    private Long historyId;

    @OneToOne(targetEntity = Account.class)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne(targetEntity = Quiz.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "QUIZ_ID")
    private Quiz quiz;

    @Column(name = "ANSWERS_JSON")
    private String answersJson;

    @Column(name = "COMPLETED_DATE")
    private LocalDateTime completedDate;

    @Column(name = "SPENT_TIME")
    private Double spentTime;

    // The score, marked once when the run is recorded. Null on runs taken before it was kept,
    // which the profile shows as a dash rather than as nought out of nought.
    @Setter
    @Column(name = "RIGHT_ANSWERS")
    private Integer rightAnswers;

    @Setter
    @Column(name = "TOTAL_ANSWERS")
    private Integer totalAnswers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserQuizHistory that = (UserQuizHistory) o;
        return historyId != null && Objects.equals(historyId, that.historyId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Long getId() {
        return this.historyId;
    }
}
