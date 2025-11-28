package com.bookStoreApiSpring.bookStoreApiSpring.controllers;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.BookFormDTO;
import com.bookStoreApiSpring.bookStoreApiSpring.models.Book;
import com.bookStoreApiSpring.bookStoreApiSpring.services.iServices.iBookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/books")
@AllArgsConstructor

public class BookController {

    private final iBookService bookService;

    @GetMapping("/list")
    List<Book> list(){
        return bookService.findAll();
    }

    @GetMapping
    Page<Book> paginate(@PageableDefault(sort = "title", direction = Sort.Direction.ASC, size = 5) Pageable pageable){
        return bookService.paginate(pageable);
    }

    @GetMapping(value = "/{id}")
    Book findById (@PathVariable(value = "id") Integer id){
        return bookService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Book create(@RequestBody @Validated BookFormDTO bookFormDTO){
        return bookService.create(bookFormDTO);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    Book update(@PathVariable(value = "id") Integer id,@RequestBody @Validated BookFormDTO bookFormDTO){
        return bookService.update(id,bookFormDTO);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable(value = "id") Integer id){
        bookService.delete(id);
    }

}
