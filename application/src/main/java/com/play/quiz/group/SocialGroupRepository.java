package com.play.quiz.group;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialGroupRepository extends JpaRepository<SocialGroup, Long> {

    List<SocialGroup> findAllByOrderByCreatedDateDesc();

    List<SocialGroup> findByOwnerId(Long ownerId);
}
