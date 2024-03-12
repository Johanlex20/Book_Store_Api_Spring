package com.bookstoreapi.bookstoreapi.controller;

import com.bookstoreapi.bookstoreapi.controller.dto.UserFormDTO;
import com.bookstoreapi.bookstoreapi.domain.User;
import com.bookstoreapi.bookstoreapi.exception.BadRequestExecpton;
import com.bookstoreapi.bookstoreapi.exception.ResourceNotFoundException;
import com.bookstoreapi.bookstoreapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/users")

public class UserController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping("")
    public Page<User> paginete(@PageableDefault(sort = "firstName", direction = Sort.Direction.ASC, size = 5) Pageable pageable){
        return userRepository.findAll(pageable);
    }


    @GetMapping("list")
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public User findById(@PathVariable(value = "id") Integer id){
        return userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public User create(@RequestBody @Validated UserFormDTO userFormDTO){

        boolean existsEmail = userRepository.existsByEmail(userFormDTO.getEmail());

        if (existsEmail){
            throw new BadRequestExecpton("El email ya existe!");
        }

        User user = new User();

         user.setFirstName(userFormDTO.getFirstName());
         user.setLastName(userFormDTO.getLastName());
         user.setEmail(userFormDTO.getEmail());
         user.setPassword(userFormDTO.getPassword());
         user.setRole(userFormDTO.getRole());
         user.setCreatedAt(LocalDateTime.now());
         user.setFullName(userFormDTO.getFirstName().concat(" " + userFormDTO.getLastName()));

        return userRepository.save(user);
    }

    @PutMapping(value = "/{id}")
    public User update(@PathVariable(value = "id") Integer id, @RequestBody @Validated UserFormDTO userFormDTO){

        User userUpdate = userRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        boolean existsEmail = userRepository.existsByEmailAndIdNot(userFormDTO.getEmail(),id);

        if (existsEmail){
            throw new BadRequestExecpton("El email ya existe!");
        }


        if (userUpdate != null) {
            userUpdate.setFirstName(userFormDTO.getFirstName());
            userUpdate.setLastName(userFormDTO.getLastName());
            userUpdate.setFullName(userFormDTO.getFirstName().concat(" " + userFormDTO.getLastName()));
            userUpdate.setEmail(userFormDTO.getEmail());
            userUpdate.setPassword(userFormDTO.getPassword());
        }
           return userRepository.save(userUpdate);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") Integer id){

        boolean existsUser = userRepository.existsById(id);

        if (!existsUser){
            throw new ResourceNotFoundException("ID no encontrado");
        }
        userRepository.deleteById(id);


    }


}
