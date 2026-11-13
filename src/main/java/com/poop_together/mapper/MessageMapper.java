package com.poop_together.mapper;

import com.poop_together.dto.response.conversation.MessageResponse;
import com.poop_together.entity.Message;

public interface MessageMapper {
    MessageResponse toMessageResponse(Message m);

}
