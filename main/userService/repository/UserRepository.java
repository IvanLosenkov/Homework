package org.LosenkovIvan.userService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.LosenkovIvan.userService.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}