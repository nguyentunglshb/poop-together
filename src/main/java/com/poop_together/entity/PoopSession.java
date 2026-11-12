package com.poop_together.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.poop_together.enums.SessionStatus;
import com.poop_together.enums.StopReason;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
@Table (name = "poop_sessions")
public class PoopSession {
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;
    
    @Column (name = "user_id")
    String userId;

    @CreationTimestamp 
    @Column (name = "started_at")
    Instant startedAt;

    @UpdateTimestamp 
    @Column (name = "updated_at")
    Instant updatedAt;

    @Column (name = "duration_seconds")
    Integer durationSeconds;
    
    @Enumerated (EnumType.STRING)
    SessionStatus status;

    @Enumerated (EnumType.STRING)
    @Column (name = "stop_reason")
    StopReason stopReason;

    String note;

    @Version 
    Long version;
}
