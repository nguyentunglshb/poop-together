package com.poop_together.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.poop_together.dto.request.friendShip.SendFriendRequestDto;
import com.poop_together.dto.request.friendShip.UpdateStatusRequestDto;
import com.poop_together.dto.response.friendShip.FriendShipResponse;
import com.poop_together.dto.response.friendShip.ReceivedFriendRequestResponse;
import com.poop_together.dto.response.friendShip.SentFriendRequestResponse;
import com.poop_together.entity.FriendShip;
import com.poop_together.entity.User;
import com.poop_together.enums.FriendShipStatus;
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
            friendRequest.setStatus(FriendShipStatus.PENDING);
            return friendShipMapper.toFriendShipResponse(friendShipRepository.save(friendRequest));
    }

    public List<FriendShipResponse> getFriendShips() {
        return friendShipMapper.toFriendShipResponses(friendShipRepository.findAll());
    }

    public List<SentFriendRequestResponse> getSentFriendRequests() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String requesterId = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();
        List<FriendShip> sentRequests = friendShipRepository.findByRequesterIdAndStatusOrderByCreatedAtDesc(requesterId, FriendShipStatus.PENDING);
        Map<String, User> users = loadUsers(sentRequests, FriendShip::getAddressee_id);
        return sentRequests.stream()
                    .map(f -> {
                        SentFriendRequestResponse response =
                            friendShipMapper.toSentFriendRequestResponse(f);

                        response.setAddressee(
                            friendShipMapper.toFriendShipUserResponse(
                                users.get(f.getAddressee_id())));

                        return response;
                    })
                    .toList();
    }

    public List<ReceivedFriendRequestResponse> getReceivedFriendRequests() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String addresseeId = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();
        List<FriendShip> receivedRequests = friendShipRepository.findByAddresseeIdAndStatusOrderByCreatedAtDesc(addresseeId, FriendShipStatus.PENDING);
        Map<String, User> users = loadUsers(receivedRequests, FriendShip::getRequester_id);
        return receivedRequests.stream()
                    .map(f -> {
                        ReceivedFriendRequestResponse response = friendShipMapper.toReceivedFriendRequestResponse(f);
                        response.setRequester(friendShipMapper.toFriendShipUserResponse(users.get(f.getRequester_id())));
                        return response;
                    })
                    .toList();
    }

    public FriendShipResponse updateFriendRequestStatus(UpdateStatusRequestDto req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String addresseeId = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();
        FriendShip friendRequest = friendShipRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));
        if (!friendRequest.getAddressee_id().equals(addresseeId)) {
            throw new AppException(ErrorCode.FRIEND_REQUEST_NOT_AUTHORIZED);
        }
        if (friendRequest.getStatus() != FriendShipStatus.PENDING) {
            throw new AppException(ErrorCode.FRIEND_REQUEST_ALREADY_RESPONDED);
        }
        friendRequest.setStatus(req.getStatus());
        return friendShipMapper.toFriendShipResponse(friendShipRepository.save(friendRequest));
    }


        // Lấy user của phía bên kia bằng 1 query duy nhất (tránh N+1)
    private Map<String, User> loadUsers(List<FriendShip> list, Function<FriendShip, String> idGetter) {
        if (list.isEmpty()) {
            return Map.of();
        }
        Set<String> ids = list.stream().map(idGetter).collect(Collectors.toSet());
        return userRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
    }
}
