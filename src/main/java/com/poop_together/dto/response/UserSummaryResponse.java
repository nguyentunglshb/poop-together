package com.poop_together.dto.response;

import com.poop_together.entity.User;

public record UserSummaryResponse(
        String id,
        String username,
        String displayName,
        String avatarUrl) {

    public static UserSummaryResponse from(User user) {
        if (user == null) {
            return null;
        }
        return new UserSummaryResponse(user.getId(), user.getUsername(), user.getDisplayName(), user.getAvatarUrl());
    }

}
