package org.tuandev.socialbe.mapper;

import org.tuandev.socialbe.dto.response.UserResponse;
import org.tuandev.socialbe.entities.User;

public class UserMapper {
    public static UserResponse toUserResponse(final User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
