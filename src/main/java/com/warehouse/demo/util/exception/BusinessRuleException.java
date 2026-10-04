package com.warehouse.demo.util.exception;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(
        String message
    ) {
        super(message);
    }
}
