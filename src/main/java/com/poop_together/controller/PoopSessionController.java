package com.poop_together.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poop_together.dto.ApiResponse;
import com.poop_together.dto.response.poopSession.PoopSessionResponse;
import com.poop_together.service.PoopSessionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



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
    public void  stopSession(@RequestBody String entity) {
        //TODO: process POST request
    }

    @GetMapping("/current")
    public String getCurrentSession(@RequestParam String param) {
        return new String();
    }

    @GetMapping()
    public String getSessions(@RequestParam String param) {
        return new String();
    }
    
    
    
    
    
}
