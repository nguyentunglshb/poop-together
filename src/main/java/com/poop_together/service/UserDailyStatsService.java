package com.poop_together.service;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.poop_together.entity.PoopSession;
import com.poop_together.entity.UserDailyStats;
import com.poop_together.repository.UserDailyStatsRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service 
@RequiredArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDailyStatsService {
    static ZoneId STATS_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    UserDailyStatsRepository userDailyStatsRepository;
    
    @Transactional (propagation = Propagation.MANDATORY)
    public void addSession(PoopSession poopSession) {
        LocalDate statDate = poopSession.getStartedAt().atZone(STATS_ZONE).toLocalDate();

        UserDailyStats stats = userDailyStatsRepository
                .findByUserIdAndStatDate(poopSession.getUserId(), statDate)
                .orElseGet(() -> UserDailyStats.builder()
                        .userId(poopSession.getUserId())
                        .statDate(statDate)
                        .build());
        stats.setTotalSeconds(stats.getTotalSeconds() + poopSession.getDurationSeconds());
        Long sessionCount = stats.getSessionCount();
        stats.setSessionCount(sessionCount == null ? 1L : sessionCount + 1L);

        userDailyStatsRepository.save(stats);
    }
}
