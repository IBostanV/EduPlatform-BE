package com.play.quiz.repository;

import java.util.List;

import com.play.quiz.domain.ClientError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientErrorRepository extends JpaRepository<ClientError, Long> {

    // The admin tab shows the newest; older ones are still counted and cleared with the rest.
    List<ClientError> findTop200ByOrderByCreatedDateDesc();
}
