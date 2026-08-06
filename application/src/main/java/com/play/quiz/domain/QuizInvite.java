package com.play.quiz.domain;

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

/**
 * One row per person invited to a quiz. Who sent it is the audited CREATED_BY, so the creator is
 * not stored twice.
 */
@Entity
@Table(name = "Q_QUIZ_INVITE")
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuizInvite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_invite_generator")
    @SequenceGenerator(name = "quiz_invite_generator", sequenceName = "quiz_invite_seq", allocationSize = 1)
    @Column(name = "INVITE_ID")
    private Long inviteId;

    @ToString.Exclude
    @ManyToOne(targetEntity = Quiz.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "QUIZ_ID")
    private Quiz quiz;

    @ToString.Exclude
    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Override
    public Long getId() {
        return this.inviteId;
    }
}
