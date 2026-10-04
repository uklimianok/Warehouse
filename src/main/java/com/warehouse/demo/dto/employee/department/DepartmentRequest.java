package com.warehouse.demo.dto.employee.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DepartmentRequest {
    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9\\s]+ Department")
    private String name;

    @NotNull
    @Positive 
    private Integer priority;
}
