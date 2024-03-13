package com.bookstoreapi.bookstoreapi.web;

import com.bookstoreapi.bookstoreapi.domain.User;
import com.bookstoreapi.bookstoreapi.exception.BadRequestExecpton;
import com.bookstoreapi.bookstoreapi.repository.UserRepository;
import com.bookstoreapi.bookstoreapi.web.dto.SingupUserDTO;
import com.bookstoreapi.bookstoreapi.web.dto.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.PipedReader;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api")
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    UserProfileDTO singup(@RequestBody @Validated SingupUserDTO singupUserDTO){
        boolean existsEmail = userRepository.existsByEmail(singupUserDTO.getEmail());
        if (existsEmail){
            throw new BadRequestExecpton("Email already exists.");
        }
        User user = new User();

        user.setFirstName(singupUserDTO.getFirstName());
        user.setLastName(singupUserDTO.getLastName());
        user.setEmail(singupUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(singupUserDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setFullName(singupUserDTO.getFirstName().concat(" " + singupUserDTO.getLastName()));
        user.setRole(User.Role.USER);

        userRepository.save(user);

        return new UserProfileDTO(user.getFirstName(),user.getLastName(),user.getFullName(),user.getEmail(),user.getRole());

    }



}
