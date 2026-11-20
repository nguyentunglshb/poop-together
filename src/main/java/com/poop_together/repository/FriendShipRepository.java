package com.poop_together.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poop_together.entity.FriendShip;
import com.poop_together.enums.FriendShipStatus;

@Repository 
public interface FriendShipRepository extends JpaRepository<FriendShip, String> {
   boolean existsByRequesterIdAndAddresseeId(
    String requesterId, String addresseeId);

    boolean existsByRequesterIdAndAddresseeIdAndStatus(String requesterId, String addresseeId, FriendShipStatus status);

  List<FriendShip> findByRequesterIdAndStatusOrderByCreatedAtDesc(
      String requesterId, FriendShipStatus status);

  List<FriendShip> findByAddresseeIdAndStatusOrderByCreatedAtDesc(
      String addresseeId, FriendShipStatus status);
}
