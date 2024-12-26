package org.tuandev.socialbe.services;

import org.tuandev.socialbe.dto.response.FriendResponse;
import org.tuandev.socialbe.dto.response.UserResponse;
import org.tuandev.socialbe.entities.Friend;

import java.util.List;

public interface FriendService {
    List<FriendResponse> getFriends();
    List<UserResponse> getNotFriends();
    List<FriendResponse> getPendingRequests();
    void sendFriendRequest(int friendId);
    void respondToRequest(int friendId, String response);
}
