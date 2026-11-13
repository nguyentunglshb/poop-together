package com.poop_together.dto.response.conversation;

import java.time.Instant;

import com.poop_together.dto.response.UserSummaryResponse;
import com.poop_together.entity.Conversation;
import com.poop_together.entity.User;
import com.poop_together.enums.ConversationType;

public record ConversationResponse(
        String id,
        ConversationType type,
        String name, // chỉ GROUP (DIRECT: null)
        String avatarUrl, // chỉ GROUP (DIRECT: null)
        UserSummaryResponse peer, // chỉ DIRECT (GROUP: null)
        LastMessageResponse lastMessage, // null cho tới branch chat-messages
        long unreadCount, // 0 cho tới branch chat-read-state
        Instant lastMessageAt,
        Instant createdAt) {

    public static ConversationResponse of(
            Conversation c, User peer, LastMessageResponse lastMessage, long unreadCount) {
        return new ConversationResponse(c.getId(), c.getType(), c.getName(), c.getAvatarUrl(),
                UserSummaryResponse.from(peer), lastMessage, unreadCount, c.getLastMessageAt(), c.getCreatedAt());
    }

    public static ConversationResponse fromDirect(Conversation c, User peer) {
        return of(c, peer, null, 0);
    }
}
