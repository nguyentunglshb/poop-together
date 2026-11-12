package com.poop_together.controller;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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
    private static final DateTimeFormatter DATE_FILTER_FORMAT = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4)
            .appendPattern("-MM-dd")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .toFormatter(Locale.ROOT)
            .withResolverStyle(ResolverStyle.STRICT)
            .withZone(PoopSessionService.SESSION_TIME_ZONE);

    PoopSessionService poopSessionService;

    @InitBinder("searchSessionDTO")
    void bindSearchDates(WebDataBinder binder) {
        binder.addCustomFormatter(new Formatter<Instant>() {
            @Override
            public Instant parse(String text, Locale locale) {
                try {
                    return DATE_FILTER_FORMAT.parse(text, Instant::from);
                } catch (DateTimeParseException exception) {
                    throw new IllegalArgumentException("Expected a valid yyyy-MM-dd date", exception);
                }
            }

            @Override
            public String print(Instant instant, Locale locale) {
                return DATE_FILTER_FORMAT.format(instant);
            }
        }, "from", "to");
    }

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
