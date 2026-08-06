package com.play.quiz.iq;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IqResponseRepository extends JpaRepository<IqResponse, Long> {

    /** This test's answers so far, oldest first: the items and how they went, for the estimate. */
    @Query("SELECT r FROM IqResponse r JOIN FETCH r.item WHERE r.session.sessionId = :sessionId"
            + " ORDER BY r.responseId")
    List<IqResponse> findAnswers(Long sessionId);
}
