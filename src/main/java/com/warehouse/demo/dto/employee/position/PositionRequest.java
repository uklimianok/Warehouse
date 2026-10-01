package com.warehouse.demo.dto.employee.position;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PositionRequest {
    private String name;
    private boolean isEnabled;
    private long departmentId;
    private List<Long> inheritedPositionsId;
    private Map<String, List<String>> controllerFlags;
}
