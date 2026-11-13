package com.poop_together.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.poop_together.dto.request.conversation.DirectConversationRequestDto;
import com.poop_together.dto.response.conversation.ConversationResponse;
import com.poop_together.entity.Conversation;
import com.poop_together.entity.ConversationMember;
import com.poop_together.entity.User;
import com.poop_together.enums.ConversationType;
import com.poop_together.enums.FriendShipStatus;
import com.poop_together.exception.AppException;
import com.poop_together.exception.ErrorCode;
import com.poop_together.mapper.ConversationMapper;
import com.poop_together.repository.ConversationMemberRepository;
import com.poop_together.repository.ConversationRepository;
import com.poop_together.repository.FriendShipRepository;
import com.poop_together.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    private final FriendShipRepository friendShipRepository;
    ConversationRepository conversationRepository;
    UserRepository userRepository;
    ConversationMapper conversationMapper;
    ConversationMemberRepository conversationMemberRepository;
    DirectConversationCreatorService directConversationCreatorService;

    public ConversationResponse getOrCreteDirect(DirectConversationRequestDto directConversationRequestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String requesterId = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();

        if (directConversationRequestDto.getFriendId().equals(requesterId)) {
            throw new AppException(ErrorCode.CONVERSATION_WITH_SELF);
        }

        User friend = userRepository.findById(directConversationRequestDto.getFriendId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!areFriends(requesterId, directConversationRequestDto.getFriendId())) {
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
        return conversations.stream()
                .sorted()
                .map(c -> ConversationResponse.of(c, peerByConversationId.get(c.getId()), null, 0))
                .toList();
    }

    public ConversationResponse getConversation(String conversationId) {
        return conversationMapper
                .toConversationResponse(
                        conversationRepository.findById((conversationId))
                                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND)));
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

    private boolean areFriends(String a, String b) {
        return friendShipRepository.existsByRequesterIdAndAddresseeIdAndStatus(b, a, FriendShipStatus.ACCEPTED)
                || friendShipRepository.existsByRequesterIdAndAddresseeIdAndStatus(a, b, FriendShipStatus.ACCEPTED);
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

}
