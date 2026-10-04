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

/** Something a member wrote in a group. Reacted to through Q_REACTION as "GROUP_POST-<id>". */
@Entity
@Table(name = "Q_GROUP_POST")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupPost {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_post_generator")
    @SequenceGenerator(name = "group_post_generator", sequenceName = "group_post_seq", allocationSize = 1)
    @Column(name = "POST_ID")
    private Long postId;

    @Column(name = "GROUP_ID")
    private Long groupId;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
}
