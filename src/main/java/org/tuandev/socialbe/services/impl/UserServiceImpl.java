package org.tuandev.socialbe.services.impl;

import org.springframework.stereotype.Service;
import org.tuandev.socialbe.config.SecurityUtil;
import org.tuandev.socialbe.dto.response.UserResponse;
import org.tuandev.socialbe.entities.User;
import org.tuandev.socialbe.exceptions.NotFoundException;
import org.tuandev.socialbe.mapper.UserMapper;
import org.tuandev.socialbe.repositories.UserRepository;
import org.tuandev.socialbe.services.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String email = SecurityUtil.getCurrentEmail();
        if (email == null) {
            return null;
        }
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream()
                .map(UserMapper::toUserResponse)
                .collect(Collectors.toList());
        Collections.reverse(userResponses);
        return userResponses;
    }

    @Override
    public boolean updateStatus(int userId, User.UserStatus status) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        user.setStatus(status);
        userRepository.save(user);
        System.out.println("User " + status);
        return true;
    }
}
