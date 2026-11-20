package com.poop_together.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poop_together.dto.response.conversation.ReadStateResponse;
import com.poop_together.dto.response.conversation.UnreadSummaryResponse;
import com.poop_together.entity.ConversationMember;
import com.poop_together.exception.AppException;
import com.poop_together.exception.ErrorCode;
import com.poop_together.repository.ConversationMemberRepository;
import com.poop_together.repository.MessageRepository;
import com.poop_together.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReadStateService {
    MessageRepository messageRepository;
    ConversationAccessService accessService;
    ConversationMemberRepository memberRepository;
    UserRepository userRepository;

    @Transactional
    public ReadStateResponse markAsRead(String conversationId, Instant upTo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)).getId();

        accessService.getConversation(conversationId);
        ConversationMember m = accessService.assertActiveMember(conversationId, userId);

        Instant now = Instant.now().truncatedTo(ChronoUnit.MICROS);
        Instant target = (upTo == null || upTo.isAfter(now)) ? now : upTo.truncatedTo(ChronoUnit.MICROS);

        if (m.getLastReadAt() == null || target.isAfter(m.getLastReadAt())) {
            m.setLastReadAt(target);
            memberRepository.save(m);
        }

        return new ReadStateResponse(
                conversationId,
                m.getLastReadAt(),
                countUnread(m, userId));
    }

    @Transactional(readOnly = true)
    public UnreadSummaryResponse getUnreadySummary() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)).getId();

        List<ConversationMember> members = memberRepository.findByUserIdAndLeftAtIsNull(userId);
        Map<String, Long> counts = countUnreadByConversation(members, userId);

        long total = counts.values().stream().mapToLong(Long::longValue).sum();
        long withUnread = counts.values().stream().filter(c -> c > 0).count();
        return new UnreadSummaryResponse(total, withUnread);
    }

    public Map<String, Long> countUnreadByConversation(List<ConversationMember> members, String userId) {
        Map<String, Long> result = new HashMap<>();
        for (ConversationMember m : members) {
            result.put(m.getConversationId(), countUnread(m, userId));
        }
        return result;
    }

    public long countUnread(ConversationMember m, String userId) {
        return messageRepository.countByConversationIdAndCreatedAtAfterAndSenderIdNot(userId, baseline(m), userId);
    }

    private static Instant baseline(ConversationMember m) {
        return m.getLastReadAt() != null ? m.getLastReadAt() : m.getJoinedAt();
    }
}
