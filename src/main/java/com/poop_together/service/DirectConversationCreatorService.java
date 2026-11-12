package com.poop_together.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poop_together.entity.Conversation;
import com.poop_together.entity.ConversationMember;
import com.poop_together.enums.ConversationType;
import com.poop_together.repository.ConversationMemberRepository;
import com.poop_together.repository.ConversationRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service 
@RequiredArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class DirectConversationCreatorService {
    ConversationRepository conversationRepository;
    ConversationMemberRepository conversationMemberRepository;

    @Transactional 
    public Conversation create(String creatorId, String friendId, String directKey) {
        Conversation conversation = Conversation
            .builder()
            .type(ConversationType.DIRECT)
            .directKey(directKey)
            .createdBy(creatorId)
            .build();

        conversationRepository.saveAndFlush(conversation);

        conversationMemberRepository.saveAll(List.of(
            newMember(conversation.getId(), creatorId),
            newMember(conversation.getId(), friendId)
        ));

        return conversation;
    }

    private ConversationMember newMember(String conversationId, String userId) {
        ConversationMember member = ConversationMember
            .builder()
            .conversationId(conversationId)
            .userId(userId)
            .build();
        
        return member;
    }
}
