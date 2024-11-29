package org.tuandev.socialbe.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.tuandev.socialbe.services.JWTService;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JWTService jwtService;

    public JwtChannelInterceptor(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if ("CONNECT".equals(accessor.getCommand().name())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    String userEmail = jwtService.getUsernameFromToken(token);
                    accessor.setUser(() -> userEmail);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid JWT token");
                }
            }
        }
        return message;
    }
}
