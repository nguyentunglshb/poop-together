package com.poop_together.entity;

import java.time.LocalDate;

import com.poop_together.enums.SessionStatus;
import com.poop_together.enums.StopReason;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class PoopSessions {
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;
    
    String user_id;
    LocalDate started_at;
    LocalDate ended_at;
    Integer duration_seconds;
    
    @Enumerated (EnumType.STRING)
    SessionStatus status;

    @Enumerated (EnumType.STRING)
    StopReason stop_reason;

    String note;
    LocalDate created_at;
}
