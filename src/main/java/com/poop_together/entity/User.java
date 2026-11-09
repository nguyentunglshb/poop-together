package com.poop_together.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

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
@Table(name = "users")
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String username;

    @Column (name = "password_hash") 
    String passwordHash;

    @Column (name = "display_name")
    String displayName;

    @Column (name = "avatar_url")
    String avatarUrl;

    String timezone;

    @Column (name = "last_seen_at")
    LocalDate lastSeenAt;

    @Column (name = "is_active")
    Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column (name = "updated_at")
    LocalDateTime updatedAt;
    Set<String> roles;
}
