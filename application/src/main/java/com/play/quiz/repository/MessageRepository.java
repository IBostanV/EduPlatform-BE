package com.play.quiz.repository;

import com.play.quiz.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySourceAndDestination_GroupId(final String source, final Long destination);

    List<Message> findByDestination_Id(final Long destination);
}
