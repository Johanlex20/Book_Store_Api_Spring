package com.bookstoreapi.bookstoreapi;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesItemRepository extends JpaRepository<SalesItem,Integer> {
}
