package com.bookstoreapi.bookstoreapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping( value = "/api/admin/books")

public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping()
    public List<Book> list() {
        return bookRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Book> get(@PathVariable(value = "id") Integer id) {

        Book book = bookRepository
                .findById(id)
                .orElse(null);

        if (book != null){
            return ResponseEntity.ok(book);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping(value = "/{id}")
   public ResponseEntity<?> update(@PathVariable( value = "id") Integer id, @RequestBody Book bookForm) {

        Book bookFromDb = bookRepository
                .findById(id)
                .orElse(null);

        if (bookFromDb == null){
            return ResponseEntity.notFound().build();
        }

        bookFromDb.setTitle(bookForm.getTitle());
        bookFromDb.setPrice(bookForm.getPrice());

        bookRepository.save(bookFromDb);

        return ResponseEntity.ok(bookFromDb);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Integer id) {

        Book bookFromDb = bookRepository
                .findById(id)
                .orElse(null);

        if (bookFromDb == null){
            return ResponseEntity.notFound().build();
        }

        bookRepository.delete(bookFromDb);
        return ResponseEntity.notFound().build();
    }

}
