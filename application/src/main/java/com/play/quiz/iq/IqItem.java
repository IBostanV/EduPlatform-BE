package com.play.quiz.iq;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * One question in the IQ item bank, with what it is worth on the ability scale.
 *
 * <p>{@code difficulty} starts at what the template's rules are reckoned to be worth and is
 * corrected from real answers once enough people have seen the item: see
 * {@code IqService.recalibrate}. The running totals below are what that correction is worked out
 * from — how many saw it, how many got it right, and how able those people turned out to be.
 *
 * <p>{@code payload} is the figure or the series the browser draws, as JSON; the answer is not in
 * it, and never leaves the server until the answer is given.
 */
@Entity
@Table(name = "Q_IQ_ITEM")
@Getter
@NoArgsConstructor
public class IqItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "iq_item_generator")
    @SequenceGenerator(name = "iq_item_generator", sequenceName = "iq_item_seq", allocationSize = 1)
    private Long itemId;

    @Column(name = "CODE")
    private String code;

    /** MATRIX, SERIES, ODD or ANALOGY — what the browser has to draw. */
    @Column(name = "ITEM_TYPE")
    private String itemType;

    @Lob
    @Column(name = "PAYLOAD")
    private String payload;

    @Column(name = "ANSWER_INDEX")
    private int answerIndex;

    @Setter
    @Column(name = "DIFFICULTY")
    private double difficulty;

    @Setter
    @Column(name = "ATTEMPTS")
    private int attempts;

    @Setter
    @Column(name = "CORRECT_COUNT")
    private int correctCount;

    /** The abilities of everyone who has answered it, added up; the mean is what recalibration uses. */
    @Setter
    @Column(name = "THETA_SUM")
    private double thetaSum;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
}
