package com.example.usedmarket.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PassWordNotMatchException extends RuntimeException{
    public PassWordNotMatchException(String message) {
        super(message);
    }
}
