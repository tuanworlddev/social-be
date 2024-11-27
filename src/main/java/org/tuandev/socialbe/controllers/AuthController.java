package org.tuandev.socialbe.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tuandev.socialbe.dto.request.AuthRequest;
import org.tuandev.socialbe.dto.request.LogoutRequest;
import org.tuandev.socialbe.dto.request.RefreshTokenRequest;
import org.tuandev.socialbe.dto.request.UserRequest;
import org.tuandev.socialbe.dto.response.Response;
import org.tuandev.socialbe.entities.User;
import org.tuandev.socialbe.services.AuthService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody @Valid AuthRequest authRequest) {
        Response response = authService.login(authRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Response> createUser(@Valid @RequestBody UserRequest userRequest) {
        authService.register(userRequest, User.UserRole.ROLE_USER);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.builder()
                .message("User created")
                .code(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Response> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshToken) {
        Response response = authService.refreshAccessToken(refreshToken.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(@RequestBody @Valid LogoutRequest logoutRequest) {
        authService.logout(logoutRequest);
        return ResponseEntity.status(HttpStatus.OK).body(Response.builder()
                .code(HttpStatus.OK.value())
                .message("User logged out")
                .build());
    }
}
