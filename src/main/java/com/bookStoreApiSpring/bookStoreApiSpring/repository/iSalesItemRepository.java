package com.bookStoreApiSpring.bookStoreApiSpring.repository;
import com.bookStoreApiSpring.bookStoreApiSpring.models.SalesItem;
import com.bookStoreApiSpring.bookStoreApiSpring.models.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface iSalesItemRepository extends JpaRepository<SalesItem, Integer> {
    Optional<SalesItem> findByIdAndOrder(Integer id, SalesOrder salesOrder);
}
