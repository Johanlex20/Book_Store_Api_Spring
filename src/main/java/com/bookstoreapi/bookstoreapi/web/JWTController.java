package com.bookstoreapi.bookstoreapi.web;

import com.bookstoreapi.bookstoreapi.domain.User;
import com.bookstoreapi.bookstoreapi.exception.ResourceNotFoundException;
import com.bookstoreapi.bookstoreapi.repository.UserRepository;
import com.bookstoreapi.bookstoreapi.security.TokenProvider;
import com.bookstoreapi.bookstoreapi.web.dto.AuthRequest;
import com.bookstoreapi.bookstoreapi.web.dto.AuthResponse;
import com.bookstoreapi.bookstoreapi.web.dto.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Map;

@RestController
@RequestMapping("/api")
public class JWTController {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private UserRepository userRepository;

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
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer" + token);

        User user = userRepository
                .findByEmail(authRequest.getEmail())
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
