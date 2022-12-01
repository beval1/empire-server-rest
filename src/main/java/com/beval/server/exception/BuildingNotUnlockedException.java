package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class BuildingNotUnlockedException extends ApiException{
    public BuildingNotUnlockedException() {
        super(HttpStatus.BAD_REQUEST, "Building not unlocked!");
    }
    public BuildingNotUnlockedException(HttpStatus status, String message) {
        super(status, message);
    }
}
