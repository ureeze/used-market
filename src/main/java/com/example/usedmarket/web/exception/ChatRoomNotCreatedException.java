package com.example.usedmarket.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChatRoomNotCreatedException extends RuntimeException {
    public ChatRoomNotCreatedException(String message) {
        super(message);
    }
}