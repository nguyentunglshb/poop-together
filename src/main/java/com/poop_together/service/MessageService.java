package com.poop_together.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.poop_together.dto.response.conversation.MessagePageResponse;
import com.poop_together.dto.response.conversation.MessageResponse;
import com.poop_together.entity.Conversation;
import com.poop_together.entity.ConversationMember;
import com.poop_together.entity.Message;
import com.poop_together.enums.ConversationType;
import com.poop_together.enums.MessageType;
import com.poop_together.exception.AppException;
import com.poop_together.exception.ErrorCode;
import com.poop_together.mapper.MessageMapper;
import com.poop_together.repository.ConversationMemberRepository;
import com.poop_together.repository.ConversationRepository;
import com.poop_together.repository.MessageRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {
    static int MAX_CONTENT_LENGTH = 2000;
    static int DEFAULT_LIMIT = 30;
    static int MAX_LIMIT = 100;

    ConversationAccessService accessService;
    ConversationRepository conversationRepository;
    ConversationMemberRepository memberRepository;
    MessageRepository messageRepository;
    FriendShipChecker friendShipChecker;
    MessageMapper messageMapper;

    @Transactional
    public MessageResponse send(String conversationId, String userId, String content, String clientId) {
        Conversation conversation = accessService.getConversation(conversationId);
        accessService.assertActiveMember(conversationId, userId);
        validateContent(content);
        ensureStillFriends(conversation, userId);

        // Client gửi lại cùng client_id (mạng chập chờn, bấm hai lần): trả lại tin cũ,
        // không tạo mới
        if (clientId != null && !clientId.isBlank()) {
            Optional<Message> existing = messageRepository.findBySenderIdAndClientId(userId, clientId);
            if (existing.isPresent()) {
                if (!existing.get().getConversationId().equals(conversationId)) {
                    throw new AppException(ErrorCode.MESSAGE_INVALID);
                }
                return messageMapper.toMessageResponse(existing.get());
            }
        }

        Message message = Message
                .builder()
                .conversationId(conversationId)
                .senderId(userId)
                .type(MessageType.TEXT)
                .content(content.strip())
                .clientId(clientId == null || clientId.isBlank() ? null : clientId)
                .build();

        messageRepository.save(message);

        // Cập nhật tin cuối cùng để danh sách đoạn chat sắp xếp và hiển thị đúng
        if (conversation.getLastMessageAt() == null) {
            conversation.setLastMessageAt(message.getCreatedAt());
            conversation.setLastMessageId(message.getId());
            conversationRepository.save(conversation);
        }

        return messageMapper.toMessageResponse(message);
    }

    @Transactional
    public MessagePageResponse getHistory(String conversationId, String userId, Instant before, int limit) {
        accessService.getConversation(conversationId);
        accessService.assertMember(conversationId, userId);

        int size = limit <= 0 ? DEFAULT_LIMIT : Math.min(limit, MAX_LIMIT);
        Pageable page = PageRequest.of(0, size + 1);

    }

    private void validateContent(String content) {
        if (content == null || content.isBlank() || content.strip().length() > MAX_CONTENT_LENGTH) {
            throw new AppException(ErrorCode.MESSAGE_INVALID);
        }
    }

    private void ensureStillFriends(Conversation conversation, String userId) {
        if (conversation.getType() != ConversationType.DIRECT) {
            return;
        }
        String peerId = memberRepository.findByConversationId(conversation.getId())
                .stream()
                .map(ConversationMember::getUserId)
                .filter(id -> !id.equals(userId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_MEMBER_NOT_FOUND));

        if (!friendShipChecker.areFriends(userId, peerId)) {
            throw new AppException(ErrorCode.NOT_FRIENDS);
        }
    }
}
