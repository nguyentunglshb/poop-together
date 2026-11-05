package com.poop_together.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.poop_together.enums.FriendShipStatus;

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
@Table (name = "friendships")
public class FriendShip {

    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;
    
    String requester_id;
    String addressee_id;

    @Enumerated (EnumType.STRING)
    FriendShipStatus status;
    
    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime created_at;

    @UpdateTimestamp
    LocalDateTime responded_at;
}
