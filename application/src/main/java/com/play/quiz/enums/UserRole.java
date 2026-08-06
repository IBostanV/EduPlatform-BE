package com.play.quiz.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
// The ids are Q_ROLE's, seeded by 20230721175423_add_user_roles. Every row there belongs here:
// roles are read back with UserRole.valueOf, so one this enum does not know throws on the login
// of anyone who holds it.
public enum UserRole {
    ROLE_ADMIN(1L),
    ROLE_USER(2L),
    ROLE_MODERATOR(3L),
    // The content dashboard: categories, glossaries, questions and the knowledge base.
    ROLE_CONTENT_EDITOR(4L),
    ROLE_CONTENT_PUBLISHER(5L);

    private final Long roleId;
}
