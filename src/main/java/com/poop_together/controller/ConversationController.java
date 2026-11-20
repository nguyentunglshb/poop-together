package com.poop_together.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poop_together.dto.ApiResponse;
import com.poop_together.dto.request.conversation.DirectConversationRequestDto;
import com.poop_together.dto.request.conversation.MarkReadRequestDto;
import com.poop_together.dto.response.conversation.ConversationResponse;
import com.poop_together.dto.response.conversation.ReadStateResponse;
import com.poop_together.dto.response.conversation.UnreadSummaryResponse;
import com.poop_together.service.ConversationService;
import com.poop_together.service.ReadStateService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {
    ReadStateService readStateService;
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
    ApiResponse<ConversationResponse> getConversation(@PathVariable String conversationId) {
        return ApiResponse
                .<ConversationResponse>builder()
                .result(conversationService.getConversation(conversationId))
                .build();
    }

    @PostMapping("/{conversationId}/read")
    ApiResponse<ReadStateResponse> markRead(@PathVariable String conversationId,
            @RequestBody(required = false) MarkReadRequestDto markReadRequestDto) {
        Instant upTo = markReadRequestDto != null ? markReadRequestDto.getUpTo() : null;
        return ApiResponse.<ReadStateResponse>builder()
                .result(readStateService.markAsRead(conversationId, upTo))
                .build();
    }

    @GetMapping("/unread-count")
    ApiResponse<UnreadSummaryResponse> unreadCount() {
        return ApiResponse.<UnreadSummaryResponse>builder()
                .result(readStateService.getUnreadySummary())
                .build();
    }

}
