package com.warehouse.demo.dto.service.actionLog;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class ActionLogResponse {
    private long id;
    private String employeeNumber;
    private LocalDateTime proceededAt;
    private String entityType;
    private long entityId;
    private String action;
}
