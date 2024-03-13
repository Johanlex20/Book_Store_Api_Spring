package com.bookstoreapi.bookstoreapi.web.dto;

import com.bookstoreapi.bookstoreapi.domain.User;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private User.Role role;

    @PrePersist
    private void prepersist(){
        fullName = firstName +" "+lastName;
    }
}
