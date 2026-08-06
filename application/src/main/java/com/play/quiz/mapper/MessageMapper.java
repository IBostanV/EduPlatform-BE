package com.play.quiz.mapper;

import com.play.quiz.domain.Message;
import com.play.quiz.dto.MessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "participantId", ignore = true)
    @Mapping(target = "participantUsername", ignore = true)
    @Mapping(target = "destinationId", source = "destination.groupId")
    @Mapping(target = "edited", expression = "java(message.getUpdatedDate() != null)")
    @Mapping(target = "event", ignore = true)
    MessageDto toDto(Message message);

    @Mapping(target = "destination.groupId", source = "destinationId")
    Message toEntity(MessageDto messageDto);

    List<MessageDto> toDtoList(List<Message> messages);

    List<Message> toEntityList(List<MessageDto> messages);
}
