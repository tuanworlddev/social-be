package org.tuandev.socialbe.services;

import org.tuandev.socialbe.dto.response.UserResponse;
import org.tuandev.socialbe.entities.User;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    boolean updateStatus(int userId, User.UserStatus status);
}
