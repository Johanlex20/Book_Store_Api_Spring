package com.bookstoreapi.bookstoreapi.service;

import com.bookstoreapi.bookstoreapi.domain.Book;
import com.bookstoreapi.bookstoreapi.domain.SalesItem;
import com.bookstoreapi.bookstoreapi.domain.SalesOrder;
import com.bookstoreapi.bookstoreapi.exception.ResourceNotFoundException;
import com.bookstoreapi.bookstoreapi.repository.BookRepository;
import com.bookstoreapi.bookstoreapi.repository.SalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesOrderService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    public SalesOrder create(List<Integer> bookIds){

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

        return salesOrderRepository.save(salesOrder);
    }

}
