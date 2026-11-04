package com.poop_together.entity;

import java.time.LocalDate;

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

    String user_id;
    LocalDate stat_date;
    Number total_second;
    Number session_count;
    LocalDate updated_at;
}
