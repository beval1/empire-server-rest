package com.beval.server.exception.handler;

import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //exceptions for user authentications are later caught by the authenticationEntryPoint if not here
    //but this is required because of swagger
    @ExceptionHandler({DisabledException.class, BadCredentialsException.class,
            LockedException.class, CredentialsExpiredException.class, AccountExpiredException.class})
    public ResponseEntity<Object> handleAccountExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseDTO
                        .builder()
                        .message(ex.getMessage())
                        .content(null)
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleInvalidPayload(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseDTO
                        .builder()
                        .message("Invalid payload!")
                        .content(null)
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build()
        );
    }

    @ExceptionHandler({ResourceNotFoundException.class, RoleNotFoundException.class,
            UserAlreadyExistsException.class, NotAuthorizedException.class, UserBannedException.class,
            BuildingNotFoundException.class, CastleNotFoundException.class, MaxBuildingLimitReachedException.class,
            CastleAlreadyExistsException.class, NotEnoughResourcesException.class
    })
    public ResponseEntity<Object> handleCustomExceptions(ApiException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(
                        ResponseDTO
                                .builder()
                                .message(ex.getMessage())
                                .content(null)
                                .status(ex.getStatus().value())
                                .build()
                );
    }

    //@PreAuthorize handler
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDenied(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ResponseDTO
                                .builder()
                                .message(ex.getMessage())
                                .status(HttpStatus.FORBIDDEN.value())
                                .content(null)
                                .build()
                );
    }

}
