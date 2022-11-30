package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class BuildingNotFoundException extends ApiException{
    public BuildingNotFoundException() {
        super(HttpStatus.UNAUTHORIZED, "Building not found");
    }
    public BuildingNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
