package com.poop_together.service;

import org.springframework.stereotype.Service;

import com.poop_together.entity.Conversation;
import com.poop_together.entity.ConversationMember;
import com.poop_together.exception.AppException;
import com.poop_together.exception.ErrorCode;
import com.poop_together.repository.ConversationMemberRepository;
import com.poop_together.repository.ConversationRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationAccessService {
    ConversationRepository conversationRepository;
    ConversationMemberRepository conversationMemberRepository;

    public Conversation getConversation(String conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));
    }

    public ConversationMember assertMember(String conversationId, String userId) {
        return conversationMemberRepository.findByConversationIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_CONVERSATION_MEMBER));
    }

    public ConversationMember assertActiveMember(String conversationId, String userId) {
        ConversationMember member = assertMember(conversationId, userId);
        if (member.getLeftAt() != null) {
            throw new AppException(ErrorCode.NOT_CONVERSATION_MEMBER);
        }
        return member;
    }
}
