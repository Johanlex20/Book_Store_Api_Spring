package com.bookstoreapi.bookstoreapi.controller;

import com.bookstoreapi.bookstoreapi.domain.Book;
import com.bookstoreapi.bookstoreapi.domain.SalesOrder;
import com.bookstoreapi.bookstoreapi.repository.BookRepository;
import com.bookstoreapi.bookstoreapi.repository.SalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class HomeController {
    @Autowired
    private BookRepository bookRepository;
    private SalesOrderRepository salesOrderRepository;

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

}
