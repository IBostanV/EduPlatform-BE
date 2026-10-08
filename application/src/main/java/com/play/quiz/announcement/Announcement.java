package com.play.quiz.announcement;

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

/** Something an admin tells every player; each sees it once, then dismisses it. */
@Entity
@Table(name = "Q_ANNOUNCEMENT")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "announcement_generator")
    @SequenceGenerator(name = "announcement_generator", sequenceName = "announcement_seq", allocationSize = 1)
    @Column(name = "ANNOUNCEMENT_ID")
    private Long announcementId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
}
