package com.play.quiz.announcement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    List<Announcement> findAllByOrderByAnnouncementIdDesc();

    // ponytail: only the last 30 days, so a new account is not greeted by every announcement ever
    // made; a player away for longer misses the older ones. Seed ANNOUNCEMENT_SEEN_ID at sign-up
    // if that matters.
    @Query(value = """
            SELECT * FROM Q_ANNOUNCEMENT
             WHERE ANNOUNCEMENT_ID > (SELECT ANNOUNCEMENT_SEEN_ID FROM Q_USER WHERE ACCOUNT_ID = :accountId)
               AND CREATED_DATE > SYSDATE - 30
             ORDER BY ANNOUNCEMENT_ID
            """, nativeQuery = true)
    List<Announcement> findUnseen(Long accountId);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE Q_USER SET ANNOUNCEMENT_SEEN_ID = GREATEST(ANNOUNCEMENT_SEEN_ID, :announcementId)
             WHERE ACCOUNT_ID = :accountId
            """, nativeQuery = true)
    void markSeen(Long accountId, Long announcementId);
}
