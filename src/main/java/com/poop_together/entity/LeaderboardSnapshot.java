package com.poop_together.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.hibernate.annotations.CreationTimestamp;

import com.poop_together.enums.LeaderboardPeriod;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
@Table (name = "leaderboard_snapshots")
public class LeaderboardSnapshot {
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;

    @Enumerated (EnumType.STRING)
    LeaderboardPeriod period;
    
    @Column (name = "period_start", nullable = false, updatable = false)
    Instant periodStart;

    @Column (name = "user_id")
    String userId;

    Number rank;
    
    @Column(name = "total_seconds") 
    Number totalSeconds;

    @CreationTimestamp 
    @Column (name = "created_at", updatable = false)
    Instant createdAt;

    @PrePersist
    void initializePeriodStart() {
        periodStart = Instant.now().atZone(ZoneId.of("Asia/Bangkok"))
                .truncatedTo(ChronoUnit.DAYS)
                .toInstant();
    }
}
