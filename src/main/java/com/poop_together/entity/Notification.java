package com.poop_together.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import com.poop_together.enums.NotificationType;

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
@Table (name = "notifications")
public class Notification {
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;

    @Column (name = "user_id")
    String userId;
    
    @Enumerated (EnumType.STRING)
    NotificationType type;
    
    @Column (name = "actor_id")
    String actorId;

    String payload;

    @Column (name = "is_read")
    Boolean isRead;

    @CreationTimestamp 
    @Column (name = "created_at")
    LocalDate createdAt;
}
