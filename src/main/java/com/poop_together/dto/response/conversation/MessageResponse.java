package com.poop_together.dto.response.conversation;

import java.time.Instant;

import com.poop_together.enums.MessageType;

public record MessageResponse(
        String id,
        String conversationId,
        String senderId,
        MessageType type,
        String content,
        String clientId,
        Instant createdAt) {

}
