package com.play.quiz.cosmetic;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** A cosmetic a player owns, by its {@link Cosmetic} code. */
@Entity
@Table(name = "Q_USER_ITEM")
@IdClass(OwnedItem.Key.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnedItem {

    @Id
    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Id
    @Column(name = "ITEM_CODE")
    private String itemCode;

    @Column(name = "ACQUIRED_DATE")
    private LocalDateTime acquiredDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key implements Serializable {
        private Long accountId;
        private String itemCode;
    }
}
