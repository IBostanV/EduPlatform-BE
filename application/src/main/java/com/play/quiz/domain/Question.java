package com.play.quiz.domain;

import com.play.quiz.converter.AttributeListConverter;
import com.play.quiz.domain.helpers.BaseEntity;
import com.play.quiz.domain.translation.AnswerTranslation;
import com.play.quiz.domain.translation.QuestionTranslation;
import com.play.quiz.enums.QuestionAttribute;
import com.play.quiz.enums.QuestionType;
import com.play.quiz.util.Numbers;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Q_QUESTION")
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qstn_generator")
    @SequenceGenerator(name = "qstn_generator", sequenceName = "questions_seq", allocationSize = 1)
    private Long questionId;

    @Setter
    @OneToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    @ToString.Exclude
    private Account account;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Transient
    private Object tipId;

    private String topic;
    private int priority;
    private String content;
    private Long excludeType;

    @OneToOne(targetEntity = Category.class)
    @JoinColumn(name = "CAT_ID")
    private Category category;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @Column(name = "COMPLEXITY_LEVEL")
    private int complexityLevel;

    @Convert(converter = AttributeListConverter.class)
    private List<QuestionAttribute> attributes;

    @Setter
    @OneToMany(mappedBy = "question",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    @ToString.Exclude
    private List<Answer> answers;

    @OneToMany(mappedBy = "question",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @ToString.Exclude
    private List<QuestionTranslation> translations;

    public Question copy(final QuestionType questionType, String content) {
        return copy(questionType, content, Collections.emptyList());
    }

    public Question copy(final QuestionType questionType, String content, final List<QuestionAttribute> attributes) {
        return new Question(null, this.account, questionType, this.tipId, this.topic, this.priority, content,
                this.excludeType, this.category, this.isActive, this.complexityLevel, attributes, this.answers,
                this.translations);
    }

    // Q_QUIZ_TYPE bit values of the types only some questions can be played as.
    private static final long MULTIPLE_CHOICE = 2;
    private static final long DRAG_AND_DROP = 16;
    private static final long IN_ORDER = 128;

    /**
     * Every new question, however made, also excludes the quiz types its answers cannot be played
     * as: multiple choice needs several right answers, drag and drop several glossary terms to pair
     * each key with its value, in order several of them whose values are numbers to sort by. The
     * migration exclude_unfit_quiz_types did the same to the questions already there.
     */
    @PrePersist
    void excludeUnfitTypes() {
        List<Answer> all = Objects.requireNonNullElse(answers, List.of());
        boolean byValue = all.size() >= 2
                && all.stream().allMatch(answer -> Objects.nonNull(answer.getGlossary()))
                && !Objects.requireNonNullElse(attributes, List.<QuestionAttribute>of()).contains(QuestionAttribute.ANSWER_BY_KEY);
        boolean numbers = all.stream().allMatch(answer -> Objects.nonNull(Numbers.parse(answer.getContent())));
        long unfit = (all.size() >= 2 ? 0 : MULTIPLE_CHOICE)
                | (byValue ? 0 : DRAG_AND_DROP)
                | (byValue && numbers ? 0 : IN_ORDER);
        excludeType = Objects.requireNonNullElse(excludeType, 0L) | unfit;
    }

    public void fillTranslationsParent() {
        this.translations.forEach(translation -> translation.setQuestion(this));
    }

    public void fillAnswersParent() {
        this.answers.forEach(answer -> {
            answer.setQuestion(this);
            Objects.requireNonNullElse(answer.getAnswerTranslations(), List.<AnswerTranslation>of())
                    .forEach(translation -> translation.setAnswer(answer));
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Question question = (Question) o;
        return questionId != null && Objects.equals(questionId, question.questionId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Long getId() {
        return this.questionId;
    }
}
