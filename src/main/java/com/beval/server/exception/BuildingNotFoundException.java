package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class BuildingNotFoundException extends ApiException{
    public BuildingNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Building not found");
    }
    public BuildingNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
