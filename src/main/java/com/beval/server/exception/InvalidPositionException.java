package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class InvalidPositionException extends ApiException{
    public InvalidPositionException() {
        super(HttpStatus.BAD_REQUEST, "Invalid position!");
    }
    public InvalidPositionException(HttpStatus status, String message) {
        super(status, message);
    }
}
