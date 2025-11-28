package com.bookStoreApiSpring.bookStoreApiSpring.handler;
import com.bookStoreApiSpring.bookStoreApiSpring.exceptions.BadRequestException;
import com.bookStoreApiSpring.bookStoreApiSpring.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.crypto.MarshalException;
import java.net.URI;
import java.util.*;

@RestControllerAdvice
public class RestHandlerException {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handlerValidationException(MethodArgumentNotValidException manve){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("Solicitud no procesada!");
        problemDetail.setType(URI.create("http://good-company.com/unprocessable-entity"));
        problemDetail.setDetail("La solicitud no puede procesarse porque tiene errores.");

        Set<String> errors = new HashSet<>();
        List<FieldError> fieldErrors = manve.getFieldErrors();

        for (FieldError fe : fieldErrors){
            String message = messageSource.getMessage(fe, Locale.getDefault());
            errors.add(message);
        }

        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail badRequestException(BadRequestException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Solicitud Incorrecta");
        problemDetail.setType(URI.create("http://good-company.com/badRequest"));
        return problemDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handlerResourceNotFound(ResourceNotFoundException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Solicitud No encontrada");
        problemDetail.setType(URI.create("http://good-company.com/NotFound"));
        return problemDetail;
    }
}
