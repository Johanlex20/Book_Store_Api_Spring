package com.bookstoreapi.bookstoreapi.web;

import com.bookstoreapi.bookstoreapi.domain.User;
import com.bookstoreapi.bookstoreapi.exception.BadRequestExecpton;
import com.bookstoreapi.bookstoreapi.repository.UserRepository;
import com.bookstoreapi.bookstoreapi.web.dto.SingupUserDTO;
import com.bookstoreapi.bookstoreapi.web.dto.UserProfileDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor
public class AccountController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    UserProfileDTO singup(@RequestBody @Validated SingupUserDTO singupUserDTO){
        boolean existsEmail = userRepository.existsByEmail(singupUserDTO.getEmail());
        if (existsEmail){
            throw new BadRequestExecpton("Email already exists.");
        }
        //User user = new User();

        User user = new ModelMapper().map(singupUserDTO, User.class);

        //user.setFirstName(singupUserDTO.getFirstName());
        //user.setLastName(singupUserDTO.getLastName());
        //user.setEmail(singupUserDTO.getEmail());
        //user.setFullName(singupUserDTO.getFirstName().concat(" " + singupUserDTO.getLastName()));
        //user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(singupUserDTO.getPassword()));
        user.setRole(User.Role.USER);

        userRepository.save(user);

        //return new UserProfileDTO(user.getFirstName(),user.getLastName(),user.getFullName(),user.getEmail(),user.getRole());
        return new ModelMapper().map(user, UserProfileDTO.class);

    }
}
