package com.play.quiz.season;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A season-pass payment: a weekly quest ("QUEST:&lt;code&gt;", {@code periodStart} its week's Monday)
 * or a tier ("TIER:&lt;n&gt;", {@code periodStart} its season's first day). Written once, by the
 * conditional insert in {@link SeasonClaimRepository}.
 */
@Entity
@Table(name = "Q_SEASON_CLAIM")
@IdClass(SeasonClaim.Key.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeasonClaim {

    @Id
    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Id
    @Column(name = "CODE")
    private String code;

    @Id
    @Column(name = "PERIOD_START")
    private LocalDate periodStart;

    @Column(name = "POINTS")
    private int points;

    @Column(name = "CLAIMED_DATE")
    private LocalDateTime claimedDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key implements Serializable {
        private Long accountId;
        private String code;
        private LocalDate periodStart;
    }
}
