package org.tuandev.socialbe.services;

import org.tuandev.socialbe.dto.request.AuthRequest;
import org.tuandev.socialbe.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);
    AuthResponse refreshToken(String refreshToken);
    AuthResponse register(AuthRequest authRequest);
}
