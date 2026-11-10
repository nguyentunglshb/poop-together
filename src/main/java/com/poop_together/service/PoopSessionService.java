package com.poop_together.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.poop_together.dto.request.poopSession.SearchSessionDTO;
import com.poop_together.dto.response.poopSession.PoopSessionResponse;
import com.poop_together.entity.PoopSession;
import com.poop_together.enums.SessionStatus;
import com.poop_together.enums.SortBySession;
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

    @Transactional 
    public PoopSessionResponse stopSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();

        if(!poopSessionRepository.existsByUserIdAndStatus(userId, SessionStatus.ACTIVE)) {
            throw new AppException(ErrorCode.SESSION_NOT_ACTIVE);
        }

        Optional<PoopSession> poopSessionOptional = poopSessionRepository.findByUserIdAndStatus(userId, SessionStatus.ACTIVE);
        if (poopSessionOptional.isEmpty()) {
            throw new AppException(ErrorCode.SESSION_NOT_ACTIVE);
        }

        PoopSession poopSession = poopSessionOptional.get();
        poopSession.setStatus(SessionStatus.COMPLETED);

        long durationSeconds = (System.currentTimeMillis() - poopSession.getStartedAt().toEpochMilli()) / 1000;
        poopSession.setDurationSeconds((int) durationSeconds);
        return poopSessionMapper.toPoopSessionResponse(poopSessionRepository.saveAndFlush(poopSession));
    }

    public PoopSessionResponse getCurrentSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getId();

        if(!poopSessionRepository.existsByUserIdAndStatus(userId, SessionStatus.ACTIVE)) {
            throw new AppException(ErrorCode.SESSION_NOT_ACTIVE);
        }

        Optional<PoopSession> poopSessionOptional = poopSessionRepository.findByUserIdAndStatus(userId, SessionStatus.ACTIVE);
        if (poopSessionOptional.isEmpty()) {
            throw new AppException(ErrorCode.SESSION_NOT_ACTIVE);
        }

        PoopSession poopSession = poopSessionOptional.get();
        long durationSeconds = (System.currentTimeMillis() - poopSession.getStartedAt().toEpochMilli()) / 1000;
        poopSession.setDurationSeconds((int) durationSeconds);
        return poopSessionMapper.toPoopSessionResponse(poopSessionRepository.saveAndFlush(poopSession));
    }

    public Page<PoopSession> getSessions(SearchSessionDTO searchSessionDto) {
        String sortProperty = Arrays.stream(SortBySession.values())
                .filter(sortBy -> sortBy.getValue().equals(searchSessionDto.getSortBy()))
                .map(SortBySession::getProperty)
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.INPUT_NOT_VALID));
        Sort sort = Sort.by(
            searchSessionDto.getSortDirection(),
            sortProperty
        );

        Pageable pageable = PageRequest.of(
            searchSessionDto.getPageNumber(),
            searchSessionDto.getPageSize(),
            sort
        );
        SessionStatus status = searchSessionDto.getStatus();
        if (status == null) {
            return poopSessionRepository.findAll(pageable);
        }

        return poopSessionRepository.findByStatus(status, pageable);

    }
}
