package com.bookstoreapi.bookstoreapi.web;
import com.bookstoreapi.bookstoreapi.service.BookService;
import com.bookstoreapi.bookstoreapi.web.dto.BookFormDto;
import com.bookstoreapi.bookstoreapi.domain.Book;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping( value = "/api/admin/books")

public class BookController {

    // @Autowired
    private final BookService bookService;

    @GetMapping
    public Page<Book> paginate(@PageableDefault(sort = "title", direction = Sort.Direction.ASC, size = 5) Pageable pageable){
        //return bookRepository.findAll(PageRequest.of(0,20));
        return bookService.paginate(pageable);
    }

    @GetMapping("/list")
    public List<Book> list() {
        return bookService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Book findById(@PathVariable(value = "id") Integer id) {
        return bookService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Book create(@RequestBody @Validated BookFormDto bookFormDto) {
        return bookService.create(bookFormDto);
    }

    @PutMapping(value = "/{id}")
    public Book update(@PathVariable( value = "id") Integer id, @RequestBody @Validated BookFormDto bookFormDto) {
        return bookService.update(id,bookFormDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") Integer id) {
       bookService.delete(id);
    }

}
