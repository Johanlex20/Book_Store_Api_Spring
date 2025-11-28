package com.bookStoreApiSpring.bookStoreApiSpring.exceptions;

import org.springframework.dao.DataAccessException;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String s) {
        super (s);
    }

    public BadRequestException(String s, Throwable e) {
        super(s,e);
    }
}
