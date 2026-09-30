package com.play.quiz.social;

import java.util.Objects;

import com.play.quiz.domain.helpers.BaseEntity;
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
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

/**
 * A player's reaction to a line of news: a friend's level, conquest, post or challenge. The line is
 * named by its feed key ("FRIEND_POST-12"), the same key the feed already gives every line, so a
 * reaction needs nothing from the thing it reacts to.
 */
@Entity
@Table(name = "Q_REACTION")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Reaction extends BaseEntity {

    /** Stored as a word, drawn as an emoji by the browser: the database never has to hold one. */
    public enum Kind { FIRE, CLAP, WOW, HEART }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reaction_generator")
    @SequenceGenerator(name = "reaction_generator", sequenceName = "reaction_seq", allocationSize = 1)
    @Column(name = "REACTION_ID")
    private Long reactionId;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "ITEM_KEY")
    private String itemKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "KIND")
    private Kind kind;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Reaction that = (Reaction) o;
        return reactionId != null && Objects.equals(reactionId, that.reactionId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Long getId() {
        return reactionId;
    }
}
