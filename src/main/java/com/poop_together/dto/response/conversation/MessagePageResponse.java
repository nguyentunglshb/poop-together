package com.poop_together.dto.response.conversation;

import java.time.Instant;
import java.util.List;

public record MessagePageResponse(
        List<MessageResponse> items,
        boolean hasMore,
        Instant nextBefore) {

}
