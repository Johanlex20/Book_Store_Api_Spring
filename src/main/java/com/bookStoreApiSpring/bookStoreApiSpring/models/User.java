package com.bookStoreApiSpring.bookStoreApiSpring.models;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "fullname")
    private String fullName;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Role{
        ADMIN,
        USER
    }

    @PrePersist
    private void prepersist(){
        createdAt = LocalDateTime.now();
        fullName = firstName +" "+lastName;

    }


    @PreUpdate
    private void preUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
