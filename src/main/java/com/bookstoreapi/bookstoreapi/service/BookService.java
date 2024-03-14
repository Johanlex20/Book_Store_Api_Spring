package com.bookstoreapi.bookstoreapi.service;
import com.bookstoreapi.bookstoreapi.domain.Book;
import com.bookstoreapi.bookstoreapi.exception.BadRequestExecpton;
import com.bookstoreapi.bookstoreapi.exception.ResourceNotFoundException;
import com.bookstoreapi.bookstoreapi.repository.BookRepository;
import com.bookstoreapi.bookstoreapi.web.dto.BookFormDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor

public class BookService {

    private final BookRepository bookRepository;

    public Page<Book> paginate(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Integer id) {
        return bookRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public Book create(BookFormDto bookFormDto) {
        // crear un unico identificador "slug"
        boolean slugExists = bookRepository.existsBySlug(bookFormDto.getSlug());

        if (slugExists) {
            throw new BadRequestExecpton("el slug ya existe!");
        }
        //Book book = new Book();
        //book.setTitle(bookFormDto.getTitle());
        //book.setPrice(bookFormDto.getPrice());
        //book.setSlug(bookFormDto.getSlug());
        //book.setDesc(bookFormDto.getDesc());
        //book.setCoverPath(bookFormDto.getCoverPath());
        //book.setFilePath(bookFormDto.getFilePath());
        //book.setCreatedAt(LocalDateTime.now());

        Book book = new ModelMapper().map(bookFormDto, Book.class);

        return bookRepository.save(book);
    }

    public Book update( Integer id, BookFormDto bookFormDto) {
        Book bookFromDb = findById(id);
        // crear un unico identificador "slug" con el findBySlugAndIdnot verifica todos los Slug menos el del id
        boolean slugExists = bookRepository.existsBySlugAndIdNot(bookFormDto.getSlug(), id);

        if (slugExists){
            throw new BadRequestExecpton("el slug ya existe!");
        }
//        bookFromDb.setTitle(bookFormDto.getTitle());
//        bookFromDb.setPrice(bookFormDto.getPrice());
//        bookFromDb.setSlug(bookFormDto.getSlug());
//        bookFromDb.setDesc(bookFormDto.getDesc());
//        bookFromDb.setCoverPath(bookFormDto.getCoverPath());
//        bookFromDb.setFilePath(bookFormDto.getFilePath());
//        bookFromDb.setUpdatedAt(LocalDateTime.now());

        new ModelMapper().map(bookFormDto, bookFromDb);
        return bookRepository.save(bookFromDb);
    }

    public void delete(Integer id) {
        Book bookFromDb = findById(id);
        bookRepository.delete(bookFromDb);
    }


}
