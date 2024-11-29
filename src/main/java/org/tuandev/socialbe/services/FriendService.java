package org.tuandev.socialbe.services;

import org.tuandev.socialbe.dto.response.FriendResponse;
import org.tuandev.socialbe.entities.Friend;

import java.util.List;

public interface FriendService {
    List<FriendResponse> getAllFriends();
    List<FriendResponse> getFriendsByStatus(Friend.FriendStatus status);
    void addFriend(int friendId);
    void updateStatus(int id, Friend.FriendStatus status);
}
