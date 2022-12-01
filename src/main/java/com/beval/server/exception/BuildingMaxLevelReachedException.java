package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class BuildingMaxLevelReachedException extends ApiException{
    public BuildingMaxLevelReachedException() {
        super(HttpStatus.BAD_REQUEST, "Building max level reached!");
    }
    public BuildingMaxLevelReachedException(HttpStatus status, String message) {
        super(status, message);
    }
}
