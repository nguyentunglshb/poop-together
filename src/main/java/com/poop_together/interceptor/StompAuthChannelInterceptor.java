package com.poop_together.interceptor;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StompAuthChannelInterceptor implements ChannelInterceptor {
    JwtDecoder jwtDecoder;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String header = accessor.getFirstNativeHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                throw new MessagingException("Unauthorize");
            }

            String userId = null;

            try {
                String token = header.substring(7);
                userId = jwtDecoder.decode(token).getClaimAsString("user_id");

            } catch (Exception e) {
                throw new MessagingException("Unauthorize");
            }

            // Principal.getName() phải là userId: convertAndSendToUser() tìm người nhận
            // theo tên này
            accessor.setUser(new UsernamePasswordAuthenticationToken(userId, null, List.of()));
        }

        // Chỉ cho đăng ký kênh riêng của chính mình
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            if (accessor.getUser() == null || destination == null || !destination.startsWith("/user/queue")) {
                throw new MessagingException("Cannot subscribe this channel");
            }
        }

        return message;
    }

}
