package org.tuandev.socialbe.services;

import org.tuandev.socialbe.dto.request.AuthRequest;
import org.tuandev.socialbe.dto.request.LogoutRequest;
import org.tuandev.socialbe.dto.request.UserRequest;
import org.tuandev.socialbe.dto.response.Response;
import org.tuandev.socialbe.entities.User;

public interface AuthService {
    Response login(AuthRequest authRequest);
    void register(UserRequest userRequest, User.UserRole userRole);
    Response refreshAccessToken(String refreshToken);
    void logout(LogoutRequest logoutRequest);
}
