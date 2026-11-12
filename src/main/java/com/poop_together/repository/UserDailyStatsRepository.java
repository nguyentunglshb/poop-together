package com.poop_together.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poop_together.entity.UserDailyStats;

@Repository 
public interface UserDailyStatsRepository extends JpaRepository<UserDailyStats, String> {
    Optional<UserDailyStats> findByUserIdAndStatDate(String userId, LocalDate statDate);
}
