package com.bookStoreApiSpring.bookStoreApiSpring.controllers;

import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.AuthRequest;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.AuthResponse;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.UserProfileDTO;
import com.bookStoreApiSpring.bookStoreApiSpring.exceptions.ResourceNotFoundException;
import com.bookStoreApiSpring.bookStoreApiSpring.models.User;
import com.bookStoreApiSpring.bookStoreApiSpring.repository.iUserRepository;
import com.bookStoreApiSpring.bookStoreApiSpring.security.jwt.TokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor
public class JWTController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final iUserRepository userRepository;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(),
                authRequest.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer"+ token);

        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(ResourceNotFoundException::new);

        AuthResponse authResponse = new AuthResponse(
                token,
                new UserProfileDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole()));

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(authResponse);
    }

}
