package com.play.quiz.group;

import java.time.LocalDateTime;

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

/**
 * A group players create and post in. A public one anyone signed in can read and join; a private
 * one shows outsiders only its name and description, and joining it is a request its owner
 * answers. Not a chat group (MessageGroup): that is a conversation, this is a community.
 */
@Entity
@Table(name = "Q_SOCIAL_GROUP")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "social_group_generator")
    @SequenceGenerator(name = "social_group_generator", sequenceName = "social_group_seq", allocationSize = 1)
    @Column(name = "GROUP_ID")
    private Long groupId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_PRIVATE")
    private boolean privateGroup;

    @Column(name = "OWNER_ID")
    private Long ownerId;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
}
