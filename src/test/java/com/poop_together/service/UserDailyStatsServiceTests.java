package com.poop_together.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.poop_together.entity.PoopSession;
import com.poop_together.entity.UserDailyStats;
import com.poop_together.repository.UserDailyStatsRepository;

@ExtendWith(MockitoExtension.class)
class UserDailyStatsServiceTests {

    @Mock
    UserDailyStatsRepository repository;

    @Test
    void leavesTheFirstDailyStatsIdForHibernateToGenerate() {
        var session = new PoopSession();
        session.setUserId("user-1");
        session.setStartedAt(Instant.parse("2026-11-12T01:00:00Z"));
        session.setDurationSeconds(60);
        LocalDate statDate = LocalDate.of(2026, 11, 12);
        when(repository.findByUserIdAndStatDate(session.getUserId(), statDate))
                .thenReturn(Optional.empty());

        new UserDailyStatsService(repository).addSession(session);

        var saved = ArgumentCaptor.forClass(UserDailyStats.class);
        verify(repository).save(saved.capture());
        UserDailyStats stats = saved.getValue();
        assertNull(stats.getId());
        assertEquals(session.getUserId(), stats.getUserId());
        assertEquals(statDate, stats.getStatDate());
        assertEquals(1L, stats.getSessionCount());
        assertEquals(60L, stats.getTotalSeconds());
    }
}
