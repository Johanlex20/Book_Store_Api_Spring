package com.bookstoreapi.bookstoreapi.repository;


import com.bookstoreapi.bookstoreapi.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findBySlug(String slug);

    boolean existsBySlug(String slug);

    //Buscar todos los Books que tengan como Slug un valor dado, pero se debe excluir de la busqueda aquel libro que tenga como id otro valor dado
    Optional<Book> findBySlugAndIdNot(String slug, Integer id);

    boolean existsBySlugAndIdNot(String slug, Integer id);

    //Retorna los ultimos 6 libros en base a la fecha de creacion en una lista
    List<Book> findTop6ByOrderByCreatedAtDesc();

}
