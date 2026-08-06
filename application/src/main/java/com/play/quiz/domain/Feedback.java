package com.play.quiz.domain;

import com.play.quiz.domain.helpers.BaseEntity;
import com.play.quiz.enums.FeedbackType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A bug report, question or suggestion a player or a guest sent the admins. Who sent it and when
 * are the audited CREATED_BY (empty for a guest) and CREATED_DATE; resolving it records the admin
 * in UPDATED_BY.
 */
@Entity
@Table(name = "Q_FEEDBACK")
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Feedback extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedback_generator")
    @SequenceGenerator(name = "feedback_generator", sequenceName = "feedback_seq", allocationSize = 1)
    @Column(name = "FEEDBACK_ID")
    private Long feedbackId;

    @Enumerated(EnumType.STRING)
    private FeedbackType type;

    private String message;

    // The app page the player was on when they sent it.
    private String page;

    // Only for a problem reported from inside a quiz: which question, e.g. "Question #12: ...".
    private String question;

    // A picture of the problem, if one was attached, with its media type.
    @ToString.Exclude
    private byte[] screenshot;

    @Column(name = "SCREENSHOT_TYPE")
    private String screenshotType;

    // Only from guests, who have no account to answer: where to reply, if they left one.
    @Column(name = "CONTACT_EMAIL")
    private String contactEmail;

    @Setter
    private boolean resolved;

    @Override
    public Long getId() {
        return this.feedbackId;
    }
}
