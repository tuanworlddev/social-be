package org.tuandev.socialbe.mapper;

import org.tuandev.socialbe.dto.response.FriendResponse;
import org.tuandev.socialbe.entities.Friend;

public class FriendMapper {
    public static FriendResponse toFriendResponse(Friend friend) {
        return FriendResponse.builder()
                .id(friend.getId())
                .friend(UserMapper.toUserResponse(friend.getFriend()))
                .status(friend.getStatus().name())
                .createdAt(friend.getCreatedAt())
                .updatedAt(friend.getUpdatedAt())
                .build();
    }
}
