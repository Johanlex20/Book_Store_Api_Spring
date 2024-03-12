package com.bookstoreapi.bookstoreapi.repository;

import com.bookstoreapi.bookstoreapi.domain.SalesItem;
import com.bookstoreapi.bookstoreapi.domain.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalesItemRepository extends JpaRepository<SalesItem,Integer> {

    Optional<SalesItem> findByIdAndOrder(Integer id, SalesOrder salesOrder);

    Optional<SalesItem> findByIdAndOrderId(Integer id,Integer orderId);



}
