package com.bookStoreApiSpring.bookStoreApiSpring.controllers;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.UserFormDTO;
import com.bookStoreApiSpring.bookStoreApiSpring.models.User;
import com.bookStoreApiSpring.bookStoreApiSpring.services.iServices.iUserService;
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
@RequestMapping(value = "/api/admin/users")
@AllArgsConstructor
public class UserController {

    private final iUserService userService;

    @GetMapping(value = "/list")
    List<User> findAll(){
        return userService.findAll();
    }

    @GetMapping
    Page<User> paginate(@PageableDefault(sort = "fullName", direction = Sort.Direction.ASC, size = 10) Pageable pageable){
        return userService.paginate(pageable);
    }

    @GetMapping(value = "/{id}")
    User findById (@PathVariable(value = "id") Integer id){
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    User create(@RequestBody @Validated UserFormDTO userFormDTO){
        return userService.create(userFormDTO);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    User update(@PathVariable(value = "id") Integer id, @RequestBody @Validated UserFormDTO userFormDTO){
        return userService.update(id, userFormDTO);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable(value = "id") Integer id){
        userService.delete(id);
    }
}
