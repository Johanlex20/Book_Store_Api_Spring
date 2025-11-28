package com.bookStoreApiSpring.bookStoreApiSpring.services;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.BookFormDTO;
import com.bookStoreApiSpring.bookStoreApiSpring.exceptions.BadRequestException;
import com.bookStoreApiSpring.bookStoreApiSpring.exceptions.ResourceNotFoundException;
import com.bookStoreApiSpring.bookStoreApiSpring.models.Book;
import com.bookStoreApiSpring.bookStoreApiSpring.repository.iBookRepository;
import com.bookStoreApiSpring.bookStoreApiSpring.services.iServices.iBookService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BookService implements iBookService  {

    private final iBookRepository bookRepository;

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Page<Book> paginate(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Book findById(Integer id) {
        return bookRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Error: Book id no encontrado!"));
    }

    @Override
    public Book create(BookFormDTO bookFormDTO) {
        Book book = null;

        boolean existsSlug = bookRepository.existsBySlug(bookFormDTO.getSlug());

        if (existsSlug){
            throw new BadRequestException("ERROR DUPLICADO: El Slug ya existe");
        }

        try{
//            book = new Book();
//            book.setTitle(bookFormDTO.getTitle());
//            book.setSlug(bookFormDTO.getSlug());
//            book.setDesc(bookFormDTO.getDesc());
//            book.setPrice(bookFormDTO.getPrice());
//            book.setCreatedAt(LocalDateTime.now());

            book = new ModelMapper().map(bookFormDTO, Book.class);
        }catch (DataAccessException e){
            throw new BadRequestException("ERROR: al crear libro! ",e);
        }
        return bookRepository.save(book);
    }

    @Override
    public Book update(Integer id, BookFormDTO bookFormDTO) {
        Book book = findById(id);

        boolean existsSlug = bookRepository.existsBySlugAndIdNot(bookFormDTO.getSlug(), id);

        if (existsSlug){
            throw new BadRequestException("ERROR DUPLICADO: El Slug ya existe");
        }

        if (book != null){
//            book.setTitle(bookFormDTO.getTitle());
//            book.setSlug(bookFormDTO.getSlug());
//            book.setDesc(bookFormDTO.getDesc());
//            book.setPrice(bookFormDTO.getPrice());
//            book.setUpdatedAt(LocalDateTime.now());
            new ModelMapper().map(bookFormDTO, book);

        }else {
            throw new BadRequestException("Error al actualizar el libro!");
        }
        return bookRepository.save(book);
    }

    @Override
    public void delete(Integer id) {
        Book bookDelete = findById(id);
            bookRepository.deleteById(id);

    }
}
