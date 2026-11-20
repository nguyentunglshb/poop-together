package com.poop_together.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.poop_together.dto.response.conversation.MessageCreatedEvent;
import com.poop_together.dto.response.conversation.MessageResponse;
import com.poop_together.entity.ConversationMember;
import com.poop_together.repository.ConversationMemberRepository;
import com.poop_together.service.RealtimeEventPublisher;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatEventListener {
    ConversationMemberRepository memberRepository;
    RealtimeEventPublisher publisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMessageCreated(MessageCreatedEvent event) {
        MessageResponse message = event.message();

        // Gửi cho mọi thành viên còn trong hội thoại, KỂ CẢ người gửi
        // (để đồng bộ các tab/thiết bị khác của họ; client chống hiện đôi bằng
        // client_id)
        memberRepository.findByConversationId(message.conversationId()).stream()
                .filter(m -> m.getLeftAt() == null)
                .map(ConversationMember::getUserId)
                .forEach(userId -> publisher.sendToUser(userId, "message_new", message));
    }
}
