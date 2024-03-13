package com.bookstoreapi.bookstoreapi.web.dto;

import com.bookstoreapi.bookstoreapi.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponse {

    private String token;
    @JsonProperty("user")
    private UserProfileDTO userProfileDTO;

}
