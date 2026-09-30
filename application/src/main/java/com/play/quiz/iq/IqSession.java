package com.play.quiz.iq;

import java.time.LocalDateTime;

import com.play.quiz.domain.Account;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * One go at the test, from the first question to the number at the end.
 *
 * <p>The session holds which item is on screen and when it was sent, because the clock is the
 * server's: a question answered after its time is up is wrong however long the browser says it
 * took.
 */
@Entity
@Table(name = "Q_IQ_SESSION")
@Getter
@Setter
@NoArgsConstructor
public class IqSession {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "iq_session_generator")
    @SequenceGenerator(name = "iq_session_generator", sequenceName = "iq_session_seq", allocationSize = 1)
    private Long sessionId;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Column(name = "STARTED_DATE")
    private LocalDateTime startedDate;

    @Column(name = "FINISHED_DATE")
    private LocalDateTime finishedDate;

    /** The question on screen, and when it went out. Both null once the test is over. */
    @Column(name = "CURRENT_ITEM_ID")
    private Long currentItemId;

    @Column(name = "SERVED_DATE")
    private LocalDateTime servedDate;

    @Column(name = "ANSWERED")
    private int answered;

    @Column(name = "THETA")
    private Double theta;

    @Column(name = "STANDARD_ERROR")
    private Double standardError;

    @Column(name = "IQ")
    private Integer iq;

    @Column(name = "PERCENTILE")
    private Integer percentile;

    /**
     * Whether the score was read off the app's own takers rather than off the model's assumption
     * about them. False until there are enough finished tests to norm against.
     */
    @Column(name = "NORMED")
    private boolean normed;

    /** How many times this player had finished the test before this one; practice shows. */
    @Column(name = "ATTEMPT_NO")
    private int attemptNo;
}
