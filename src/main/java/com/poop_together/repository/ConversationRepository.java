package com.poop_together.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poop_together.entity.Conversation;

@Repository 
public interface ConversationRepository extends JpaRepository<Conversation, String> {
    Optional<Conversation> findByDirectKey(String directKey);
}
