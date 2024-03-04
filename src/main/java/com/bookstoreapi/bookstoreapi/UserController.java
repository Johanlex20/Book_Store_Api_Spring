package com.bookstoreapi.bookstoreapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users")

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

        if (user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping()
    public ResponseEntity<User> save(@RequestBody User user){
         userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") Integer id, @RequestBody User user){

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

        User userDelete = userRepository.findById(id).orElse(null);

        if (userDelete == null){
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(userDelete);
        return ResponseEntity.notFound().build();

    }


}
