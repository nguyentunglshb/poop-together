package com.poop_together.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poop_together.entity.PoopSession;
import com.poop_together.enums.SessionStatus;

@Repository 
public interface PoopSessionRepository extends JpaRepository<PoopSession, String> {
    boolean existsByUserIdAndStatus(String userId, SessionStatus status);
    
}
