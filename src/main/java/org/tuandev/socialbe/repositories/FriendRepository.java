package org.tuandev.socialbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tuandev.socialbe.entities.Friend;
import org.tuandev.socialbe.entities.User;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
    List<Friend> findByFriend(User user);
    List<Friend> findByUserAndStatus(User user, Friend.FriendStatus status);
    List<Friend> findByFriendAndStatus(User user, Friend.FriendStatus status);
    List<Friend> findByUser(User user);
}
