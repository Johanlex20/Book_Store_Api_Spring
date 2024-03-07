package com.bookstoreapi.bookstoreapi.controller;

import com.bookstoreapi.bookstoreapi.controller.dto.BookFormDto;
import com.bookstoreapi.bookstoreapi.domain.Book;
import com.bookstoreapi.bookstoreapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping( value = "/api/admin/books")

public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public Page<Book> paginate(@PageableDefault(sort = "title", direction = Sort.Direction.ASC, size = 5) Pageable pageable){
        //return bookRepository.findAll(PageRequest.of(0,20));
        return bookRepository.findAll(pageable);
    }


    @GetMapping("/list")
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

    //@ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public ResponseEntity<Book> create(@RequestBody @Validated BookFormDto bookFormDto) {

        // crear un unico identificador "slug"
        boolean slugExists = bookRepository.existsBySlug(bookFormDto.getSlug());

        if (slugExists){
            return ResponseEntity.badRequest().build();
        }

        Book book = new Book();

        book.setTitle(bookFormDto.getTitle());
        book.setPrice(bookFormDto.getPrice());
        book.setSlug(bookFormDto.getSlug());
        book.setDesc(bookFormDto.getDesc());
        book.setCoverPath(bookFormDto.getCoverPath());
        book.setFilePath(bookFormDto.getFilePath());
        book.setCreatedAt(LocalDateTime.now());

        bookRepository.save(book);
        return ResponseEntity
                .created(URI.create("http://todotic.pe"))
                .body(book);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable( value = "id") Integer id, @RequestBody @Validated BookFormDto bookFormDto) {

        // crear un unico identificador "slug" con el findBySlugAndIdnot verifica todos los Slug menos el del id
        boolean slugExists = bookRepository.existsBySlugAndIdNot(bookFormDto.getSlug(), id);

        if (slugExists){
            return ResponseEntity.badRequest().build();
        }

        Book bookFromDb = bookRepository
                .findById(id)
                .orElse(null);

        if (bookFromDb == null){
            return ResponseEntity.notFound().build();
        }

        bookFromDb.setTitle(bookFormDto.getTitle());
        bookFromDb.setPrice(bookFormDto.getPrice());
        bookFromDb.setSlug(bookFormDto.getSlug());
        bookFromDb.setDesc(bookFormDto.getDesc());
        bookFromDb.setCoverPath(bookFormDto.getCoverPath());
        bookFromDb.setFilePath(bookFormDto.getFilePath());
        bookFromDb.setUpdatedAt(LocalDateTime.now());

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
        return ResponseEntity.noContent().build();
    }

}
