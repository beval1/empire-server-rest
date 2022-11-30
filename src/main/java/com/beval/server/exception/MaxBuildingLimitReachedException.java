package com.beval.server.exception;

import com.beval.server.exception.ApiException;
import org.springframework.http.HttpStatus;

public class MaxBuildingLimitReachedException extends ApiException {
    public MaxBuildingLimitReachedException() {
        super(HttpStatus.BAD_REQUEST, "Max building limit reached!");
    }
    public MaxBuildingLimitReachedException(HttpStatus status, String message) {
        super(status, message);
    }
}
