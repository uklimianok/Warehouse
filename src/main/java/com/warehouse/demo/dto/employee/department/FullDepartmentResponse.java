package com.warehouse.demo.dto.employee.department;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullDepartmentResponse extends DepartmentResponse {
    private String codeName;

    public FullDepartmentResponse(long id, String name, String codeName) {
        super(id, name);
        this.codeName = codeName;
    }
}
