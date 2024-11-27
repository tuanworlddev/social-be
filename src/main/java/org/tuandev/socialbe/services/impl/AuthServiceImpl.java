package org.tuandev.socialbe.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tuandev.socialbe.dto.request.AuthRequest;
import org.tuandev.socialbe.dto.request.LogoutRequest;
import org.tuandev.socialbe.dto.request.UserRequest;
import org.tuandev.socialbe.dto.response.AuthResponse;
import org.tuandev.socialbe.dto.response.Response;
import org.tuandev.socialbe.entities.User;
import org.tuandev.socialbe.exceptions.AlreadyExistsException;
import org.tuandev.socialbe.exceptions.NotAuthenticatedException;
import org.tuandev.socialbe.exceptions.NotFoundException;
import org.tuandev.socialbe.repositories.UserRepository;
import org.tuandev.socialbe.security.CustomUserDetails;
import org.tuandev.socialbe.services.AuthService;
import org.tuandev.socialbe.services.JWTService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Response login(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new NotAuthenticatedException("Invalid email or password");
        }

        String accessToken = jwtService.generateAccessToken(new CustomUserDetails(user));
        String refreshToken = jwtService.generateRefreshToken(new CustomUserDetails(user));

        return Response.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully logged in")
                .data(AuthResponse.builder()
                        .token(accessToken)
                        .refreshToken(refreshToken)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public void register(UserRequest userRequest, User.UserRole userRole) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new AlreadyExistsException("User already exists");
        }
        User user = User.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .fullName(userRequest.getFullName())
                .role(userRole)
                .avatar("https://media.istockphoto.com/id/1268548918/vector/white-create-account-screen-icon-isolated-with-long-shadow-red-circle-button-vector.jpg?s=612x612&w=0&k=20&c=tyaWWtW2_yQyvK4hBnVXEt3tfSNr0jVC_6P7XbOBrbk=")
                .build();
        userRepository.save(user);
    }

    @Override
    public Response refreshAccessToken(String refreshToken) {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw new NotAuthenticatedException("Invalid or expired refresh token");
        }

        String username = jwtService.getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String accessToken = jwtService.generateAccessToken(new CustomUserDetails(user));

        return Response.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully logged in")
                .data(AuthResponse.builder()
                        .token(accessToken)
                        .refreshToken(refreshToken)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        if (!jwtService.validateRefreshToken(logoutRequest.getRefreshToken())) {
            throw new NotAuthenticatedException("Invalid or expired refresh token");
        }
        redisTemplate.opsForValue().set(
                "backlist:" + logoutRequest.getRefreshToken(),
                "true",
                jwtService.getExpirationTime(logoutRequest.getRefreshToken()) - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS
        );
    }

}
