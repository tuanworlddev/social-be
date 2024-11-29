package org.tuandev.socialbe.services.impl;

import org.springframework.stereotype.Service;
import org.tuandev.socialbe.dto.response.UserResponse;
import org.tuandev.socialbe.entities.User;
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

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream()
                .map(UserMapper::toUserResponse)
                .collect(Collectors.toList());
        Collections.reverse(userResponses);
        return userResponses;
    }
}
