package com.example.usedmarket.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookIsNotExistException extends RuntimeException  {
    public BookIsNotExistException(String message) {
        super(message);
    }
}
