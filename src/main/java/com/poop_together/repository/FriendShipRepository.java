package com.poop_together.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poop_together.entity.FriendShip;

@Repository 
public interface FriendShipRepository extends JpaRepository<FriendShip, String> {
    @Query("""
    select case when count(f) > 0 then true else false end
    from FriendShip f
    where f.requester_id = :requesterId
      and f.addressee_id = :addresseeId
    """)
    boolean existsByRequesterIdAndAddresseeId(String requesterId, String addresseeId);

    @Query("select f from FriendShip f where f.requester_id = :requesterId")
    List<FriendShip> findByRequesterId(String requesterId);

    @Query("select f from FriendShip f where f.addressee_id = :addresseeId")
    List<FriendShip> findByAddresseeId(String addresseeId);
}
