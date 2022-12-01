package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class BuildingNotDestroyableException extends ApiException{
    public BuildingNotDestroyableException() {
        super(HttpStatus.BAD_REQUEST, "Building not destroyable!");
    }
    public BuildingNotDestroyableException(HttpStatus status, String message) {
        super(status, message);
    }
}
