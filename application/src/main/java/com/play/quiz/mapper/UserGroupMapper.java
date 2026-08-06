package com.play.quiz.mapper;

import com.play.quiz.domain.UserGroup;
import com.play.quiz.dto.UserGroupDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserGroupMapper {

    @Mapping(target = "participantId", source = "participant.accountId")
    @Mapping(target = "participantUsername", source = "participant.username")
    @Mapping(target = "name", source = "messageGroup.name")
    @Mapping(target = "photo", source = "messageGroup.photo")
    UserGroupDto toDto(UserGroup userGroup);

    @Mapping(target = "participant.accountId", source = "participantId")
    @Mapping(target = "participant.username", source = "participantUsername")
    UserGroup toEntity(UserGroupDto userGroupDto);

    Set<UserGroupDto> toDtoSet(Set<UserGroup> userGroupSet);
}
