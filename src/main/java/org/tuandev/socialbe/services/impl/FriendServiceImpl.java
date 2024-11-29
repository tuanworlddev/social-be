package org.tuandev.socialbe.services.impl;

import org.springframework.stereotype.Service;
import org.tuandev.socialbe.config.SecurityUtil;
import org.tuandev.socialbe.dto.response.FriendResponse;
import org.tuandev.socialbe.entities.Friend;
import org.tuandev.socialbe.entities.User;
import org.tuandev.socialbe.exceptions.NotFoundException;
import org.tuandev.socialbe.mapper.FriendMapper;
import org.tuandev.socialbe.repositories.FriendRepository;
import org.tuandev.socialbe.repositories.UserRepository;
import org.tuandev.socialbe.services.FriendService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendServiceImpl implements FriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public FriendServiceImpl(UserRepository userRepository, FriendRepository friendRepository) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }

    @Override
    public List<FriendResponse> getAllFriends() {
        String userEmail = SecurityUtil.getCurrentEmail();
        if (userRepository.findByEmail(userEmail).isPresent()) {
            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NotFoundException(userEmail));
            List<Friend> friends = friendRepository.findByFriend(user);
            List<FriendResponse> friendResponses = friends.stream()
                    .map(FriendMapper::toFriendResponse)
                    .collect(Collectors.toList());
            Collections.reverse(friendResponses);
            return friendResponses;
        }
        return Collections.emptyList();
    }

    @Override
    public List<FriendResponse> getFriendsByStatus(Friend.FriendStatus status) {
        String userEmail = SecurityUtil.getCurrentEmail();
        if (userRepository.findByEmail(userEmail).isPresent()) {
            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NotFoundException(userEmail));
            List<Friend> friends = friendRepository.findByUserAndStatus(user, status);
            List<FriendResponse> friendResponses = friends.stream()
                    .map(FriendMapper::toFriendResponse)
                    .collect(Collectors.toList());
            Collections.reverse(friendResponses);
            return friendResponses;
        }
        return Collections.emptyList();
    }

    @Override
    public void addFriend(int friendId) {
        String userEmail = SecurityUtil.getCurrentEmail();
        if (userRepository.findByEmail(userEmail).isPresent()) {
            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NotFoundException("User not found"));
            User friend = userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("User not found"));
            Friend newFriend = Friend.builder()
                    .user(user)
                    .friend(friend)
                    .build();
            friendRepository.save(newFriend);
        }
    }

    @Override
    public void updateStatus(int id, Friend.FriendStatus status) {
        Friend friend = friendRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        friend.setStatus(status);
        friendRepository.save(friend);
    }
}
