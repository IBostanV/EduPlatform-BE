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

/** A member's comment under a group post. */
@Entity
@Table(name = "Q_GROUP_POST_COMMENT")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupPostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_post_comment_generator")
    @SequenceGenerator(name = "group_post_comment_generator", sequenceName = "group_post_comment_seq", allocationSize = 1)
    @Column(name = "COMMENT_ID")
    private Long commentId;

    @Column(name = "POST_ID")
    private Long postId;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
}
