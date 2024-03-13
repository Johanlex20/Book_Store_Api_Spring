package com.bookstoreapi.bookstoreapi.web.dto;

import com.bookstoreapi.bookstoreapi.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private User.Role role;
}
