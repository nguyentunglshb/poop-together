package com.poop_together.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.poop_together.entity.PoopSession;
import com.poop_together.exception.GlobalExceptionHandler;
import com.poop_together.mapper.PoopSessionMapper;
import com.poop_together.repository.PoopSessionRepository;
import com.poop_together.repository.UserRepository;
import com.poop_together.service.PoopSessionService;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class PoopSessionDateBindingTests {

    @Mock
    PoopSessionRepository repository;

    @Mock
    PoopSessionMapper mapper;

    @Mock
    UserRepository userRepository;

    @Mock
    Root<PoopSession> root;

    @Mock
    Path<Instant> startedAt;

    @Mock
    CriteriaBuilder builder;

    @Captor
    ArgumentCaptor<Specification<PoopSession>> filters;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        var service = new PoopSessionService(repository, mapper, userRepository, null);
        mvc = MockMvcBuilders.standaloneSetup(new PoopSessionController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void filtersTheEntireLocalDateWithInstantBounds() throws Exception {
        when(repository.findAll(ArgumentMatchers.<Specification<PoopSession>>any(), any(Pageable.class)))
                .thenAnswer(invocation -> Page.empty(invocation.getArgument(1)));
        when(root.<Instant>get("startedAt")).thenReturn(startedAt);

        mvc.perform(get("/poop-sessions")
                .param("from", "2026-11-12")
                .param("to", "2026-11-12"))
                .andExpect(status().isOk());

        verify(repository).findAll(filters.capture(), any(Pageable.class));
        filters.getValue().toPredicate(root, null, builder);
        verify(builder).greaterThanOrEqualTo(startedAt, Instant.parse("2026-11-11T17:00:00Z"));
        verify(builder).lessThan(startedAt, Instant.parse("2026-11-12T17:00:00Z"));
    }

    @ParameterizedTest
    @CsvSource({"from,2026-02-30", "to,2026-11-12T00:00:00Z", "from,2026-1-12"})
    void rejectsInvalidDateInputs(String field, String value) throws Exception {
        mvc.perform(get("/poop-sessions").param(field, value))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(repository);
    }

    @Test
    void rejectsReversedDateRanges() throws Exception {
        mvc.perform(get("/poop-sessions")
                .param("from", "2026-11-13")
                .param("to", "2026-11-12"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(repository);
    }
}
