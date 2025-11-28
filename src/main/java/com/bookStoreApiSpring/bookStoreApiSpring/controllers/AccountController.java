package com.bookStoreApiSpring.bookStoreApiSpring.controllers;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.SingupUserDTO;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.UserProfileDTO;
import com.bookStoreApiSpring.bookStoreApiSpring.exceptions.BadRequestException;
import com.bookStoreApiSpring.bookStoreApiSpring.models.User;
import com.bookStoreApiSpring.bookStoreApiSpring.repository.iUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor
public class AccountController {

    private final iUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/singup")
    UserProfileDTO singup(@RequestBody @Validated SingupUserDTO singupUserDTO){

        boolean existsEmail = userRepository.existsByEmail(singupUserDTO.getEmail());
        if (existsEmail){
            throw new BadRequestException("Email ya existe!.");
        }

        User user = new User();
        user.setFirstName(singupUserDTO.getFirstName());
        user.setLastName(singupUserDTO.getLastName());
        user.setEmail(singupUserDTO.getEmail());
        user.setFullName(singupUserDTO.getFirstName().concat(" " + singupUserDTO.getLastName()));
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(singupUserDTO.getPassword()));
        user.setRole(User.Role.USER);

        userRepository.save(user);

        //return new UserProfileDTO(user.getFirstName(),user.getLastName(),user.getFullName(),user.getEmail(),user.getRole());
        return new UserProfileDTO(user.getFirstName(),user.getLastName(),user.getFullName(),user.getEmail(),user.getRole());

    }

}
