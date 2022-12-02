package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class NoBarracksException extends ApiException{
    public NoBarracksException() {
        super(HttpStatus.BAD_REQUEST, "No barracks in the castle!");
    }
    public NoBarracksException(HttpStatus status, String message) {
        super(status, message);
    }
}
