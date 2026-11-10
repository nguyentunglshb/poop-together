package com.poop_together.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poop_together.dto.ApiResponse;
import com.poop_together.dto.request.poopSession.SearchSessionDTO;
import com.poop_together.dto.response.poopSession.PoopSessionResponse;
import com.poop_together.entity.PoopSession;
import com.poop_together.service.PoopSessionService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;



@RestController 
@RequestMapping ("/poop-sessions")
@RequiredArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class PoopSessionController {
    PoopSessionService poopSessionService;

    @PostMapping("/start")
    ApiResponse<PoopSessionResponse> startSession() {
        return ApiResponse
        .<PoopSessionResponse>builder()
        .result(poopSessionService.startSession())
        .build();
    }

    @PostMapping("/stop")
    ApiResponse<PoopSessionResponse> stopSession() {
        return ApiResponse
                .<PoopSessionResponse>builder()
                .result(poopSessionService.stopSession())
                .build();
    }

    @GetMapping("/current")
    ApiResponse<PoopSessionResponse> getCurrentSession() {
        return ApiResponse
                .<PoopSessionResponse>builder()
                .result(poopSessionService.getCurrentSession())
                .build();
    }

    @GetMapping()
    ApiResponse<Page<PoopSession>> getSessions(@Valid @ModelAttribute SearchSessionDTO searchSessionDto) {
        return ApiResponse
                .<Page<PoopSession>>builder()
                .result(poopSessionService.getSessions(searchSessionDto))
                .build();
    }
    
    
    
    
    
}
