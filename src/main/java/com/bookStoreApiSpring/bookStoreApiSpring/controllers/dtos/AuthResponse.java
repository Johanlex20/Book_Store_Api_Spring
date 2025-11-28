package com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;
    @JsonProperty("user")
    private UserProfileDTO userProfileDTO;
}
