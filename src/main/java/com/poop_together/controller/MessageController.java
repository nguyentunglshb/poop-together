package com.poop_together.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poop_together.dto.ApiResponse;
import com.poop_together.dto.request.conversation.MessageHistoryRequestDto;
import com.poop_together.dto.request.conversation.SendMessageRequestDto;
import com.poop_together.dto.response.conversation.MessagePageResponse;
import com.poop_together.dto.response.conversation.MessageResponse;
import com.poop_together.service.MessageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/conversations/{conversationId}/messages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {
    MessageService messageService;

    @PostMapping
    ApiResponse<MessageResponse> send(@PathVariable String conversationId,
            @RequestBody SendMessageRequestDto sendMessageRequestDto) {
        return ApiResponse.<MessageResponse>builder()
                .result(messageService.send(conversationId, sendMessageRequestDto))
                .build();
    }

    @GetMapping
    ApiResponse<MessagePageResponse> history(@PathVariable String conversationId,
            MessageHistoryRequestDto messageHistoryRequestDto) {
        return ApiResponse.<MessagePageResponse>builder()
                .result(messageService.getHistory(conversationId, messageHistoryRequestDto))
                .build();
    }
}
