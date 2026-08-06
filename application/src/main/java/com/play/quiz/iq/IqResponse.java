package com.play.quiz.iq;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/** One answer: which item, what was picked, whether it was right, and how long it took. */
@Entity
@Table(name = "Q_IQ_RESPONSE")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class IqResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "iq_response_generator")
    @SequenceGenerator(name = "iq_response_generator", sequenceName = "iq_response_seq", allocationSize = 1)
    private Long responseId;

    @ManyToOne(targetEntity = IqSession.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "SESSION_ID")
    private IqSession session;

    @ManyToOne(targetEntity = IqItem.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private IqItem item;

    @Column(name = "CHOSEN_INDEX")
    private int chosenIndex;

    @Column(name = "CORRECT")
    private boolean correct;

    @Column(name = "SECONDS")
    private int seconds;
}
