package com.poop_together.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

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
@Table (name = "refresh_tokens")
public class RefreshToken {
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;

    @Column (name = "user_id")
    String userId;

    @Column (name = "token_hash")
    String tokenHash;

    @Column (name = "expires_at")
    LocalDate expiresAt;

    @Column (name = "revoked_at")
    LocalDate revokedAt;

    @CreationTimestamp 
    @Column (name = "created_at")
    LocalDate createdAt;
}
