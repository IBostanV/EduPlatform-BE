package com.play.quiz.mapper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.MessageGroup;
import com.play.quiz.domain.UserGroup;
import com.play.quiz.dto.UserGroupDto;
import org.junit.jupiter.api.Test;

/** What the chat list carries for each group: its name, its members and its picture. */
class UserGroupMapperTest {

    private final UserGroupMapper mapper = new UserGroupMapperImpl();

    @Test
    void given_a_group_with_a_picture_when_mapped_then_carry_it_to_the_chat_list() {
        UserGroup membership = UserGroup.builder()
                .groupId(1L)
                .participant(Account.builder().accountId(2L).username("Ion").build())
                .messageGroup(MessageGroup.builder()
                        .groupId(1L)
                        .name("Admin Group")
                        .photo(new byte[]{1, 2, 3})
                        .photoType("image/jpeg")
                        .build())
                .build();

        UserGroupDto result = mapper.toDto(membership);

        assertEquals("Admin Group", result.getName());
        assertEquals("Ion", result.getParticipantUsername());
        assertArrayEquals(new byte[]{1, 2, 3}, result.getPhoto());
    }

    @Test
    void given_a_group_with_no_picture_when_mapped_then_leave_it_empty_for_the_initials() {
        UserGroup membership = UserGroup.builder()
                .groupId(2L)
                .messageGroup(MessageGroup.builder().groupId(2L).name("Test Group").build())
                .build();

        assertNull(mapper.toDto(membership).getPhoto());
    }
}
