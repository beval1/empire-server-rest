package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class CastleAlreadyExistsException extends ApiException{
    public CastleAlreadyExistsException() {
        super(HttpStatus.UNAUTHORIZED, "Building not found");
    }
    public CastleAlreadyExistsException(HttpStatus status, String message) {
        super(status, message);
    }
}
