package com.poop_together.entity;

import java.time.LocalDate;

import com.poop_together.enums.ConversationType;

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
public class Conversations {
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;

    @Enumerated (EnumType.STRING)
    ConversationType type;

    LocalDate created_at;
}
