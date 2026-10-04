package com.warehouse.demo.dto.employee.shift;

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
public class ShiftRequest {
    @NotBlank 
    @Size(min = 1, max = 30)
    private String symbol;
}
