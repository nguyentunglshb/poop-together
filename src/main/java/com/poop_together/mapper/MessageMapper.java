package com.poop_together.mapper;

import org.mapstruct.Mapper;

import com.poop_together.dto.response.conversation.MessageResponse;
import com.poop_together.entity.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageResponse toMessageResponse(Message m);

}
