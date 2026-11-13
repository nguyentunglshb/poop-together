package com.poop_together.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poop_together.dto.ApiResponse;
import com.poop_together.dto.request.conversation.DirectConversationRequestDto;
import com.poop_together.dto.response.conversation.ConversationResponse;
import com.poop_together.service.ConversationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {
    ConversationService conversationService;

    @PostMapping("/direct")
    ApiResponse<ConversationResponse> createDirectConversation(
            @RequestBody DirectConversationRequestDto directConversationRequestDto) {
        return ApiResponse
                .<ConversationResponse>builder()
                .result(conversationService.getOrCreteDirect(directConversationRequestDto))
                .build();
    }

    @GetMapping
    ApiResponse<List<ConversationResponse>> getConversations() {
        return ApiResponse
                .<List<ConversationResponse>>builder()
                .result(conversationService.getMyConversations())
                .build();
    }

    @GetMapping("/{conversationId}")
    ApiResponse<ConversationResponse> getConversation(@RequestParam String conversationId) {
        return ApiResponse
                .<ConversationResponse>builder()
                .result(conversationService.getConversation(conversationId))
                .build();
    }

}
