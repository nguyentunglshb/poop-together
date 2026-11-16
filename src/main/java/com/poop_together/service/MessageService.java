package com.poop_together.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.poop_together.dto.request.conversation.MessageHistoryRequestDto;
import com.poop_together.dto.request.conversation.SendMessageRequestDto;
import com.poop_together.dto.response.conversation.MessageCreatedEvent;
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
import com.poop_together.repository.UserRepository;

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
    UserRepository userRepository;
    MessageRepository messageRepository;
    FriendShipChecker friendShipChecker;
    MessageMapper messageMapper;
    ApplicationEventPublisher eventPublisher;

    @Transactional
    public MessageResponse send(String conversationId, SendMessageRequestDto sendMessageRequestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)).getId();

        Conversation conversation = accessService.getConversation(conversationId);
        accessService.assertActiveMember(conversationId, userId);
        validateContent(sendMessageRequestDto.getContent());
        ensureStillFriends(conversation, userId);

        // Client gửi lại cùng client_id (mạng chập chờn, bấm hai lần): trả lại tin cũ,
        // không tạo mới
        if (sendMessageRequestDto.getClientId() != null && !sendMessageRequestDto.getClientId().isBlank()) {
            Optional<Message> existing = messageRepository.findBySenderIdAndClientId(userId,
                    sendMessageRequestDto.getClientId());
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
                .content(sendMessageRequestDto.getContent().strip())
                .clientId(sendMessageRequestDto.getClientId() == null || sendMessageRequestDto.getClientId().isBlank()
                        ? null
                        : sendMessageRequestDto.getClientId())
                .build();

        Message savedMessage = messageRepository.saveAndFlush(message);

        MessageResponse response = messageMapper.toMessageResponse(savedMessage);
        eventPublisher.publishEvent(new MessageCreatedEvent(response));

        // Cập nhật tin cuối cùng để danh sách đoạn chat sắp xếp và hiển thị đúng
        Instant createdAt = savedMessage.getCreatedAt();
        if (conversation.getLastMessageAt() == null || !createdAt.isBefore(conversation.getLastMessageAt())) {
            conversation.setLastMessageAt(createdAt);
            conversation.setLastMessageId(savedMessage.getId());
            conversationRepository.save(conversation);
        }

        return messageMapper.toMessageResponse(savedMessage);
    }

    @Transactional
    public MessagePageResponse getHistory(String conversationId, MessageHistoryRequestDto messageHistoryRequestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)).getId();

        accessService.getConversation(conversationId);
        accessService.assertMember(conversationId, userId);

        int size = messageHistoryRequestDto.getLimit() <= 0 ? DEFAULT_LIMIT
                : Math.min(messageHistoryRequestDto.getLimit(), MAX_LIMIT);
        Pageable page = PageRequest.of(0, size + 1);

        List<Message> rows = (messageHistoryRequestDto.getBefore() == null)
                ? messageRepository.findByConversationIdOrderByCreatedAtDescIdDesc(conversationId, page)
                : messageRepository.findByConversationIdAndCreatedAtBeforeOrderByCreatedAtDescIdDesc(conversationId,
                        messageHistoryRequestDto.getBefore(), page);

        boolean hasMore = rows.size() > size;
        List<Message> pageRows = hasMore ? rows.subList(0, size) : rows;
        Instant nextBefore = hasMore ? pageRows.get(pageRows.size() - 1).getCreatedAt() : null;

        return new MessagePageResponse(
                pageRows.stream().map(messageMapper::toMessageResponse).toList(),
                hasMore,
                nextBefore);
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
