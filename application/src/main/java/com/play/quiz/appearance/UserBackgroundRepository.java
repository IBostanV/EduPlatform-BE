package com.play.quiz.appearance;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserBackgroundRepository extends JpaRepository<UserBackground, Long> {

    /** Only the address part, for the appearance: reading it does not load the picture. */
    @Query("SELECT b.publicId FROM UserBackground b WHERE b.accountId = :accountId")
    Optional<String> findPublicId(Long accountId);

    Optional<UserBackground> findByPublicId(String publicId);
}
