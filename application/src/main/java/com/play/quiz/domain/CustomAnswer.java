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
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * One of a custom question's answers: a right one, or a wrong option written by the player.
 * For a put-in-order question the right answers' positions are the right order.
 */
@Entity
@Table(name = "Q_CUSTOM_ANSWER")
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_answer_generator")
    @SequenceGenerator(name = "custom_answer_generator", sequenceName = "custom_answer_seq", allocationSize = 1)
    @Column(name = "ANSWER_ID")
    private Long answerId;

    @Setter
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_ID")
    private CustomQuestion question;

    private String content;

    @Column(name = "IS_RIGHT")
    private boolean right;

    private int position;

    @Override
    public Long getId() {
        return this.answerId;
    }
}
