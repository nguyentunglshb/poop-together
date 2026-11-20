package com.poop_together.service;

import org.springframework.stereotype.Service;

import com.poop_together.enums.FriendShipStatus;
import com.poop_together.repository.FriendShipRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendShipChecker {
    FriendShipRepository friendShipRepository;

    public boolean areFriends(String a, String b) {
        return friendShipRepository.existsByRequesterIdAndAddresseeIdAndStatus(b, a, FriendShipStatus.ACCEPTED)
                || friendShipRepository.existsByRequesterIdAndAddresseeIdAndStatus(a, b, FriendShipStatus.ACCEPTED);
    }
}
