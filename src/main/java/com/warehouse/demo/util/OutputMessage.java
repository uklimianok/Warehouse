package com.warehouse.demo.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OutputMessage {
    NOT_FOUND("not found."),
    EXISTS("already exists."),
    ACTIVE("is active."),
    DELETED("deleted.");

    private final String message;
}
