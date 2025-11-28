package com.bookStoreApiSpring.bookStoreApiSpring.services.iServices;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.BookFormDTO;
import com.bookStoreApiSpring.bookStoreApiSpring.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface iBookService {

     List<Book> findAll();

    Page<Book> paginate(Pageable pageable);

     Book findById (Integer id);

     Book create(BookFormDTO bookFormDTO);

     Book update(Integer id, BookFormDTO bookFormDTO);

     void delete(Integer id);

}
