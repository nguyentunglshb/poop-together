package com.poop_together.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.poop_together.dto.request.friendShip.SendFriendRequestDto;
import com.poop_together.dto.response.friendShip.FriendShipResponse;
import com.poop_together.dto.response.friendShip.FriendShipUserResponse;
import com.poop_together.dto.response.friendShip.ReceivedFriendRequestResponse;
import com.poop_together.dto.response.friendShip.SentFriendRequestResponse;
import com.poop_together.entity.FriendShip;
import com.poop_together.entity.User;

@Mapper (componentModel = "spring")
public interface FriendShipMapper {
    FriendShip toFriendShip(SendFriendRequestDto friendShip);

    FriendShipResponse toFriendShipResponse(FriendShip friendShip);

    List<FriendShipResponse> toFriendShipResponses(List<FriendShip> friendShips);

    SentFriendRequestResponse toSentFriendRequestResponse(FriendShip friendShip);

    List<SentFriendRequestResponse> toSentFriendRequestResponses(List<FriendShip> friendShips);

    ReceivedFriendRequestResponse toReceivedFriendRequestResponse(FriendShip friendShip);

    List<ReceivedFriendRequestResponse> toReceivedFriendRequestResponses(List<FriendShip> friendShips);

    FriendShipUserResponse toFriendShipUserResponse(User user);

}
