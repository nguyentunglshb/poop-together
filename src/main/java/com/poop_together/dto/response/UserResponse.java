package com.poop_together.dto.response;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
@FieldDefaults (level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String displayName;
    String avatarUrl;
    String timezone;
    Instant lastSeenAt;
    Boolean isActive;
    Instant createdAt;
    Instant updatedAt;
}
