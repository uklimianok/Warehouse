package com.warehouse.demo.dto.employee.organizationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrganizationTypeRequest {
    @NotBlank 
    @Size(min = 3, max = 50)
    private String name;
}
