package com.poop_together.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table (name = "messages")
public class Message {
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;

    @Column (name = "conversation_id")
    String conversationId;

    @Column (name = "sender_id")
    String senderId;

    String content;
    
    @CreationTimestamp 
    @Column (name = "created_at", updatable = false) 
    LocalDate createdAt;
    
    @UpdateTimestamp 
    @Column (name = "deleted_at", updatable = false)
    LocalDate deletedAt;
}
