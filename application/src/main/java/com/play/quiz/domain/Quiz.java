package com.play.quiz.domain;

import com.play.quiz.converter.QuestionIdsConverter;
import com.play.quiz.domain.helpers.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Q_QUIZ")
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Quiz extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_generator")
    @SequenceGenerator(name = "quiz_generator", sequenceName = "quiz_seq", allocationSize = 1)
    private Long quizId;

    @OneToOne(targetEntity = Category.class)
    @JoinColumn(name = "CAT_ID")
    private Category category;

    @OneToOne(targetEntity = QuizType.class)
    @JoinColumn(name = "TYPE")
    private QuizType type;

    @Column(name = "QUESTION_IDS")
    @Convert(converter = QuestionIdsConverter.class)
    private Set<Long> questionIds;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    @Column(name = "QUESTIONS_COUNT")
    private int questionsCount;

    // True for a quiz a player built from questions they wrote (Q_CUSTOM_QUESTION); false for one
    // made from the admins' or the system's questions (Q_QUESTION).
    @Column(name = "IS_CUSTOM")
    private boolean custom;

    // Seconds allowed per question. Null on the quizzes that time the whole run instead.
    @Column(name = "QUESTION_TIME")
    private Integer questionTime;

    @Transient
    private List<Question> questionList = new ArrayList<>(questionsCount);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Quiz quiz = (Quiz) o;
        return quizId != null && Objects.equals(quizId, quiz.quizId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Long getId() {
        return this.quizId;
    }
}
