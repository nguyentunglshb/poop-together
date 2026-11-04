package com.poop_together.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.poop_together.dto.request.friendship.SendFriendRequestDto;
import com.poop_together.dto.response.friendship.FriendShipResponse;
import com.poop_together.entity.FriendShip;

@Mapper (componentModel = "spring")
public interface FriendShipMapper {
    FriendShip toFriendShip(SendFriendRequestDto friendShip);

    FriendShipResponse toFriendShipResponse(FriendShip friendShip);

    List<FriendShipResponse> toFriendShipResponses(List<FriendShip> friendShips);
}
