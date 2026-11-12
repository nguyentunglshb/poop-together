package com.poop_together.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@FieldDefaults (level = AccessLevel.PRIVATE)
@Entity 
@Builder 
@Table (name = "conversation_members")
public class ConversationMember {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;

    @Column (name = "conversation_id")
    String conversationId;

    @Column (name = "user_id")
    String userId;

    @CreationTimestamp 
    @Column (name = "joined_at", updatable = true)
    Instant joinedAt;

    @Column (name = "left_at")
    Instant leftAt;

    @Column (name = "last_read_at")
    Instant lastReadAt;
    
    
}
