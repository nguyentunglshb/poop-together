package com.poop_together.dto.response.conversation;

import java.time.Instant;

import com.poop_together.dto.response.UserSummaryResponse;

public record MemberResponse(
        UserSummaryResponse user,
        Instant joinedAt

) {

}
