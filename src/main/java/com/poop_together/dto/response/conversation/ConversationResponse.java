package com.poop_together.dto.response.conversation;

import java.time.Instant;

import com.poop_together.enums.ConversationType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE)
public class ConversationResponse {
    String id;
    ConversationType type;
    Instant createdAt;
    String name;
    String avatarUrl;
    String createdBy;
    Instant lastMessageAt;
    Instant updatedAt;

}
