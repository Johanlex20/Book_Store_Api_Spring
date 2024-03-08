package com.bookstoreapi.bookstoreapi.controller;

import com.bookstoreapi.bookstoreapi.domain.User;
import com.bookstoreapi.bookstoreapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/users")

public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById(@PathVariable(value = "id") Integer id){
        User user = userRepository.findById(id).orElse(null);

        return user != null?  ResponseEntity.ok(user): ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping()
    public ResponseEntity<User> create(@RequestBody User user){

        boolean existsEmail = userRepository.existsByEmail(user.getEmail());
        if (existsEmail){
            return ResponseEntity.badRequest().build();
        }
         user.setCreatedAt(LocalDateTime.now());
         userRepository.save(user);
        return ResponseEntity.created(null).body(user);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") Integer id, @RequestBody User user){

        boolean existsEmail = userRepository.existsByEmailAndIdNot(user.getEmail(),id);

        if (existsEmail){
            return ResponseEntity.badRequest().build();
        }

        User userUpdate = userRepository.findById(id).orElse(null);

        if (userUpdate != null){
            userUpdate.setFirstName(user.getFirstName());
            userUpdate.setLastName(user.getLastName());
            userUpdate.setEmail(user.getEmail());

            userRepository.save(userUpdate);
        }else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userUpdate);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Integer id){

        boolean existsUser = userRepository.existsById(id);

        if (!existsUser){
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();

    }


}
