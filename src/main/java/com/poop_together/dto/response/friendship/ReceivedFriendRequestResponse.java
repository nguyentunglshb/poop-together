package com.poop_together.dto.response.friendShip;

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
public class ReceivedFriendRequestResponse {
    String id;
    FriendShipUserResponse requester;
    String created_at;
    String responded_at;

    FriendShipStatus status;
}
