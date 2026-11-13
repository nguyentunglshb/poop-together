package com.poop_together.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poop_together.entity.ConversationMember;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, String> {
    List<ConversationMember> findByUserIdAndLeftAtIsNull(String userId);

    List<ConversationMember> findByConversationIdInAndUserIdNot(Collection<String> conversationIds, String userId);
}
