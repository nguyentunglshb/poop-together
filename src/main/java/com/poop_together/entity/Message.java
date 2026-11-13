package com.poop_together.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.poop_together.enums.MessageType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "messages", uniqueConstraints = @UniqueConstraint(name = "uq_messages_sender_client", columnNames = {
        "sender_id",
        "client_id" }), indexes = @Index(name = "idx_messages_conv_created", columnList = "conversation_id, created_at"))
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "conversation_id")
    String conversationId;

    @Column(name = "sender_id")
    String senderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type = MessageType.TEXT;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "client_id", length = 64)
    private String clientId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
