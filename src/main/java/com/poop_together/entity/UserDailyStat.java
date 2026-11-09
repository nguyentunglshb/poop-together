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
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE)
@Entity 
@Table (name = "user_daily_stats")
public class UserDailyStat {

    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;

    @Column (name = "user_id")
    String userId;

    @Column (name = "stat_date")
    LocalDate statDate;

    @Column (name = "total_seconds")
    Number totalSeconds;

    @Column (name = "session_count")
    Number sessionCount;

    @UpdateTimestamp 
    @Column (name = "updated_at")
    LocalDate updatedAt;
}
