package com.play.quiz.feed;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A level somebody reached, and when.
 *
 * <p>The level itself is worked out from the experience collected, which says where a player is
 * but not when they got there — and a friend's news is about the when. Written by the insert in
 * {@link LevelUpRepository} whenever experience crosses a level, so there is nothing to set here.
 */
@Entity
@Table(name = "Q_LEVEL_UP")
@Getter
@NoArgsConstructor
public class LevelUp {

    @Id
    @Column(name = "LEVEL_UP_ID")
    private Long levelUpId;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "LEVEL_NO")
    private int level;

    @Column(name = "REACHED_DATE")
    private LocalDateTime reachedDate;
}
