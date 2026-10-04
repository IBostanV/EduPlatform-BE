package com.play.quiz.review;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A question a player got wrong, on a rung of the review ladder ({@link ReviewService}). */
@Entity
@Table(name = "Q_MISTAKE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mistake {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mistake_generator")
    @SequenceGenerator(name = "mistake_generator", sequenceName = "mistake_seq", allocationSize = 1)
    @Column(name = "MISTAKE_ID")
    private Long mistakeId;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "QUESTION_ID")
    private Long questionId;

    @Column(name = "BOX")
    private int box;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;
}
