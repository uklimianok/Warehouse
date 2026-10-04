package com.warehouse.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(
        {EntityNotFoundException.class}
    )
    public ResponseEntity<String> returnEntityNotFoundExceptionResponse(
        EntityNotFoundException exception
    ) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(
        {DataIntegrityViolationException.class}
    )
    public ResponseEntity<String> returnDataIntegrityViolationExceptionResponse(
        DataIntegrityViolationException exception
    ) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(
        {AccessDeniedException.class}
    )
    public ResponseEntity<String> returnAccessDeniedException(
        AccessDeniedException exception
    ) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(
        {MethodArgumentNotValidException.class}
    )
    public ResponseEntity<Map<String, String>> returnMethodArgumentNotValidExceptionResponse(
        MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
            .getFieldErrors()
            .forEach(e -> errors
                .put(e.getField(), e.getDefaultMessage())
            );
        
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
