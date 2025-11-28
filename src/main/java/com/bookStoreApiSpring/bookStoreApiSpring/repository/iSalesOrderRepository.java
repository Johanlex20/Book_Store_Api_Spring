package com.bookStoreApiSpring.bookStoreApiSpring.repository;
import com.bookStoreApiSpring.bookStoreApiSpring.models.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iSalesOrderRepository extends JpaRepository<SalesOrder,Integer> {
}
