package com.poop_together.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poop_together.dto.ApiResponse;
import com.poop_together.dto.request.friendShip.SendFriendRequestDto;
import com.poop_together.dto.request.friendShip.UpdateStatusRequestDto;
import com.poop_together.dto.response.friendShip.FriendShipResponse;
import com.poop_together.dto.response.friendShip.ReceivedFriendRequestResponse;
import com.poop_together.dto.response.friendShip.SentFriendRequestResponse;
import com.poop_together.service.FriendShipService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@RestController 
@RequestMapping ("/friendships")
@RequiredArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendShipController {
    FriendShipService friendShipService;
    
    @PostMapping()
    ApiResponse<FriendShipResponse> sendFriendRequest(@RequestBody SendFriendRequestDto req) {
        return ApiResponse.<FriendShipResponse>builder()
                .result(friendShipService.sendFriendRequest(req))
                .build();
    }
    
    @GetMapping()
    ApiResponse<List<FriendShipResponse>> getFriendShips() {
        return ApiResponse.<List<FriendShipResponse>>builder()
                .result(friendShipService.getFriendShips())
                .build();
    }

    @GetMapping("/sent")
    ApiResponse<List<SentFriendRequestResponse>> getSentFriendRequests() {
        return ApiResponse.<List<SentFriendRequestResponse>>builder()
                .result(friendShipService.getSentFriendRequests())
                .build();
    }

    @GetMapping("/received")
    ApiResponse<List<ReceivedFriendRequestResponse>> getReceivedFriendRequests() {
        return ApiResponse.<List<ReceivedFriendRequestResponse>>builder()
                .result(friendShipService.getReceivedFriendRequests())
                .build();
    }

    @PostMapping ("/update-status")
    ApiResponse<FriendShipResponse> updateFriendRequestStatus(@RequestBody UpdateStatusRequestDto req) {
        return ApiResponse.<FriendShipResponse>builder()
                .result(friendShipService.updateFriendRequestStatus(req))
                .build();
    }
    
}
