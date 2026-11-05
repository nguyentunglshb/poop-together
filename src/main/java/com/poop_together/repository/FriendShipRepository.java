package com.poop_together.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poop_together.entity.FriendShip;
import com.poop_together.enums.FriendShipStatus;

@Repository 
public interface FriendShipRepository extends JpaRepository<FriendShip, String> {
    @Query("""
    select case when count(f) > 0 then true else false end
    from FriendShip f
    where f.requester_id = :requesterId
      and f.addressee_id = :addresseeId
    """)
    boolean existsByRequesterIdAndAddresseeId(String requesterId, String addresseeId);

    @Query("select f from FriendShip f where f.requester_id = :requesterId and f.status = :status order by f.created_at desc")
    List<FriendShip> findByRequesterIdAndStatusOrderByCreatedAtDesc(String requesterId, FriendShipStatus status);

    @Query("select f from FriendShip f where f.addressee_id = :addresseeId and f.status = :status order by f.created_at desc")
    List<FriendShip> findByAddresseeIdAndStatusOrderByCreatedAtDesc(String addresseeId, FriendShipStatus status);
}
