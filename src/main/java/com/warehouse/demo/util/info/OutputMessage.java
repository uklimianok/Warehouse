package com.warehouse.demo.util.info;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OutputMessage {
    NOT_FOUND("not found."),
    EXISTS("already exists."),
    ACTIVE("is active."),
    DELETED("deleted."),
    ACCESS_DENIED("Access denied."),
    OPERATION_DENIED("Operation denied.");

    private final String message;
}
