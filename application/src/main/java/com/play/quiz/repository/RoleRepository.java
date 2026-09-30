package com.play.quiz.repository;

import com.play.quiz.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// The handful of rows in Q_ROLE. Roles are only ever read: granting one attaches an existing row
// to an account, and an account's roles must be managed instances or the join table write fails.
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
