package com.bookstoreapi.bookstoreapi.handler;

import com.bookstoreapi.bookstoreapi.exception.BadRequestExecpton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@RestControllerAdvice
public class RestExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidationError(MethodArgumentNotValidException manve) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("Unprocessable Entity");
        problemDetail.setType(URI.create("https://api.todotic.pe/errors/unprocessable-entity"));
        problemDetail.setDetail("The entity can't processed because it has errors.");

        Set<String> errors = new HashSet<>();
        List<FieldError> fieldErrors = manve.getFieldErrors();

        for (FieldError fe : fieldErrors) {
            String message = messageSource.getMessage(fe, Locale.getDefault());
            errors.add(message);
        }
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }
        @ExceptionHandler({BadRequestExecpton.class})
        ProblemDetail handleBadRequestException(BadRequestExecpton ex){
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
            problemDetail.setTitle("Bad Request");
            problemDetail.setType(URI.create("https://api.todotic.pe/errors/bad-request"));
            return problemDetail;
        }


}
