package com.warehouse.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.warehouse.demo.util.exception.BusinessRuleException;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestControllerAdvice 
public class ControllerExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(
        EntityNotFoundException exception
    ) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(
        DataIntegrityViolationException exception
    ) {
        log.warn("Database constraint violated", exception);
        return new ResponseEntity<>("The operation conflicts with existing data.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(
        AccessDeniedException exception
    ) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(
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

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<String> handleBusinessRule(
        BusinessRuleException exception
    ) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
}
