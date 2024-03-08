package com.bookstoreapi.bookstoreapi.repository;

import com.bookstoreapi.bookstoreapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer idNot);
}
