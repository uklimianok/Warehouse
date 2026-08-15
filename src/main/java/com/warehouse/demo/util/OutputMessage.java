package com.warehouse.demo.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OutputMessage {
    NOT_FOUND("not found."),
    EXISTS("already exists."),
    ACTIVE("is active."),
    DELETED("deleted."),
    WORK_STATION_NOT_REQUIRED("must not contain current position."),
    WORK_STATION_REQUIRED("must contain current position."),
    NEXT_WORK_STATION_REQUIRED("must contain next position."),
    NEXT_WORK_STATION_NOT_REQUIRED("must not contain next position."),
    WORK_STATIONS_REQUIRED("must contain current and next position."),
    WORK_STATIONS_NOT_REQUIRED("must not contain any position."),
    GATE_REQUIRED("must contain any gate.");

    private final String message;
}
