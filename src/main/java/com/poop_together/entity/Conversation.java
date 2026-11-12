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
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE)
@Entity 
@Table (name = "conversations")
public class Conversation {
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;

    @Enumerated (EnumType.STRING)
    ConversationType type;

    @CreationTimestamp 
    @Column (name = "created_at", updatable = false)
    Instant createdAt;

    String name;

    @Column (name = "avatar_url")
    String avatarUrl;

    @Column (name = "created_by")
    String createdBy;

    @Column (name = "last_message_at")
    Instant lastMessageAt;

    @UpdateTimestamp 
    @Column (name = "updated_at")
    Instant updatedAt;
}
