package org.tuandev.socialbe.services.impl;

import org.springframework.stereotype.Service;
import org.tuandev.socialbe.config.SecurityUtil;
import org.tuandev.socialbe.dto.response.FriendResponse;
import org.tuandev.socialbe.dto.response.UserResponse;
import org.tuandev.socialbe.entities.Friend;
import org.tuandev.socialbe.entities.User;
import org.tuandev.socialbe.exceptions.NotAuthenticatedException;
import org.tuandev.socialbe.exceptions.NotFoundException;
import org.tuandev.socialbe.mapper.FriendMapper;
import org.tuandev.socialbe.mapper.UserMapper;
import org.tuandev.socialbe.repositories.FriendRepository;
import org.tuandev.socialbe.repositories.UserRepository;
import org.tuandev.socialbe.services.FriendService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FriendServiceImpl implements FriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public FriendServiceImpl(UserRepository userRepository, FriendRepository friendRepository) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }

    private User getCurrentUser() {
        String email = SecurityUtil.getCurrentEmail();
        if (email == null) {
            return null;
        }
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public List<FriendResponse> getFriends() {
        User currentUser = getCurrentUser();

        // Lấy danh sách bạn bè mà currentUser là người gửi yêu cầu kết bạn
        List<Friend> friendsAsUser = friendRepository.findByUserAndStatus(currentUser, Friend.FriendStatus.accepted);

        // Lấy danh sách bạn bè mà currentUser là người nhận yêu cầu kết bạn
        List<Friend> friendsAsFriend = friendRepository.findByFriendAndStatus(currentUser, Friend.FriendStatus.accepted);

        // Gộp hai danh sách lại và map sang FriendResponse
        return Stream.concat(friendsAsUser.stream(), friendsAsFriend.stream())
                .map(friend -> FriendMapper.toFriendResponse(friend, currentUser))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getNotFriends() {
        User currentUser = getCurrentUser();

        // Lấy danh sách bạn bè và đang chờ (cả khi currentUser là user và friend)
        List<User> friendsAndPending = Stream.concat(
                friendRepository.findByUser(currentUser).stream().map(Friend::getFriend),
                friendRepository.findByFriend(currentUser).stream().map(Friend::getUser)
        ).toList();

        // Lọc ra những người dùng không phải bạn bè hoặc đang chờ
        return userRepository.findAll().stream()
                .filter(user -> !user.equals(currentUser) && !friendsAndPending.contains(user))
                .map(UserMapper::toUserResponse)
                .toList();
    }


    @Override
    public List<FriendResponse> getPendingRequests() {
        User currentUser = getCurrentUser();
        return friendRepository.findByFriend(currentUser)
                .stream()
                .filter(friend -> friend.getStatus() == Friend.FriendStatus.pending)
                .map(FriendMapper::toFriendResponse)
                .toList();
    }

    @Override
    public void sendFriendRequest(int friendId) {
        User currentUser = getCurrentUser();
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Friend not found"));

        if (friendRepository.findByUserAndStatus(currentUser, Friend.FriendStatus.accepted).stream()
                .anyMatch(f -> f.getFriend().getId() == friendId)) {
            throw new IllegalArgumentException("Already friends");
        }

        Friend friendRequest = Friend.builder()
                .user(currentUser)
                .friend(friend)
                .status(Friend.FriendStatus.pending)
                .build();

        friendRepository.save(friendRequest);
    }

    @Override
    public void respondToRequest(int friendId, String response) {
        User currentUser = getCurrentUser();

        Friend friendRequest = friendRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Friend request not found"));

        if (!friendRequest.getFriend().equals(currentUser)) {
            throw new NotAuthenticatedException("You are not authorized to respond to this request");
        }

        friendRequest.setStatus(Friend.FriendStatus.valueOf(response));

        friendRepository.save(friendRequest);
    }

}
