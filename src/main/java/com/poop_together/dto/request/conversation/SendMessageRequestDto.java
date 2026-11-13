package com.poop_together.dto.request.conversation;

public record SendMessageRequestDto(
        String content,
        String clientId) {

}
