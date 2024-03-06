package com.bookstoreapi.bookstoreapi.repository;

import com.bookstoreapi.bookstoreapi.domain.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Integer> {
}
