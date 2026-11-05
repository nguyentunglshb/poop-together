package com.poop_together.dto.response.friendShip;

import java.time.LocalDate;

import com.poop_together.enums.FriendShipStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
@FieldDefaults (level = AccessLevel.PRIVATE)
public class FriendShipResponse {
    String id;
    String requester_id;
    String addressee_id;
    LocalDate created_at;
    LocalDate responded_at;
    FriendShipStatus status;
}
