package org.tuandev.socialbe.mapper;

import org.tuandev.socialbe.dto.response.FriendResponse;
import org.tuandev.socialbe.entities.Friend;
import org.tuandev.socialbe.entities.User;

public class FriendMapper {
    public static FriendResponse toFriendResponse(Friend friend, User currentUser) {
        User friendUser = friend.getUser().equals(currentUser) ? friend.getFriend() : friend.getUser();

        return FriendResponse.builder()
                .id(friend.getId())
                .friend(UserMapper.toUserResponse(friendUser))
                .status(friend.getStatus().name())
                .createdAt(friend.getCreatedAt())
                .updatedAt(friend.getUpdatedAt())
                .build();
    }

    public static FriendResponse toFriendResponse(Friend friend) {
        return FriendResponse.builder()
                .id(friend.getId())
                .friend(UserMapper.toUserResponse(friend.getUser()))
                .status(friend.getStatus().name())
                .createdAt(friend.getCreatedAt())
                .updatedAt(friend.getUpdatedAt())
                .build();
    }
}
