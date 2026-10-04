package com.warehouse.demo.dto.employee.department;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullDepartmentResponse extends DepartmentResponse {
    private String codeName;
    private int priority;
    private boolean isRemovable;

    public FullDepartmentResponse(
        long id, 
        String name, 
        String codeName, 
        int priority,
        boolean isRemovable
    ) {
        super(id, name);
        this.codeName = codeName;
        this.priority = priority;
        this.isRemovable = isRemovable;
    }
}
