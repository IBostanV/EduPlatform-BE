package com.play.quiz.appearance;

import java.time.LocalDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** The picture a player uploaded as their background; one per player, a new upload replaces it. */
@Entity
@Table(name = "Q_USER_BACKGROUND")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserBackground {

    @Id
    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    /** What the picture's address carries: random, and new with every upload. */
    @Column(name = "PUBLIC_ID")
    private String publicId;

    @Column(name = "CONTENT_TYPE")
    private String contentType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "IMAGE")
    private byte[] image;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
}
