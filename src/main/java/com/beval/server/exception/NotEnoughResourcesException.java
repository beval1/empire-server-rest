package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class NotEnoughResourcesException extends ApiException{
    public NotEnoughResourcesException() {
        super(HttpStatus.BAD_REQUEST, "Not enough resources!");
    }
    public NotEnoughResourcesException(HttpStatus status, String message) {
        super(status, message);
    }
}
