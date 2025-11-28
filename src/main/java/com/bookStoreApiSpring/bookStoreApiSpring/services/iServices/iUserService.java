package com.bookStoreApiSpring.bookStoreApiSpring.services.iServices;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.UserFormDTO;
import com.bookStoreApiSpring.bookStoreApiSpring.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface iUserService {
    List<User> findAll();

    Page<User> paginate(Pageable pageable);

    User findById (Integer id);

    User create(UserFormDTO userFormDTO);

    User update(Integer id, UserFormDTO userFormDTO);

    void delete(Integer id);

}
