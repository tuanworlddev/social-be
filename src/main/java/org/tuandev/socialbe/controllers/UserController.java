package org.tuandev.socialbe.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tuandev.socialbe.dto.response.Response;
import org.tuandev.socialbe.dto.response.UserResponse;
import org.tuandev.socialbe.entities.User;
import org.tuandev.socialbe.services.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Response> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(Response.builder()
                        .code(200)
                        .data(users)
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                .build());
    }
}
