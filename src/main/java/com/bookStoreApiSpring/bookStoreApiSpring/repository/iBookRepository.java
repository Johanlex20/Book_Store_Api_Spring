package com.bookStoreApiSpring.bookStoreApiSpring.repository;
import com.bookStoreApiSpring.bookStoreApiSpring.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface iBookRepository extends JpaRepository<Book,Integer> {
    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Integer id);

    List<Book> findTop6ByOrderByCreatedAtDesc();

    Optional<Book> findBySlug(String slug);
}
