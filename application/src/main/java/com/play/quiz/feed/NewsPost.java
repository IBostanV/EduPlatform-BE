package com.play.quiz.feed;

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
 * A post someone wrote on the News page, the only news that is written rather than found: an
 * admin's is a patch note for everyone, anyone else's a friend post for them and their friends.
 */
@Entity
@Table(name = "Q_NEWS")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsPost {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_generator")
    @SequenceGenerator(name = "news_generator", sequenceName = "news_seq", allocationSize = 1)
    @Column(name = "NEWS_ID")
    private Long newsId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "IS_PATCH")
    private boolean patch;
}
