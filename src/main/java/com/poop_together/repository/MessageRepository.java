package com.poop_together.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.poop_together.entity.Message;

public interface MessageRepository extends JpaRepository<Message, String> {
    Optional<Message> findBySenderIdAndClientId(String senderId, String clientId);

    List<Message> findByConversationIdOrderByCreatedAtDescIdDesc(String conversationId, Pageable pageable);

    List<Message> findByConversationIdAndCreatedAtBeforeOrderByCreatedAtDescIdDesc(String conversationId,
            Instant before,
            Pageable pageable);
}
