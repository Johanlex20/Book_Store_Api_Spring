package com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos;

import lombok.Data;

@Data
public class AuthRequest {

    private String email;
    private String password;

}
