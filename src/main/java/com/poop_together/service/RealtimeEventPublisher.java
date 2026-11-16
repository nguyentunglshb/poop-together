package com.poop_together.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.poop_together.dto.RealtimeEvent;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RealtimeEventPublisher {
    SimpMessagingTemplate messagingTemplate;

    public void sendToUser(String userId, String type, Object payload) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/events",
                RealtimeEvent.builder()
                        .type(type)
                        .payload(payload).build());
    }
}