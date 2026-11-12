package com.poop_together.entity;

import java.time.LocalDate;

import org.hibernate.annotations.UpdateTimestamp;

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
@Table (name = "user_daily_stats")
public class UserDailyStats {

    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;

    @Column (name = "user_id", nullable = false)
    String userId;

    @Column (name = "stat_date", nullable = false)
    LocalDate statDate;

    @Column(name = "total_seconds", nullable = false)
    long totalSeconds = 0;

    @Column (name = "session_count", nullable = false)
    Long sessionCount;

    @UpdateTimestamp 
    @Column (name = "updated_at")
    LocalDate updatedAt;
}
