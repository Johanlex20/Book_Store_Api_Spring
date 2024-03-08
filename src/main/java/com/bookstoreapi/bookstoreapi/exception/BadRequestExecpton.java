package com.bookstoreapi.bookstoreapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestExecpton extends RuntimeException{

    public BadRequestExecpton(String message){
        super(message);
    }

}
