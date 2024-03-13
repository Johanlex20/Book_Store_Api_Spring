package com.bookstoreapi.bookstoreapi.repository;

import com.bookstoreapi.bookstoreapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer idNot);

    Optional<User> findByEmail(String email);
}
