package com.poop_together.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.poop_together.dto.request.friendship.SendFriendRequestDto;
import com.poop_together.dto.response.friendship.FriendShipResponse;
import com.poop_together.entity.FriendShip;
import com.poop_together.enums.FriendshipStatus;
import com.poop_together.exception.AppException;
import com.poop_together.exception.ErrorCode;
import com.poop_together.mapper.FriendShipMapper;
import com.poop_together.repository.FriendShipRepository;
import com.poop_together.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor 
@Slf4j
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendShipService {
    FriendShipRepository friendShipRepository;
    FriendShipMapper friendShipMapper;
    UserRepository userRepository;

    public FriendShipResponse sendFriendRequest(SendFriendRequestDto req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String requesterId = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();
        if(requesterId.equals(req.getAddressee_id())) {
            throw new AppException(ErrorCode.FRIEND_REQUEST_SELF);
        }
        if (friendShipRepository.existsByRequesterIdAndAddresseeId(requesterId, req.getAddressee_id()) || friendShipRepository.existsByRequesterIdAndAddresseeId(req.getAddressee_id(), requesterId)) {
            throw new AppException(ErrorCode.FRIEND_REQUEST_ALREADY_SENT);
        }

        // Create and save the friend request
            FriendShip friendRequest = friendShipMapper.toFriendShip(req);
            friendRequest.setRequester_id(requesterId);
            friendRequest.setStatus(FriendshipStatus.PENDING);
            return friendShipMapper.toFriendShipResponse(friendShipRepository.save(friendRequest));
    }

    public List<FriendShipResponse> getFriendShips() {
        return friendShipMapper.toFriendShipResponses(friendShipRepository.findAll());
    }

    public List<FriendShipResponse> getSentFriendRequests() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String requesterId = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();
        return friendShipMapper.toFriendShipResponses(friendShipRepository.findByRequesterId(requesterId));
    }

    public List<FriendShipResponse> getReceivedFriendRequests() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String addresseeId = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();
        return friendShipMapper.toFriendShipResponses(friendShipRepository.findByAddresseeId(addresseeId));
    }
}
