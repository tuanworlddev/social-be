package org.tuandev.socialbe.services;

import org.tuandev.socialbe.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

}
