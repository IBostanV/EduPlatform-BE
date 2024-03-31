package com.play.quiz.mapper;

import com.play.quiz.domain.Message;
import com.play.quiz.dto.MessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "participantId", source = "destination.participant.accountId")
    @Mapping(target = "participantUsername", source = "destination.participant.username")
    @Mapping(target = "destinationId", source = "destination.id")
    MessageDto toDto(Message message);

    @Mapping(target = "destination.participant.accountId", source = "participantId")
    @Mapping(target = "destination.participant.username", source = "participantUsername")
    @Mapping(target = "destination.id", source = "destinationId")
    Message toEntity(MessageDto messageDto);

    List<MessageDto> toDtoList(List<Message> messages);

    List<Message> toEntityList(List<MessageDto> messages);
}
