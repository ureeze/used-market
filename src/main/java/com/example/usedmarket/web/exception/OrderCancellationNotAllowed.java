package com.example.usedmarket.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class OrderCancellationNotAllowed extends RuntimeException {

    public OrderCancellationNotAllowed(String message) {
        super(message);
    }
}