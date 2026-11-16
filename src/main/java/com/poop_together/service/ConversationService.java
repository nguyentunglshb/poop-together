package com.poop_together.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.poop_together.dto.request.conversation.DirectConversationRequestDto;
import com.poop_together.dto.response.conversation.ConversationResponse;
import com.poop_together.dto.response.conversation.LastMessageResponse;
import com.poop_together.entity.Conversation;
import com.poop_together.entity.ConversationMember;
import com.poop_together.entity.Message;
import com.poop_together.entity.User;
import com.poop_together.enums.ConversationType;
import com.poop_together.exception.AppException;
import com.poop_together.exception.ErrorCode;
import com.poop_together.mapper.ConversationMapper;
import com.poop_together.repository.ConversationMemberRepository;
import com.poop_together.repository.ConversationRepository;
import com.poop_together.repository.MessageRepository;
import com.poop_together.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    FriendShipChecker friendShipChecker;
    ConversationRepository conversationRepository;
    UserRepository userRepository;
    ConversationMapper conversationMapper;
    ConversationMemberRepository conversationMemberRepository;
    DirectConversationCreatorService directConversationCreatorService;
    ReadStateService readStateService;
    MessageRepository messageRepository;
    ConversationAccessService accessService;

    public ConversationResponse getOrCreteDirect(DirectConversationRequestDto directConversationRequestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String requesterId = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();

        if (directConversationRequestDto.getFriendId().equals(requesterId)) {
            throw new AppException(ErrorCode.CONVERSATION_WITH_SELF);
        }

        User friend = userRepository.findById(directConversationRequestDto.getFriendId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!friendShipChecker.areFriends(requesterId, directConversationRequestDto.getFriendId())) {
            throw new AppException(ErrorCode.NOT_FRIENDS);
        }

        String directKey = buildDirectKey(requesterId, directConversationRequestDto.getFriendId());

        Conversation conversation = conversationRepository.findByDirectKey(directKey)
                .orElseGet(() -> createOrLoad(requesterId, directConversationRequestDto.getFriendId(), directKey));

        return ConversationResponse.fromDirect(conversation, friend);
    }

    public List<ConversationResponse> getMyConversations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();

        List<ConversationMember> memberships = conversationMemberRepository.findByUserIdAndLeftAtIsNull(userId);

        if (memberships.isEmpty()) {
            return List.of();
        }

        List<String> conversationIds = memberships.stream().map(ConversationMember::getConversationId).toList();

        List<Conversation> conversations = conversationRepository.findAllById((conversationIds));

        List<String> directIds = conversations.stream().filter(c -> c.getType().equals(ConversationType.DIRECT))
                .map(Conversation::getId)
                .toList();

        Map<String, User> peerByConversationId = loadPeers(directIds, userId);

        Map<String, Long> unreadByConversation = readStateService.countUnreadByConversation(memberships, userId);

        Map<String, Message> lastMessages = loadLastMessages(conversations);

        return conversations.stream()
                .sorted()
                .map(c -> ConversationResponse.of(c, peerByConversationId.get(c.getId()),
                        toLastMessage(lastMessages.get(c.getId())),
                        unreadByConversation.getOrDefault(c.getId(), 0L)))
                .toList();
    }

    public ConversationResponse getConversation(String conversationId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();

        Conversation conversation = accessService.getConversation(conversationId);
        ConversationMember me = accessService.assertMember(conversationId, userId);

        List<ConversationMember> allMembers = conversationMemberRepository.findByConversationId(conversationId);

        Set<String> userIds = allMembers.stream()
                .map(ConversationMember::getUserId)
                .collect(Collectors.toSet());
        Map<String, User> usersById = userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        User peer = null;
        if (conversation.getType() == ConversationType.DIRECT) {
            peer = allMembers.stream()
                    .map(ConversationMember::getUserId)
                    .filter(id -> !id.equals(userId))
                    .findFirst()
                    .map(usersById::get)
                    .orElse(null);
        }

        long unreadCount = readStateService.countUnread(me, userId);

        // Danh sách thành viên hiển thị: chỉ những người chưa rời
        List<ConversationMember> members = allMembers.stream()
                .filter(m -> m.getLeftAt() == null)
                .filter(m -> usersById.containsKey(m.getUserId()))
                .map(m -> ConversationMember.builder()
                        .id(m.getId())
                        .userId(m.getUserId())
                        .joinedAt(m.getJoinedAt())
                        .leftAt(m.getLeftAt())
                        .lastReadAt(m.getLastReadAt())
                        .build())
                .toList();

        // return conversationMapper
        // .toConversationResponse(
        // conversationRepository.findById((conversationId))
        // .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND)));

        return ConversationResponse.withMembers(conversation, peer, null, unreadCount, members);
    }

    private Conversation createOrLoad(String currentUserId, String friendId, String directKey) {
        try {
            return directConversationCreatorService.create(currentUserId, friendId, directKey);
        } catch (DataIntegrityViolationException e) {
            // Người kia vừa tạo cùng lúc: lấy lại cuộc hội thoại của họ
            return conversationRepository.findByDirectKey(directKey)
                    .orElseThrow(() -> e);
        }
    }

    static String buildDirectKey(String a, String b) {
        return a.compareTo(b) < 0 ? a + "_" + b : b + "_" + a;
    }

    private Map<String, User> loadPeers(List<String> directConversationIds, String currentUserId) {
        if (directConversationIds.isEmpty()) {
            return Map.of();
        }

        List<ConversationMember> others = conversationMemberRepository
                .findByConversationIdInAndUserIdNot(directConversationIds, currentUserId);

        Set<String> peerUserIds = others.stream()
                .map(ConversationMember::getUserId)
                .collect(Collectors.toSet());

        Map<String, User> usersById = userRepository.findAllById(peerUserIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        Map<String, User> result = new HashMap<>();
        for (ConversationMember m : others) {
            User user = usersById.get(m.getUserId());
            if (user != null) {
                result.put(m.getConversationId(), user);
            }
        }
        return result;
    }

    private Map<String, Message> loadLastMessages(List<Conversation> conversations) {
        List<String> ids = conversations.stream()
                .map(Conversation::getLastMessageId)
                .filter(Objects::nonNull)
                .toList();
        if (ids.isEmpty()) {
            return Map.of();
        }
        return messageRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Message::getConversationId, Function.identity()));
    }

    private LastMessageResponse toLastMessage(Message m) {
        if (m == null) {
            return null;
        }
        return new LastMessageResponse(
                m.getId(), m.getSenderId(), m.getType().name(), preview(m.getContent()), m.getCreatedAt());
    }

    static String preview(String content) {
        if (content == null) {
            return null;
        }
        int max = 80;
        if (content.codePointCount(0, content.length()) <= max) {
            return content;
        }
        return content.substring(0, content.offsetByCodePoints(0, max)) + "…";
    }

}
