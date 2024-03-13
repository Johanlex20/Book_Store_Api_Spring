package com.bookstoreapi.bookstoreapi.web;

import com.bookstoreapi.bookstoreapi.web.dto.BookFormDto;
import com.bookstoreapi.bookstoreapi.domain.Book;
import com.bookstoreapi.bookstoreapi.exception.BadRequestExecpton;
import com.bookstoreapi.bookstoreapi.exception.ResourceNotFoundException;
import com.bookstoreapi.bookstoreapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Book get(@PathVariable(value = "id") Integer id) {

        return bookRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Book create(@RequestBody @Validated BookFormDto bookFormDto) {

        // crear un unico identificador "slug"
        boolean slugExists = bookRepository.existsBySlug(bookFormDto.getSlug());

        if (slugExists){
            throw new BadRequestExecpton("el slug ya existe!");
        }

        Book book = new Book();

        book.setTitle(bookFormDto.getTitle());
        book.setPrice(bookFormDto.getPrice());
        book.setSlug(bookFormDto.getSlug());
        book.setDesc(bookFormDto.getDesc());
        book.setCoverPath(bookFormDto.getCoverPath());
        book.setFilePath(bookFormDto.getFilePath());
        book.setCreatedAt(LocalDateTime.now());

        return bookRepository.save(book);
    }

    @PutMapping(value = "/{id}")
    public Book update(@PathVariable( value = "id") Integer id, @RequestBody @Validated BookFormDto bookFormDto) {

        Book bookFromDb = bookRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        // crear un unico identificador "slug" con el findBySlugAndIdnot verifica todos los Slug menos el del id
        boolean slugExists = bookRepository.existsBySlugAndIdNot(bookFormDto.getSlug(), id);

        if (slugExists){
            throw new BadRequestExecpton("el slug ya existe!");
        }

        bookFromDb.setTitle(bookFormDto.getTitle());
        bookFromDb.setPrice(bookFormDto.getPrice());
        bookFromDb.setSlug(bookFormDto.getSlug());
        bookFromDb.setDesc(bookFormDto.getDesc());
        bookFromDb.setCoverPath(bookFormDto.getCoverPath());
        bookFromDb.setFilePath(bookFormDto.getFilePath());
        bookFromDb.setUpdatedAt(LocalDateTime.now());

        return bookRepository.save(bookFromDb);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") Integer id) {

        Book bookFromDb = bookRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        bookRepository.delete(bookFromDb);
    }

}
