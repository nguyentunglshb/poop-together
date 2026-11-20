package com.poop_together.dto.response.conversation;

import java.time.Instant;

public record LastMessageResponse(String id, String senderId, String type, String contentPreview, Instant createdAt) {

}
