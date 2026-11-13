package com.poop_together.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.poop_together.enums.ConversationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity
@Builder
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Enumerated(EnumType.STRING)
    ConversationType type;

    @Column(name = "direct_key", length = 80, unique = true)
    String directKey; // only DIRECT

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    String name; // only GROUP

    @Column(name = "avatar_url")
    String avatarUrl; // only GROUP

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "last_message_at")
    Instant lastMessageAt;

    @Column(name = "last_message_id")
    String lastMessageId;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Instant updatedAt;
}
