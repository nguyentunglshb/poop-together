package com.poop_together.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poop_together.entity.ConversationMember;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, String> {
    
}
