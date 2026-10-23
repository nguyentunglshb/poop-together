package com.poop_together.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    String display_name;
    String avatar_url;
    String timezone;
    LocalDate last_seen_at;
    Boolean is_active;
    LocalDateTime created_at;
    LocalDateTime updated_at;
}
