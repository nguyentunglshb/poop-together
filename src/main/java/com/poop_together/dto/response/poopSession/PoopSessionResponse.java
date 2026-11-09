package com.poop_together.dto.response.poopSession;

import java.time.Instant;

import com.poop_together.enums.SessionStatus;
import com.poop_together.enums.StopReason;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE)
public class PoopSessionResponse {
    String id;
    Instant startedAt;
    Instant updatedAt;
    Integer durationSeconds;
    SessionStatus status;
    StopReason stopReason;
    String note;
}
