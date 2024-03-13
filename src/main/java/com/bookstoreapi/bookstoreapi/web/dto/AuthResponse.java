package com.bookstoreapi.bookstoreapi.web.dto;

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
