package org.tuandev.socialbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tuandev.socialbe.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
