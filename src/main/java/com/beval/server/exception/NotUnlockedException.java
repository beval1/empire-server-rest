package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class NotUnlockedException extends ApiException{
    public NotUnlockedException() {
        super(HttpStatus.BAD_REQUEST, "Not unlocked!");
    }
    public NotUnlockedException(HttpStatus status, String message) {
        super(status, message);
    }
}
