package org.tuandev.socialbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tuandev.socialbe.entities.Friend;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
}
