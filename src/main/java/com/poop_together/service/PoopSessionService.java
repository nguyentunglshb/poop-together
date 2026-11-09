package com.poop_together.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.poop_together.dto.response.poopSession.PoopSessionResponse;
import com.poop_together.entity.PoopSession;
import com.poop_together.enums.SessionStatus;
import com.poop_together.exception.AppException;
import com.poop_together.exception.ErrorCode;
import com.poop_together.mapper.PoopSessionMapper;
import com.poop_together.repository.PoopSessionRepository;
import com.poop_together.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service 
@RequiredArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class PoopSessionService {
    PoopSessionRepository poopSessionRepository;
    PoopSessionMapper poopSessionMapper;
    UserRepository userRepository;

    @Transactional 
    public PoopSessionResponse startSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();

        if(poopSessionRepository.existsByUserIdAndStatus(userId, SessionStatus.ACTIVE)) {
            throw new AppException(ErrorCode.SESSION_ALREADY_ACTIVE);
        }

        PoopSession poopSession = new PoopSession();
        poopSession.setUserId(userId);
        poopSession.setStatus(SessionStatus.ACTIVE);
        poopSession.setDurationSeconds(0);

        return poopSessionMapper.toPoopSessionResponse(poopSessionRepository.saveAndFlush(poopSession));
    }
}
