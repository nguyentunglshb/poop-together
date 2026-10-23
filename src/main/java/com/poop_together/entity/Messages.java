package com.poop_together.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
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
public class Messages {
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;

    String conversation_id;
    String sender_id;
    String content;
    LocalDate created_at;
    LocalDate deleted_at;
}
