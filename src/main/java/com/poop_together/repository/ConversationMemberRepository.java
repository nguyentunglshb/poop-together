package com.poop_together.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poop_together.entity.ConversationMember;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, String> {
    List<ConversationMember> findByUserIdAndLeftAtIsNull(String userId);

    List<ConversationMember> findByConversationIdInAndUserIdNot(Collection<String> conversationIds, String userId);

    Optional<ConversationMember> findByConversationIdAndUserId(String conversationId, String userId);

    List<ConversationMember> findByConversationId(String conversationId);
}
