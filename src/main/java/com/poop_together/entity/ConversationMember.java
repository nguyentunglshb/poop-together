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
@Table (name = "conversation_members")
public class ConversationMember {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    String id;

    String conversation_id;
    String user_id;
    String last_read_message_id;
    LocalDate joined_at;
}
