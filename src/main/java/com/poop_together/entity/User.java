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
    String password_hash;
    String display_name;
    String avatar_url;
    String timezone;
    LocalDate last_seen_at;
    Boolean is_active;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime created_at;

    @UpdateTimestamp
    LocalDateTime updated_at;
    Set<String> roles;
}
