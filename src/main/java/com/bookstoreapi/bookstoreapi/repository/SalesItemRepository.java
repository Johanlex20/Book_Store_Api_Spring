package com.bookstoreapi.bookstoreapi.repository;

import com.bookstoreapi.bookstoreapi.domain.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesItemRepository extends JpaRepository<SalesItem,Integer> {
}
