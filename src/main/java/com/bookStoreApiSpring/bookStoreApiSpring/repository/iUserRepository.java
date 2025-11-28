package com.bookStoreApiSpring.bookStoreApiSpring.repository;
import com.bookStoreApiSpring.bookStoreApiSpring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface iUserRepository extends JpaRepository<User,Integer> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer id);

    Optional<User> findByEmail(String email);
}
