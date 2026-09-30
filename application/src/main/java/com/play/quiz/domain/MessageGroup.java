package com.play.quiz.domain;

import com.play.quiz.domain.helpers.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "Q_MESSAGE_GROUP")
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_group_generator")
    @SequenceGenerator(name = "message_group_generator", sequenceName = "message_group_seq", allocationSize = 1)
    private Long groupId;
    private String name;

    // The group's picture, avatar-sized (the browser shrinks it before sending), with its type.
    @ToString.Exclude
    private byte[] photo;

    @Column(name = "PHOTO_TYPE")
    private String photoType;

    @Override
    public Long getId() {
        return this.groupId;
    }
}
