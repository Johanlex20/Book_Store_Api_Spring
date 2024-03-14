package com.bookstoreapi.bookstoreapi.web;

import com.bookstoreapi.bookstoreapi.service.PaypalService;
import com.bookstoreapi.bookstoreapi.service.SalesOrderService;
import com.bookstoreapi.bookstoreapi.web.dto.paypal.OrderCaptureResponse;
import com.bookstoreapi.bookstoreapi.web.dto.paypal.OrderResponse;
import com.bookstoreapi.bookstoreapi.domain.Book;
import com.bookstoreapi.bookstoreapi.domain.SalesItem;
import com.bookstoreapi.bookstoreapi.domain.SalesOrder;
import com.bookstoreapi.bookstoreapi.exception.BadRequestExecpton;
import com.bookstoreapi.bookstoreapi.exception.ResourceNotFoundException;
import com.bookstoreapi.bookstoreapi.repository.BookRepository;
import com.bookstoreapi.bookstoreapi.repository.SalesItemRepository;
import com.bookstoreapi.bookstoreapi.repository.SalesOrderRepository;
import com.bookstoreapi.bookstoreapi.service.SalesOrderServiceImpl;
import com.bookstoreapi.bookstoreapi.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor

public class HomeController {

    private final BookRepository bookRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final PaypalService paypalService;
    private final SalesOrderService salesOrderService;
    private final SalesItemRepository salesItemRepository;
    private final StorageService storageService;


    @GetMapping("/last-books")
    List<Book> getLastBooks(){
        //return bookRepository.findAll(PageRequest.of(6,0,Sort.by("createdAt").descending()))
               // .stream().toList();
        return bookRepository.findTop6ByOrderByCreatedAtDesc();
    }

    @GetMapping(value = "/books")
    Page<Book> getBooks(@PageableDefault(sort = "title", direction = Sort.Direction.ASC, size = 5) Pageable pageable){
       return bookRepository.findAll(pageable);
    }

    @GetMapping(value = "/books/{slug}")
    public ResponseEntity<Book> getBookBySlug(@PathVariable(value = "slug") String slug) {

        Book book = bookRepository
                .findBySlug(slug)
                .orElse(null);

        if (book != null){
            return ResponseEntity.ok(book);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/orders/{id}")
    public ResponseEntity<SalesOrder> getOrder(@PathVariable(value = "id") Integer id) {

        SalesOrder salesOrder = salesOrderRepository
                .findById(id)
                .orElse(null);

        if (salesOrder != null){
            return ResponseEntity.ok(salesOrder);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/orders/{orderId}/items/{itemId}/book/download")
    Resource downloadBookFromSalesItem(
            @PathVariable Integer orderId,
            @PathVariable Integer itemId
    ){
        SalesOrder salesOrder = salesOrderRepository
                .findById(orderId)
                .orElseThrow(ResourceNotFoundException::new);

        if (salesOrder.getPaymentStatus().equals(SalesOrder.PaymentStatus.PENDING)){
            throw new BadRequestExecpton("The order has not been paid yet.");
        }
        SalesItem salesItem = salesItemRepository
                .findByIdAndOrder(itemId, salesOrder)
                .orElseThrow(ResourceNotFoundException::new);

        if (salesItem.getDownloadAvailable() > 0){

            salesItem.setDownloadAvailable(
                    salesItem.getDownloadAvailable() -1
            );
            salesItemRepository.save(salesItem);

            return storageService.loadAsResource(salesItem.getBook().getFilePath());
        }else {
            throw new BadRequestExecpton("Can't download this file anymore.");
        }
    }


}
