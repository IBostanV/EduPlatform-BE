package com.play.quiz.repository;

import com.play.quiz.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySourceAndDestination_GroupId(final String source, final Long destination);

    List<Message> findByDestination_GroupId(final Long destination);

    // One bulk statement instead of loading every message of the group just to remove it.
    @Modifying
    @Query("DELETE FROM Message m WHERE m.destination.groupId = :groupId")
    void deleteByDestinationGroupId(Long groupId);
}
