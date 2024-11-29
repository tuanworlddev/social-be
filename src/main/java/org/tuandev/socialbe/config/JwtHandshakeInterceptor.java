package org.tuandev.socialbe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.tuandev.socialbe.services.JWTService;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    private final JWTService jwtService;

    public JwtHandshakeInterceptor(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String userEmail = jwtService.getUsernameFromToken(token);
                attributes.put("username", userEmail);
                return true;
            } catch (Exception e) {
                System.out.println("Invalid JWT token: " + e.getMessage());
                return false;
            }
        }

        return false;
    }
}
