package com.play.quiz.domain;

import java.util.List;

import com.play.quiz.domain.helpers.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A question a player wrote for their own quiz. Separate from Question: it never reaches a public
 * quiz, and keeps its wrong options with its right answers. Its quiz lists it in QUESTION_IDS.
 */
@Entity
@Table(name = "Q_CUSTOM_QUESTION")
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_question_generator")
    @SequenceGenerator(name = "custom_question_generator", sequenceName = "custom_question_seq", allocationSize = 1)
    @Column(name = "QUESTION_ID")
    private Long questionId;

    private String content;

    // Where it comes in its quiz; QUESTION_IDS is a set, so this keeps the order written.
    private int position;

    // Saved and removed with the question.
    @ToString.Exclude
    @OrderBy("position")
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<CustomAnswer> answers;

    /** Points every answer back at this question, so the cascade writes their QUESTION_ID. */
    public CustomQuestion fillAnswersParent() {
        this.answers.forEach(answer -> answer.setQuestion(this));
        return this;
    }

    @Override
    public Long getId() {
        return this.questionId;
    }
}
