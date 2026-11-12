package com.poop_together.service;

import com.poop_together.repository.FriendShipRepository;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.poop_together.dto.request.conversation.DirectConversationRequestDto;
import com.poop_together.dto.response.conversation.ConversationResponse;
import com.poop_together.entity.Conversation;
import com.poop_together.enums.FriendShipStatus;
import com.poop_together.exception.AppException;
import com.poop_together.exception.ErrorCode;
import com.poop_together.mapper.ConversationMapper;
import com.poop_together.repository.ConversationRepository;
import com.poop_together.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service 
@RequiredArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    private final FriendShipRepository friendShipRepository;
    ConversationRepository conversationRepository;
    UserRepository userRepository;
    ConversationMapper conversationMapper;
    DirectConversationCreatorService directConversationCreatorService;

    public ConversationResponse getOrCreteDirect(DirectConversationRequestDto directConversationRequestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String requesterId = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();
        
        if(directConversationRequestDto.getFriendId().equals(requesterId)) {
            throw new AppException(ErrorCode.CONVERSATION_WITH_SELF);
        }

        if(!areFriends(requesterId, directConversationRequestDto.getFriendId())) {
            throw new AppException(ErrorCode.NOT_FRIENDS);
        }

        String directKey = buildDirectKey(requesterId, directConversationRequestDto.getFriendId());

        Conversation conversation = conversationRepository.findByDirectKey(directKey)
            .orElseGet(() -> createOrLoad(requesterId, directConversationRequestDto.getFriendId(), directKey));
    
        return conversationMapper.toConversationResponse(conversation);
    }

    public List<ConversationResponse> getConversations() {
        return conversationMapper.toConversationResponses(conversationRepository.findAll());
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

}
