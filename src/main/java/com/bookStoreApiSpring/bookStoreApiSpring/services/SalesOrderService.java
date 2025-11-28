package com.bookStoreApiSpring.bookStoreApiSpring.services;
import com.bookStoreApiSpring.bookStoreApiSpring.exceptions.ResourceNotFoundException;
import com.bookStoreApiSpring.bookStoreApiSpring.models.Book;
import com.bookStoreApiSpring.bookStoreApiSpring.models.SalesItem;
import com.bookStoreApiSpring.bookStoreApiSpring.models.SalesOrder;
import com.bookStoreApiSpring.bookStoreApiSpring.models.User;
import com.bookStoreApiSpring.bookStoreApiSpring.repository.iBookRepository;
import com.bookStoreApiSpring.bookStoreApiSpring.repository.iSalesOrderRepository;
import com.bookStoreApiSpring.bookStoreApiSpring.repository.iUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SalesOrderService {

    private final iBookRepository bookRepository;
    private final iSalesOrderRepository salesOrderRepository;
    private final iUserRepository userRepository;

    public SalesOrder create(List<Integer> bookIds){
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null){
            String email = authentication.getName();

            user = userRepository.findByEmail(email)
                    .orElse(null);
        }

        SalesOrder salesOrder = new SalesOrder();
        List<SalesItem> items = new ArrayList<>();
        float total = 0;

        for (int bookId : bookIds){
            Book book = bookRepository
                    .findById(bookId)
                    .orElseThrow(ResourceNotFoundException::new);

            SalesItem salesItem = new SalesItem();
            salesItem.setBook(book);
            salesItem.setPrice(book.getPrice());
            salesItem.setDownloadAvailable(3);
            salesItem.setOrder(salesOrder);

            items.add(salesItem);
            total += salesItem.getPrice();
        }

        salesOrder.setPaymentStatus(SalesOrder.PaymentStatus.PENDING);
        salesOrder.setCreatedAt(LocalDateTime.now());
        salesOrder.setTotal(total);
        salesOrder.setItems(items);
        salesOrder.setCustomer(user);

        return salesOrderRepository.save(salesOrder);
    }

    public SalesOrder updateForPaymentCompled(Integer id){
        SalesOrder salesOrder = salesOrderRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        salesOrder.setPaymentStatus(SalesOrder.PaymentStatus.PAID);
        return salesOrderRepository.save(salesOrder);
    }

}
