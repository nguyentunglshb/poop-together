package com.poop_together.dto.request.poopSession;

import java.time.Instant;

import com.poop_together.enums.SessionStatus;
import com.poop_together.enums.StopReason;

public class StartSessionRequest {
    String id;
    String userId;
    Instant startedAt;
    Instant updatedAt;
    Integer durationSeconds;
    SessionStatus status;
    StopReason stopReason;
    String note;
}
