package com.poop_together.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service 
@RequiredArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class PoopSessionService {
    private static final ZoneId SESSION_TIME_ZONE = ZoneId.of("Asia/Bangkok");

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
        LocalDate from = searchSessionDto.getFrom();
        LocalDate to = searchSessionDto.getTo();
        if (from != null && to != null && from.isAfter(to)) {
            throw new AppException(ErrorCode.INPUT_NOT_VALID);
        }

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
        Specification<PoopSession> filters = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) {
                predicates.add(builder.equal(root.get("status"), status));
            }
            if (from != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.<Instant>get("startedAt"),
                        from.atStartOfDay(SESSION_TIME_ZONE).toInstant()));
            }
            if (to != null) {
                predicates.add(builder.lessThan(root.<Instant>get("startedAt"),
                        to.plusDays(1).atStartOfDay(SESSION_TIME_ZONE).toInstant()));
            }
            return builder.and(predicates.toArray(Predicate[]::new));
        };

        return poopSessionRepository.findAll(filters, pageable);

    }
}
