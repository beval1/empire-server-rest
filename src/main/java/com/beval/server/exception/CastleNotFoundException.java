package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class CastleNotFoundException extends ApiException{
    public CastleNotFoundException() {
        super(HttpStatus.UNAUTHORIZED, "Castle not found");
    }
    public CastleNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
