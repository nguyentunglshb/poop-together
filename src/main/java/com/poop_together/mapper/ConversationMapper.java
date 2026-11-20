package com.poop_together.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.poop_together.dto.request.conversation.DirectConversationRequestDto;
import com.poop_together.dto.response.conversation.ConversationResponse;
import com.poop_together.entity.Conversation;

@Mapper (componentModel = "spring")
public interface ConversationMapper {
    Conversation toConversation(DirectConversationRequestDto req);

    ConversationResponse toConversationResponse(Conversation conversation);

    List<ConversationResponse> toConversationResponses(List<Conversation> conversations);
}
