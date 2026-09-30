package com.play.quiz.repository;

import java.util.List;

import com.play.quiz.domain.QuizInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizInviteRepository extends JpaRepository<QuizInvite, Long> {

    // Newest first: the invitations page lists the latest on top.
    @Query("SELECT qi FROM QuizInvite qi WHERE qi.account.email = :username ORDER BY qi.createdDate DESC")
    List<QuizInvite> findByInvitedEmail(String username);

    boolean existsByQuiz_QuizIdAndAccount_Email(Long quizId, String email);

    long countByQuiz_QuizId(Long quizId);

    void deleteByQuiz_QuizId(Long quizId);
}
